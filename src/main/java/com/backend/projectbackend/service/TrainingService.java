package com.backend.projectbackend.service;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/training")
public class TrainingService {
    private final TrainingService trainingService;

    public TrainingService(TrainingService trainingService) {
        this.trainingService = trainingService;
    }

    @PostMapping("/createTraining")
}
