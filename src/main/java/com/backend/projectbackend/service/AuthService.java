package com.backend.projectbackend.service;

//Service - logic

import com.backend.projectbackend.dto.auth.*;
import com.backend.projectbackend.model.Token;
import com.backend.projectbackend.model.User;
import com.backend.projectbackend.repository.AuthRepository;
import com.backend.projectbackend.repository.TokenRepository;
import com.backend.projectbackend.util.email.EmailService;
import com.backend.projectbackend.util.responses.ApiResponse;
import com.backend.projectbackend.util.token.JwtUtil;
import com.backend.projectbackend.util.token.TokenGenerator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class AuthService {
    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final TokenRepository tokenRepository;
    private final JwtUtil jwtUtil;

    public AuthService(AuthRepository authRepository, PasswordEncoder passwordEncoder, EmailService emailService, TokenRepository tokenRepository, JwtUtil jwtUtil) {
        this.authRepository = authRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.tokenRepository = tokenRepository;
        this.jwtUtil = jwtUtil;
    }

    public ApiResponse<String> createAccount(AuthCreateAccountDTO request) {
        if (authRepository.existsByEmail(request.getEmail())) {
            return new ApiResponse<>(false, "El correo ya está registrado.", null);
        }

        try {
            User user = new User();
            user.setEmail(request.getEmail());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setUsername(request.getUsername());

            authRepository.save(user);

            Token token = new Token();
            String generatedToken = TokenGenerator.generateToken();
            token.setTokenValue(generatedToken);
            token.setUserId(user.getId());

            tokenRepository.save(token);

            emailService.sendConfirmationEmail(
                    user.getEmail(),
                    user.getUsername(),
                    token.getTokenValue()
            );
            return new ApiResponse<>(true, "We sent you a confirmation email.", null);
        } catch (Exception e) {
            e.printStackTrace(); // Puedes registrar con un logger en vez de imprimir
            return new ApiResponse<>(false, "Internal server error: " + e.getMessage(), null);
        }
    }

    public ApiResponse<String> confirmAccount(AuthConfirmAccountDTO request) {
        try{
            Token tokenExists = tokenRepository.findByTokenValue(request.getToken());
            if (tokenExists == null) {
                return new ApiResponse<>(false, "Invalid token", null);
            }

            User userExists = authRepository.findById(tokenExists.getUserId()).get();
            if (userExists == null) {
                return new ApiResponse<>(false, "User not found", null);
            }

            userExists.setConfirmed();
            authRepository.save(userExists);
            tokenRepository.delete(tokenExists);
            return new ApiResponse<>(true, "Account confirmed", null);
        }catch (Exception e){
            e.printStackTrace();
            return new ApiResponse<>(false, "Internal server error: " + e.getMessage(), null);
        }
    }

    public ApiResponse<String> login(AuthLoginDTO request) {
        try{
            User userExist = authRepository.findByEmail(request.getEmail()).get();
            if (userExist == null) {
                return new ApiResponse<>(false, "Invalid credentials", null);
            }

            if(userExist.getConfirmed() == false) {
                Token token = new Token();
                String generatedToken = TokenGenerator.generateToken();
                token.setTokenValue(generatedToken);
                token.setUserId(userExist.getId());
                emailService.sendConfirmationEmail(
                        userExist.getEmail(),
                        userExist.getUsername(),
                        token.getTokenValue()
                );
                tokenRepository.save(token);
                return new ApiResponse<>(false, "Account not confirmed, check your email", null);
            }

            if(userExist.getPassword().equals(passwordEncoder.encode(request.getPassword()))) {
                return new ApiResponse<>(false, "Wront credentials", null);
            }

            String token = jwtUtil.generateToken(userExist.getId().toString(), userExist.getAdmin() );
            return new ApiResponse<>(true, "Hi!", token);
        } catch (Exception e){
            e.printStackTrace();
            return new ApiResponse<>(false, "Internal server error: " + e.getMessage(), null);
        }
    }

    public ApiResponse<String> requestCode(RequestCodeDTO request) {
        try{
            User userExists = authRepository.findByEmail(request.getEmail()).get();
            if (userExists == null) {
                return new ApiResponse<>(false, "User not found", null);
            }

            if(userExists.getConfirmed() == true) {
                return new ApiResponse<>(false, "Email already confirmed", null);
            }

            Token token = new Token();
            String generatedToken = TokenGenerator.generateToken();
            token.setTokenValue(generatedToken);
            token.setUserId(userExists.getId());

            tokenRepository.save(token);
            authRepository.save(userExists);

            emailService.sendConfirmationEmail(
                    userExists.getEmail(),
                    userExists.getUsername(),
                    token.getTokenValue()
            );

            return new ApiResponse<>(true, "Confirmation email sent", null);
        }catch (Exception e){
            e.printStackTrace();
            return new ApiResponse<>(false, "Internal server error: " + e.getMessage(), null);
        }
    }

    public ApiResponse<String> reqPassResetCode(ReqPassResetCodeDTO request) {
        try{
            User userExists = authRepository.findByEmail(request.getEmail()).get();
            if (userExists == null) {
                return new ApiResponse<>(false, "User not found", null);
            }
            Token token = new Token();
            String generatedToken = TokenGenerator.generateToken();
            token.setTokenValue(generatedToken);
            token.setUserId(userExists.getId());

            tokenRepository.save(token);
            authRepository.save(userExists);

            emailService.sendResetPasswordEmail(
                    userExists.getEmail(),
                    userExists.getUsername(),
                    token.getTokenValue()
            );
            return new ApiResponse<>(true, "Check your email to reset your password", null);
        }catch (Exception e){
            e.printStackTrace();
            return new ApiResponse<>(false, "Internal server error: " + e.getMessage(), null);
        }
    }

    public ApiResponse<String> validateToken(AuthConfirmAccountDTO request) {
        try{
            Token tokenExists = tokenRepository.findByTokenValue(request.getToken());
            if (tokenExists == null) {
                return new ApiResponse<>(false, "Invalid token", null);
            }
            return new ApiResponse<>(true, "Valid token", null);
        }catch (Exception e){
            e.printStackTrace();
            return new ApiResponse<>(false, "Internal server error: " + e.getMessage(), null);
        }
    }

    public ApiResponse<String> updatePassword(String token, UpdatePasswordDTO request) {
        try{
            Token tokenExists = tokenRepository.findByTokenValue(token);
            if (tokenExists == null) {
                return new ApiResponse<>(false, "Invalid token", null);
            }
            if (!request.getNewPassword().equals(request.getConfirmPassword())) {
                return new ApiResponse<>(false, "Passwords do not match", null);
            }

            User user = authRepository.findById(tokenExists.getUserId()).get();
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
            authRepository.save(user);
            tokenRepository.delete(tokenExists);

            return new ApiResponse<>(true, "Password update", null);
        }catch (Exception e){
            e.printStackTrace();
            return new ApiResponse<>(false, "Internal server error: " + e.getMessage(), null);
        }
    }
}
