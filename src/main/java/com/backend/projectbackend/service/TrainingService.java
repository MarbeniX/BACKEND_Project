package com.backend.projectbackend.service;

import com.backend.projectbackend.dto.training.*;
import com.backend.projectbackend.model.Routine;
import com.backend.projectbackend.model.TrainingExercise;
import com.backend.projectbackend.model.TrainingSession;
import com.backend.projectbackend.model.User;
import com.backend.projectbackend.repository.*;
import com.backend.projectbackend.util.responses.ApiResponse;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.io.Serial;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TrainingService {
    public final TrainingRepository trainingRepository;
    private final RoutineRepository routineRepository;
    private final ExerciseRepository exerciseRepository;
    private final TrainingExerciseRepository trainingExerciseRepository;
    private final AuthRepository authRepository;

    public TrainingService(TrainingRepository trainingRepository, RoutineRepository routineRepository, ExerciseRepository exerciseRepository, TrainingExerciseRepository trainingExerciseRepository, AuthRepository authRepository) {
        this.trainingRepository = trainingRepository;
        this.routineRepository = routineRepository;
        this.exerciseRepository = exerciseRepository;
        this.trainingExerciseRepository = trainingExerciseRepository;
        this.authRepository = authRepository;
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
            TrainingSession trainingSessionExists = trainingRepository.findById(new ObjectId(id)).orElse(null);
            if(!trainingSessionExists.getUserId().equals(user.getId())) {
                return new ApiResponse<>(false, "You don't have access to this training session", null);
            }

            List<TrainingExercise> exercises = new ArrayList<>();
            List<SessionMarksDTO> marks = request.getMarks();

            for(SessionMarksDTO mark : marks) {
                ObjectId exerciseObjectId = new ObjectId(mark.getExerciseId());
                TrainingExercise trainingExercise = new TrainingExercise();
                trainingExercise.setExerciseId(exerciseObjectId);
                trainingExercise.setTimeToComplete(mark.getTimeToComplete());
                trainingExercise.setSetNumber(mark.getSetNumber());
                trainingExercise.setReps(mark.getReps());
                trainingExercise.setTrainingSession(new ObjectId(id));
                trainingExercise.setUserId(user.getId());
                trainingExerciseRepository.save(trainingExercise);
                exercises.add(trainingExercise);
            }
            trainingSessionExists.setExercises(exercises);
            trainingRepository.save(trainingSessionExists);

            User userDb = authRepository.findById(user.getId()).get();
            List<TrainingSession> userSessions = userDb.getTrainings();
            if(userSessions == null){
                userSessions = new ArrayList<>();
            }

            userSessions.add(trainingSessionExists);
            userDb.setTrainings(userSessions);
            authRepository.save(userDb);
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
            sessionDTO.setSessionId(trainingSessionExists.getId().toString());
            if(trainingSessionExists.getRoutineId() == null) {
                sessionDTO.setRoutineId(null);
                return new ApiResponse<>(true, "Session found.", sessionDTO);
            }
            sessionDTO.setRoutineId(trainingSessionExists.getRoutineId().toString());
            return new ApiResponse<>(true, "Session found.", sessionDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse<>(false, "Internal server error: " + e.getMessage(), null);
        }
    }

    public ApiResponse<List<GetTrainingSessionById>> getAllTrainingSession(User user) {
        try {
            if(routineRepository.findByUserId(user.getId()) == null) {
                return new ApiResponse<>(true, "User has no sessions", null);
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
                finishSessionDTO.setSessionId(trainingSession.getId().toString());
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

    public ApiResponse<String> deleteTrainingSession(String id, User user) {
        try {
            if(trainingRepository.findByUserId(user.getId()) == null) {
                return new ApiResponse<>(false, "User has no sessions", null);
            }
            TrainingSession trainingSessionExists = trainingRepository.findById(new ObjectId(id)).orElse(null);
            if(!trainingSessionExists.getUserId().equals(user.getId())) {
                return new ApiResponse<>(false, "You don't have access to this session", null);
            }

            List<TrainingExercise> trainingExercises = trainingSessionExists.getExercises();
            for(TrainingExercise trainingExercise : trainingExercises) {
                trainingExerciseRepository.delete(trainingExercise);
            }
            user.getTrainings().removeIf(session -> session.getId().equals(id));
            trainingRepository.deleteById(new ObjectId(id));
            System.out.println(trainingRepository.findById(new ObjectId(id)).orElse(null));
            authRepository.save(user);

            return new ApiResponse<>(true, "Session deleted", null);
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse<>(false, "Internal server error: " + e.getMessage(), null);
        }
    }

    public ApiResponse<List<GetTrainingSessionById>> searchSessions(String filter, User user) {
        try {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime startDate;

            switch (filter.toLowerCase()) {
                case "1week":
                    startDate = now.minusWeeks(1);
                    break;
                case "2weeks":
                    startDate = now.minusWeeks(2);
                    break;
                case "3weeks":
                    startDate = now.minusWeeks(3);
                    break;
                case "1month":
                    startDate = now.minusMonths(1);
                    break;
                case "3months":
                    startDate = now.minusMonths(3);
                    break;
                case "all":
                default:
                    startDate = null;
            }
            List<GetTrainingSessionById> trainingSessionsDTO = new ArrayList<>();

            List<TrainingSession> trainingSessions;
            if(startDate == null){
                trainingSessions = trainingRepository.findAllByUserId(user.getId());
            }else{
                Date start = Date.from(startDate.atZone(ZoneId.systemDefault()).toInstant());
                trainingSessions = trainingRepository.findByUserIdAndTrainingDateAfterOrderByTrainingDateDesc(user.getId(), start);
            }

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
                finishSessionDTO.setSessionId(trainingSession.getId().toString());
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
