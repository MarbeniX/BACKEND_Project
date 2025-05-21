package com.backend.projectbackend.controllers;

import com.backend.projectbackend.dto.training.FinishSessionDTO;
import com.backend.projectbackend.dto.training.GetTrainingSessionById;
import com.backend.projectbackend.dto.training.StartSessionDataDTO;
import com.backend.projectbackend.model.User;
import com.backend.projectbackend.service.TrainingService;
import com.backend.projectbackend.util.responses.ApiResponse;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.List;

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

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> finishTraining(@Valid @RequestBody FinishSessionDTO request, @PathVariable String id, Authentication authentication) throws MessagingException, UnsupportedEncodingException {
        User user = (User) authentication.getPrincipal(); // Usuario autenticado a partir del JWT
        ApiResponse<String> response = trainingService.finishTraining(request, id, user);
        if (!response.isSuccess()) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<GetTrainingSessionById>> getTrainingSessionById(@PathVariable String id, Authentication authentication) throws MessagingException, UnsupportedEncodingException {
        User user = (User) authentication.getPrincipal(); // Usuario autenticado a partir del JWT
        ApiResponse<GetTrainingSessionById> response = trainingService.getTrainingSessionById(id, user);
        if (!response.isSuccess()) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/trainingSessions")
    public ResponseEntity<ApiResponse<List<GetTrainingSessionById>>> getAllTrainingSession(Authentication authentication) throws MessagingException, UnsupportedEncodingException {
        User user = (User) authentication.getPrincipal(); // Usuario autenticado a partir del JWT
        ApiResponse<List<GetTrainingSessionById>> response = trainingService.getAllTrainingSession(user);
        if (!response.isSuccess()) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.status(201).body(response);
    }
}
