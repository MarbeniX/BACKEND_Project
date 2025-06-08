package com.backend.projectbackend.controllers;

import com.backend.projectbackend.dto.routine.RoutineResponseDTO;
import com.backend.projectbackend.dto.routine.RoutineSearchRoutinesResponseDTO;
import com.backend.projectbackend.dto.training.FinishSessionDTO;
import com.backend.projectbackend.dto.training.GetTrainingSessionById;
import com.backend.projectbackend.dto.training.StartSessionDataDTO;
import com.backend.projectbackend.model.Routine;
import com.backend.projectbackend.model.User;
import com.backend.projectbackend.service.RoutineService;
import com.backend.projectbackend.service.TrainingService;
import com.backend.projectbackend.util.responses.ApiResponse;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.List;

@RestController
@RequestMapping("/api/training")
public class TrainingController {
    private final TrainingService trainingService;
    private final RoutineService routineService;

    public TrainingController(TrainingService trainingService, RoutineService routineService) {
        this.trainingService = trainingService;
        this.routineService = routineService;
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

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteTrainingSession(@PathVariable String id, Authentication authentication) throws MessagingException, UnsupportedEncodingException {
        User user = (User) authentication.getPrincipal(); // Usuario autenticado a partir del JWT
        ApiResponse<String> response = trainingService.deleteTrainingSession(id, user);
        if (!response.isSuccess()) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/searchSessions")
    public ResponseEntity<ApiResponse<List<GetTrainingSessionById>>> searchSessions(
            @RequestParam(required = false) String filter,
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal(); // Usuario autenticado a partir del JWT
        ApiResponse<List<GetTrainingSessionById>> results = trainingService.searchSessions(filter, user);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/searchRoutines")
    public ResponseEntity<List<RoutineSearchRoutinesResponseDTO>> searchRoutines(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Routine.Category category,
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal(); // Usuario autenticado a partir del JWT
        List<RoutineSearchRoutinesResponseDTO> results = routineService.searchRoutines(name, category, user);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/report/{id}")
    public ResponseEntity<byte[]> generateSessionPDF(@PathVariable String id, Authentication authentication) {
        User user = (User) authentication.getPrincipal(); // Usuario autenticado a partir del JWT
        byte[] pdf = trainingService.generateSessionPDF(id, user);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition
                .inline() // o .attachment() si quieres forzar descarga
                .filename("routine-report.pdf")
                .build());

        return ResponseEntity.ok().headers(headers).body(pdf);
    }
}
