package com.backend.projectbackend.service;

import com.backend.projectbackend.dto.training.*;
import com.backend.projectbackend.model.*;
import com.backend.projectbackend.repository.*;
import com.backend.projectbackend.util.responses.ApiResponse;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import org.springframework.web.server.ResponseStatusException;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
            if(trainingRepository.findByUserId(user.getId()) == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User has no sessions");
            }
            List<GetTrainingSessionById> trainingSessionsDTO = new ArrayList<>();
            List<TrainingSession> trainingSessions = user.getTrainings();

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
                }else{
                    finishSessionDTO.setRoutineId(trainingSession.getRoutineId().toString());
                    trainingSessionsDTO.add(finishSessionDTO);
                }
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

    public byte[] generateSessionPDF(String id, User user) {
        try {
            if(trainingRepository.findByUserId(user.getId()) == null) {
                return null;
            }
            TrainingSession sessionExists = trainingRepository.findById(new ObjectId(id)).orElse(null);
            if(!sessionExists.getUserId().equals(user.getId())) {
                return null;
            }

            Routine sessionRoutine;

            if(sessionExists.getRoutineId() == null) {
                sessionRoutine = new Routine();
            }else{
                sessionRoutine = routineRepository.findById(sessionExists.getRoutineId()).orElse(null);
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PdfWriter writer = new PdfWriter(out);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // 1. HEADER
            String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(sessionExists.getTrainingDate());

            Paragraph header = new Paragraph()
                    .add("Usuario: " + user.getUsername() + "\n")
                    .add("Rutina: " + (sessionRoutine.getName() != null ? sessionRoutine.getName() : "Ninguna") + "\n")
                    .add("Fecha: " + formattedDate)
                    .setTextAlignment(TextAlignment.LEFT)
                    .setMarginBottom(20);
            document.add(header);

            // 2. TABLA DE MARCAS
            Table table = new Table(UnitValue.createPercentArray(new float[]{1, 2, 3, 2, 2}))
                    .useAllAvailableWidth();

            // Encabezado de la tabla
            table.addHeaderCell(new Cell().add(new Paragraph("Marca")).setBold().setBorderBottom(new SolidBorder(1)));
            table.addHeaderCell(new Cell().add(new Paragraph("Tiempo")).setBold().setBorderBottom(new SolidBorder(1)));
            table.addHeaderCell(new Cell().add(new Paragraph("Ejercicio")).setBold().setBorderBottom(new SolidBorder(1)));
            table.addHeaderCell(new Cell().add(new Paragraph("Set")).setBold().setBorderBottom(new SolidBorder(1)));
            table.addHeaderCell(new Cell().add(new Paragraph("Reps")).setBold().setBorderBottom(new SolidBorder(1)));

            // Cuerpo de la tabla
            int markNumber = 1;
            List<TrainingExercise> marks = sessionExists.getExercises();
            for (TrainingExercise mark : marks) {
                Exercise exercise = exerciseRepository.findById(new ObjectId(mark.getExerciseId().toString())).orElse(null);
                table.addCell(new Cell().add(new Paragraph(String.valueOf(markNumber++))));
                table.addCell(new Cell().add(new Paragraph(mark.getTimeToComplete() != null ? mark.getTimeToComplete().toString() : "-")));
                table.addCell(new Cell().add(new Paragraph(exercise.getTitle() != null ? exercise.getTitle() : "N/A")));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(mark.getSetNumber()))));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(mark.getReps()))));
            }

            document.add(table);
            document.close();

            return out.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
