package com.backend.projectbackend.controllers;

import com.backend.projectbackend.dto.training.StartSessionDataDTO;
import com.backend.projectbackend.model.User;
import com.backend.projectbackend.service.TrainingService;
import com.backend.projectbackend.util.responses.ApiResponse;
import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/api/training")
public class TrainingController {
    private final TrainingService trainingService;

    public TrainingController(TrainingService trainingService) {
        this.trainingService = trainingService;
    }

    @PostMapping("/start")
    public ResponseEntity<ApiResponse<String>> startTraining(@RequestBody StartSessionDataDTO request, Authentication authentication) throws MessagingException, UnsupportedEncodingException {
        User user = (User) authentication.getPrincipal(); // Usuario autenticado a partir del JWT
        ApiResponse<String> response = trainingService.startTraining(request, user);
        if (!response.isSuccess()) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.status(201).body(response);
    }
}
