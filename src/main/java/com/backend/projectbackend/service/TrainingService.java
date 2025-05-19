package com.backend.projectbackend.service;

import com.backend.projectbackend.dto.training.StartSessionDataDTO;
import com.backend.projectbackend.model.TrainingSession;
import com.backend.projectbackend.model.User;
import com.backend.projectbackend.repository.RoutineRepository;
import com.backend.projectbackend.repository.TrainingRepository;
import com.backend.projectbackend.util.responses.ApiResponse;
import org.springframework.stereotype.Service;

@Service
public class TrainingService {
    public final TrainingRepository trainingRepository;
    private final RoutineRepository routineRepository;

    public TrainingService(TrainingRepository trainingRepository, RoutineRepository routineRepository) {
        this.trainingRepository = trainingRepository;
        this.routineRepository = routineRepository;
    }

    public ApiResponse<String> startTraining(StartSessionDataDTO request, User user) {
        try {
            if(routineRepository.findByUserId(user.getId()) == null) {
                return new ApiResponse<>(true, "User has no routines", null);
            }
            TrainingSession trainingSession = new TrainingSession();
            trainingSession.setUserId(user.getId());
            trainingSession.setRoutineId(request.getRoutineId());
            trainingRepository.save(trainingSession);
            return new ApiResponse<>(true, "Session created.", null);
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse<>(false, "Internal server error: " + e.getMessage(), null);
        }
    }
}
