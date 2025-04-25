package com.backend.projectbackend.util.security;

import com.backend.projectbackend.model.User;
import com.backend.projectbackend.repository.AuthRepository;
import com.backend.projectbackend.util.token.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthRepository authRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response); // No token => sigue con el flujo normal (puede fallar luego si la ruta requiere auth)
            return;
        }

        String token = authHeader.substring(7); // Remueve "Bearer "

        try {
            String userId = jwtUtil.getUserIdFromToken(token);
            Optional<User> userOptional = authRepository.findById(new ObjectId(userId));

            if (userOptional.isPresent()) {
                User user = userOptional.get();

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // Token inválido o error
            return;
        }

        filterChain.doFilter(request, response); // Continúa con el siguiente filtro o endpoint
    }
}
