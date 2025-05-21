package com.backend.projectbackend.service;

import com.backend.projectbackend.dto.training.*;
import com.backend.projectbackend.model.Routine;
import com.backend.projectbackend.model.TrainingExercise;
import com.backend.projectbackend.model.TrainingSession;
import com.backend.projectbackend.model.User;
import com.backend.projectbackend.repository.ExerciseRepository;
import com.backend.projectbackend.repository.RoutineRepository;
import com.backend.projectbackend.repository.TrainingExerciseRepository;
import com.backend.projectbackend.repository.TrainingRepository;
import com.backend.projectbackend.util.responses.ApiResponse;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TrainingService {
    public final TrainingRepository trainingRepository;
    private final RoutineRepository routineRepository;
    private final ExerciseRepository exerciseRepository;
    private final TrainingExerciseRepository trainingExerciseRepository;

    public TrainingService(TrainingRepository trainingRepository, RoutineRepository routineRepository, ExerciseRepository exerciseRepository, TrainingExerciseRepository trainingExerciseRepository) {
        this.trainingRepository = trainingRepository;
        this.routineRepository = routineRepository;
        this.exerciseRepository = exerciseRepository;
        this.trainingExerciseRepository = trainingExerciseRepository;
    }

    public ApiResponse<String> startTraining(StartSessionDataDTO request, User user) {
        try {
            if(request.getRoutineId() != null) {
                Routine routineExists = routineRepository.findById(new ObjectId(request.getRoutineId())).orElse(null);
                if(!routineExists.getUserId().equals(user.getId())) {
                    return new ApiResponse<>(false, "You don't have access to this routine", null);
                }
            }
            TrainingSession trainingSession = new TrainingSession();
            trainingSession.setUserId(user.getId());
            if(request.getRoutineId() == null) {
                trainingSession.setRoutineId(null);
                trainingRepository.save(trainingSession);
                String sessionId = trainingSession.getId().toString();
                return new ApiResponse<>(true, "Session created.", sessionId);
            }
            ObjectId routineObjectId = new ObjectId(request.getRoutineId());
            trainingSession.setRoutineId(routineObjectId);
            trainingRepository.save(trainingSession);
            String sessionId = trainingSession.getId().toString();
            return new ApiResponse<>(true, "Session created.", sessionId);
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse<>(false, "Internal server error: " + e.getMessage(), null);
        }
    }

    public ApiResponse<String> finishTraining(FinishSessionDTO request, String id,  User user) {
        try {
            List<TrainingExercise> exercises = new ArrayList<>();
            List<SessionMarksDTO> marks = request.getMarks();

            ObjectId trainingSessionObjectId = new ObjectId(id);
            TrainingSession trainingSession = trainingRepository.findById(trainingSessionObjectId)
                    .orElseThrow(() -> new RuntimeException("Training session not found"));

            for(SessionMarksDTO mark : marks) {
                ObjectId exerciseObjectId = new ObjectId(mark.getExerciseId());
                TrainingExercise trainingExercise = new TrainingExercise();
                trainingExercise.setExerciseId(exerciseObjectId);
                trainingExercise.setTimeToComplete(mark.getTimeToComplete());
                trainingExercise.setSetNumber(mark.getSetNumber());
                trainingExercise.setReps(mark.getReps());
                trainingExercise.setTrainingSession(trainingSessionObjectId);
                trainingExercise.setUserId(user.getId());
                trainingExerciseRepository.save(trainingExercise);
                exercises.add(trainingExercise);
            }
            trainingSession.setExercises(exercises);
            trainingRepository.save(trainingSession);
            return new ApiResponse<>(true, "Session finished.", null);
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse<>(false, "Internal server error: " + e.getMessage(), null);
        }
    }

    public ApiResponse<GetTrainingSessionById> getTrainingSessionById(String id, User user) {
        try {
            if(trainingRepository.findByUserId(user.getId()) == null) {
                return new ApiResponse<>(false, "User has no sessions", null);
            }
            TrainingSession trainingSessionExists = trainingRepository.findById(new ObjectId(id)).orElseThrow(() -> new RuntimeException("Training session not found"));
            if(!trainingSessionExists.getUserId().equals(user.getId())) {
                return new ApiResponse<>(false, "You don't have access to this session", null);
            }

            List<GetTrainingSessionMarksByIdDTO> marks = trainingSessionExists.getExercises()
                    .stream()
                    .map(exercise -> {
                        GetTrainingSessionMarksByIdDTO markDTO = new GetTrainingSessionMarksByIdDTO();
                        markDTO.setMarkId(exercise.getId().toString());
                        markDTO.setExerciseId(exercise.getExerciseId().toString());
                        markDTO.setTimeToComplete(exercise.getTimeToComplete());
                        markDTO.setSetNumber(exercise.getSetNumber());
                        markDTO.setReps(exercise.getReps());
                        return markDTO;
                    }).collect(Collectors.toList());
            GetTrainingSessionById sessionDTO = new GetTrainingSessionById();
            sessionDTO.setMarks(marks);
            sessionDTO.setTrainingDate(trainingSessionExists.getTrainingDate());
            if(trainingSessionExists.getRoutineId() == null) {
                sessionDTO.setRoutineId(null);
                return new ApiResponse<>(true, "Session found.", sessionDTO);
            }
            sessionDTO.setRoutineId(trainingSessionExists.getRoutineId().toString());
            return new ApiResponse<>(true, "Session found.", null);
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse<>(false, "Internal server error: " + e.getMessage(), null);
        }
    }

    public ApiResponse<List<GetTrainingSessionById>> getAllTrainingSession(User user) {
        try {
            if(routineRepository.findByUserId(user.getId()) == null) {
                return new ApiResponse<>(true, "User has no routines", null);
            }
            List<GetTrainingSessionById> trainingSessionsDTO = new ArrayList<>();
            List<TrainingSession> trainingSessions = trainingRepository.findAllByUserId(user.getId());

            for(TrainingSession trainingSession : trainingSessions) {
                List<GetTrainingSessionMarksByIdDTO> marks = trainingSession.getExercises()
                        .stream()
                        .map(exercise -> {
                            GetTrainingSessionMarksByIdDTO markDTO = new GetTrainingSessionMarksByIdDTO();
                            markDTO.setMarkId(exercise.getId().toString());
                            markDTO.setExerciseId(exercise.getExerciseId().toString());
                            markDTO.setTimeToComplete(exercise.getTimeToComplete());
                            markDTO.setSetNumber(exercise.getSetNumber());
                            markDTO.setReps(exercise.getReps());
                            return markDTO;
                        }).collect(Collectors.toList());
                GetTrainingSessionById finishSessionDTO = new GetTrainingSessionById();
                finishSessionDTO.setMarks(marks);
                finishSessionDTO.setTrainingDate(trainingSession.getTrainingDate());
                if(trainingSession.getRoutineId() == null) {
                    finishSessionDTO.setRoutineId(null);
                    trainingSessionsDTO.add(finishSessionDTO);
                    return new ApiResponse<>(true, "All Training sessions", trainingSessionsDTO);
                }
                finishSessionDTO.setRoutineId(trainingSession.getRoutineId().toString());
                trainingSessionsDTO.add(finishSessionDTO);
            }
            return new ApiResponse<>(true, "All Training sessions", trainingSessionsDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse<>(false, "Internal server error: " + e.getMessage(), null);
        }
    }
}
