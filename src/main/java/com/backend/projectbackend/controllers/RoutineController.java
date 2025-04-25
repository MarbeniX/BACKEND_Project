package com.backend.projectbackend.controllers;

import com.backend.projectbackend.dto.routine.RoutineAddExerciseDTO;
import com.backend.projectbackend.dto.routine.RoutineResponseDTO;
import com.backend.projectbackend.model.Exercise.Muscle;
import com.backend.projectbackend.model.Exercise.Difficulty;
import com.backend.projectbackend.dto.routine.RoutineCreateDTO;
import com.backend.projectbackend.model.User;
import com.backend.projectbackend.service.RoutineService;
import com.backend.projectbackend.util.responses.ApiResponse;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.List;

//ENDPOINTS

@RestController
@RequestMapping("/api/routine")
public class RoutineController {
    private final RoutineService routineService;

    public RoutineController(RoutineService routineService) {
        this.routineService = routineService;
    }

    @PostMapping("/create-routine")
    public ResponseEntity<ApiResponse<String>> createRoutine(@Valid @RequestBody RoutineCreateDTO request, Authentication authentication) throws MessagingException, UnsupportedEncodingException {
        User user = (User) authentication.getPrincipal(); // Usuario autenticado a partir del JWT
        ApiResponse<String> response = routineService.createRoutine(request, user);
        if (!response.isSuccess()) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/getRoutines")
    public ResponseEntity<ApiResponse<List<RoutineResponseDTO>>> getRoutines(Authentication authentication) throws MessagingException, UnsupportedEncodingException {
        User user = (User) authentication.getPrincipal(); // Usuario autenticado desde JWT
        ApiResponse<List<RoutineResponseDTO>> response = routineService.getRoutines(user);

        if (!response.isSuccess()) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.status(201).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> updateRoutine(@PathVariable String id, @Valid @RequestBody RoutineCreateDTO request, Authentication authentication) throws MessagingException, UnsupportedEncodingException {
        User user = (User) authentication.getPrincipal(); // Usuario autenticado a partir del JWT
        ApiResponse<String> response = routineService.updateRoutine(id, request, user);
        if (!response.isSuccess()) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RoutineResponseDTO>> getRoutineById(@PathVariable String id, Authentication authentication) throws MessagingException, UnsupportedEncodingException {
        User user = (User) authentication.getPrincipal(); // Usuario autenticado a partir del JWT
        ApiResponse<RoutineResponseDTO> response = routineService.getRoutineById(id, user);
        if (!response.isSuccess()) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.status(201).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteRoutine(@PathVariable String id, Authentication authentication) throws MessagingException, UnsupportedEncodingException {
        User user = (User) authentication.getPrincipal(); // Usuario autenticado a partir del JWT
        ApiResponse<String> response = routineService.deleteRoutine(id, user);
        if (!response.isSuccess()) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/search")
    public ResponseEntity<List<RoutineAddExerciseDTO>> searchExercises(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Muscle muscle,
            @RequestParam(required = false) Difficulty difficulty
    ) {
        List<RoutineAddExerciseDTO> results = routineService.searchExercises(title, muscle, difficulty);
        return ResponseEntity.ok(results);
    }

    @PostMapping("/{idRoutine}/{idExercise}")
    public ResponseEntity<ApiResponse<String>> addExercise(@PathVariable String idRoutine, @PathVariable String idExercise, Authentication authentication) throws MessagingException, UnsupportedEncodingException {
        User user = (User) authentication.getPrincipal();
        ApiResponse<String> response = routineService.addExercise(idRoutine, idExercise, user);
        if (!response.isSuccess()) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.status(201).body(response);
    }

    @DeleteMapping("/{idRoutine}/{idExercise}")
    public ResponseEntity<ApiResponse<String>> removeExercise(@PathVariable String idRoutine, @PathVariable String idExercise, Authentication authentication) throws MessagingException, UnsupportedEncodingException {
        User user = (User) authentication.getPrincipal();
        ApiResponse<String> response = routineService.removeExercise(idRoutine, idExercise, user);
        if (!response.isSuccess()) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.status(201).body(response);
    }
}
