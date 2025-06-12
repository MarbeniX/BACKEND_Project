package com.backend.projectbackend.controllers;

import com.backend.projectbackend.dto.exercise.ExerciseCreateDTO;
import com.backend.projectbackend.dto.exercise.ExerciseGetByIdDTO;
import com.backend.projectbackend.dto.routine.RoutineAddExerciseDTO;
import com.backend.projectbackend.dto.user.CloudinaryImageDTO;
import com.backend.projectbackend.model.User;
import com.backend.projectbackend.service.CloudinaryService;
import com.backend.projectbackend.service.ExerciseService;
import com.backend.projectbackend.util.responses.ApiResponse;
import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

@RestController
@RequestMapping("/api/exercise")
public class ExerciseController {
    private final ExerciseService exerciseService;

    public ExerciseController(final ExerciseService exerciseService) {
        this.exerciseService = exerciseService;
    }

    @PostMapping(value = "/add-exercise")
    public ResponseEntity<ApiResponse<String>> addExercise(@ModelAttribute ExerciseCreateDTO request, Authentication authentication) throws MessagingException, UnsupportedEncodingException {
        User user = (User) authentication.getPrincipal(); // Usuario autenticado a partir del JWT
        try {
            MultipartFile image = request.getImage();
            CloudinaryImageDTO dataImage = CloudinaryService.uploadImageExercise(image);
            request.setImageURL(dataImage.getUrl());
            request.setPublicID(dataImage.getPublicID());
        } catch (IOException e) {
            e.printStackTrace();
        }
        ApiResponse<String> response = exerciseService.addExercise(request, user);
        if (!response.isSuccess()) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.status(201).body(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> updateExercise(@PathVariable String id, @ModelAttribute ExerciseCreateDTO request, Authentication authentication) throws MessagingException, UnsupportedEncodingException {
        User user = (User) authentication.getPrincipal(); // Usuario autenticado a partir del JWT
        ApiResponse<String> response = exerciseService.updateExercise(id, request, user);
        if (!response.isSuccess()) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/getAllExercises")
    public ResponseEntity<ApiResponse<List<RoutineAddExerciseDTO>>> getAllExercises(Authentication authentication) throws MessagingException, UnsupportedEncodingException {
        User user = (User) authentication.getPrincipal();
        ApiResponse<List<RoutineAddExerciseDTO>> response = exerciseService.getAllExercises(user);
        if (!response.isSuccess()) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.status(201).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteExercise(@PathVariable String id, Authentication authentication) throws MessagingException, UnsupportedEncodingException {
        User user = (User) authentication.getPrincipal(); // Usuario autenticado a partir del JWT
        ApiResponse<String> response = exerciseService.deleteExercise(id, user);
        if (!response.isSuccess()) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ExerciseGetByIdDTO>> getExerciseById(@PathVariable String id, Authentication authentication) throws MessagingException, UnsupportedEncodingException {
        User user = (User) authentication.getPrincipal();
        ApiResponse<ExerciseGetByIdDTO> response = exerciseService.getExerciseById(id, user);
        if (!response.isSuccess()) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.status(201).body(response);
    }
}
