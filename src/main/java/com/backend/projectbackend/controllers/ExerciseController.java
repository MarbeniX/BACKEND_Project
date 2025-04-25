package com.backend.projectbackend.controllers;

import com.backend.projectbackend.dto.exercise.ExerciseCreateDTO;
import com.backend.projectbackend.dto.routine.RoutineAddExerciseDTO;
import com.backend.projectbackend.model.User;
import com.backend.projectbackend.service.ExerciseService;
import com.backend.projectbackend.util.responses.ApiResponse;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.List;

@RestController
@RequestMapping("/api/exercise")
public class ExerciseController {
    private final ExerciseService exerciseService;

    public ExerciseController(final ExerciseService exerciseService) {
        this.exerciseService = exerciseService;
    }

    @PostMapping("/add-exercise")
    public ResponseEntity<ApiResponse<String>> addExercise(@Valid @RequestBody ExerciseCreateDTO request, Authentication authentication) throws MessagingException, UnsupportedEncodingException {
        User user = (User) authentication.getPrincipal(); // Usuario autenticado a partir del JWT
        ApiResponse<String> response = exerciseService.addExercise(request, user);
        if (!response.isSuccess()) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.status(201).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> updateExercise(@PathVariable String id, @Valid @RequestBody ExerciseCreateDTO request, Authentication authentication) throws MessagingException, UnsupportedEncodingException {
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
}
