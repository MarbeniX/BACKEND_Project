package com.backend.projectbackend.controllers;

//Controller - ENDPOINTS

import com.backend.projectbackend.dto.auth.*;
import com.backend.projectbackend.model.User;
import com.backend.projectbackend.service.AuthService;
import com.backend.projectbackend.util.responses.ApiResponse;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/create-account")
    public ResponseEntity<ApiResponse<String>> createAccount(@Valid @RequestBody AuthCreateAccountDTO request) throws MessagingException, UnsupportedEncodingException {
        ApiResponse<String> response = authService.createAccount(request);
        if (!response.isSuccess()) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.status(201).body(response);
    }

    @PostMapping("/{token}")
    public ResponseEntity<ApiResponse<String>> confirmAccount(@PathVariable String token) throws MessagingException, UnsupportedEncodingException {
        ApiResponse<String> response = authService.confirmAccount(token);
        if (!response.isSuccess()) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.status(201).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> login(@Valid @RequestBody AuthLoginDTO request) throws MessagingException, UnsupportedEncodingException {
        ApiResponse<String> response = authService.login(request);
        if (!response.isSuccess()) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.status(201).body(response);
    }

    @PostMapping("/req-code")
    public ResponseEntity<ApiResponse<String>> requestCode(@Valid @RequestBody RequestCodeDTO request) throws MessagingException, UnsupportedEncodingException {
        ApiResponse<String> response = authService.requestCode(request);
        if (!response.isSuccess()) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.status(201).body(response);
    }

    @PostMapping("/req-passreset-code")
    public ResponseEntity<ApiResponse<String>> reqPassResetCode(@Valid @RequestBody ReqPassResetCodeDTO request) throws MessagingException, UnsupportedEncodingException {
        ApiResponse<String> response = authService.reqPassResetCode(request);
        if (!response.isSuccess()) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.status(201).body(response);
    }

    @PostMapping("/update-password/{token}")
    public ResponseEntity<ApiResponse<String>> updatePassword(@PathVariable String token, @Valid @RequestBody UpdatePasswordDTO request) throws MessagingException, UnsupportedEncodingException {
        ApiResponse<String> response = authService.updatePassword(token, request);
        if (!response.isSuccess()) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.status(201).body(response);
    }

    @PostMapping("/validate/{token}")
    public ResponseEntity<ApiResponse<String>> validateToken(@PathVariable String token) throws MessagingException, UnsupportedEncodingException {
        ApiResponse<String> response = authService.validateToken(token);
        if (!response.isSuccess()) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/user")
    public ResponseEntity<?> getUserInfo(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(new UserDTO(user));
    }
}
