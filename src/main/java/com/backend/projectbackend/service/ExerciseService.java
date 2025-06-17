package com.backend.projectbackend.service;

import com.backend.projectbackend.dto.exercise.ExerciseCreateDTO;
import com.backend.projectbackend.dto.exercise.ExerciseGetByIdDTO;
import com.backend.projectbackend.dto.routine.RoutineAddExerciseDTO;
import com.backend.projectbackend.dto.user.CloudinaryImageDTO;
import com.backend.projectbackend.model.Exercise;
import com.backend.projectbackend.model.Routine;
import com.backend.projectbackend.model.User;
import com.backend.projectbackend.repository.ExerciseRepository;
import com.backend.projectbackend.repository.RoutineRepository;
import com.backend.projectbackend.util.responses.ApiResponse;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExerciseService {
    private final RoutineRepository routineRepository;
    private final CloudinaryService cloudinaryService;
    private ExerciseRepository exerciseRepository;

    public ExerciseService(ExerciseRepository exerciseRepository, RoutineRepository routineRepository, CloudinaryService cloudinaryService) {
        this.exerciseRepository = exerciseRepository;
        this.routineRepository = routineRepository;
        this.cloudinaryService = cloudinaryService;
    }

    public ApiResponse<String> addExercise(ExerciseCreateDTO request, User user) {
        try {
            if(user.getAdmin() == false){
                return new ApiResponse<>(false, "You do not have access to this action.", null);
            }
            Exercise exercise = new Exercise();
            exercise.setDescription(request.getDescription());
            exercise.setTitle(request.getTitle());
            exercise.setMuscle(request.getMuscle());
            exercise.setDifficulty(request.getDifficulty());
            exercise.setImageURL(request.getImageURL());
            exercise.setPublicID(request.getPublicID());
            exerciseRepository.save(exercise);
            return new ApiResponse<>(true, "Exercise created.", null);
        } catch (Exception e) {
            e.printStackTrace(); // Puedes registrar con un logger en vez de imprimir
            return new ApiResponse<>(false, "Internal server error: " + e.getMessage(), null);
        }
    }

    public ApiResponse<String> updateExercise(String id, ExerciseCreateDTO request, User user) {
        try {
            if(!user.getAdmin()){
                return new ApiResponse<>(false, "You do not have access to this action.", null);
            }

            Exercise exerciseExists = exerciseRepository.findById(new ObjectId(id)).get();
            if(exerciseExists == null){
                return new ApiResponse<>(false, "Exercise doesn't exist.", null);
            }

            MultipartFile image = request.getImage();
            if(image != null && !image.isEmpty()) {
                try {
                    CloudinaryImageDTO dataImage = CloudinaryService.uploadImageExercise(image);
                    request.setImageURL(dataImage.getUrl());
                    request.setPublicID(dataImage.getPublicID());
                    if(exerciseExists.getPublicID() != null){
                        cloudinaryService.deleteImage(exerciseExists.getPublicID());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else{
                request.setImageURL(exerciseExists.getImageURL());
                request.setPublicID(exerciseExists.getPublicID());
            }
            exerciseExists.setDescription(request.getDescription());
            exerciseExists.setTitle(request.getTitle());
            exerciseExists.setMuscle(request.getMuscle());
            exerciseExists.setDifficulty(request.getDifficulty());
            exerciseExists.setImageURL(request.getImageURL());
            exerciseExists.setPublicID(request.getPublicID());
            exerciseRepository.save(exerciseExists);
            return new ApiResponse<>(true, "Exercise updated.", null);
        } catch (Exception e) {
            e.printStackTrace(); // Puedes registrar con un logger en vez de imprimir
            return new ApiResponse<>(false, "Internal server error: " + e.getMessage(), null);
        }
    }

    public ApiResponse<List<RoutineAddExerciseDTO>> getAllExercises( User user ) {
        try {
            if(user.getAdmin() == false){
                return new ApiResponse<>(false, "You do not have access to this action.", null);
            }

            List<Exercise> exercises = exerciseRepository.findAll(); // Esto solo funciona si tienes correctamente anotado @DBRef
            if (exercises == null || exercises.isEmpty()) {
                List<Exercise> newExercises = new ArrayList<>();
                exerciseRepository.saveAll(newExercises);
                return new ApiResponse<>(false, "Still no exercises", null);
            }

            List<RoutineAddExerciseDTO> exerciseDTOS = exercises.stream()
                    .map(RoutineAddExerciseDTO::new)
                    .collect(Collectors.toList());

            return new ApiResponse<>(true, "Exercises.", exerciseDTOS);
        } catch (Exception e) {
            e.printStackTrace(); // Puedes registrar con un logger en vez de imprimir
            return new ApiResponse<>(false, "Internal server error: " + e.getMessage(), null);
        }
    }

    public ApiResponse<String> deleteExercise(String id, User user) {
        try {
            if(user.getAdmin() == false){
                return new ApiResponse<>(false, "You do not have access to this action.", null);
            }
            Exercise exerciseExists = exerciseRepository.findById(new ObjectId(id)).get();
            if(exerciseExists == null){
                return new ApiResponse<>(false, "Exercise doesn't exist.", null);
            }

            if(exerciseExists.getImageURL() != null){
                cloudinaryService.deleteImage(exerciseExists.getPublicID());
            }

            List<Routine> allRoutines = routineRepository.findAll();
            for (Routine routine : allRoutines) {
                boolean modified = routine.getExercises().removeIf(e ->
                        e.getId().equals(exerciseExists.getId())
                );
                if (modified) {
                    routineRepository.save(routine);
                }
            }
            exerciseRepository.delete(exerciseExists);

            return new ApiResponse<>(true, "Exercise deleted.", null);
        } catch (Exception e) {
            e.printStackTrace(); // Puedes registrar con un logger en vez de imprimir
            return new ApiResponse<>(false, "Internal server error: " + e.getMessage(), null);
        }
    }

    public ApiResponse<ExerciseGetByIdDTO> getExerciseById(String id, User user ) {
        try {
            if(user.getAdmin() == false){
                return new ApiResponse<>(false, "You do not have access to this action.", null);
            }

            Exercise exerciseExists = exerciseRepository.findById(new ObjectId(id)).get(); // Esto solo funciona si tienes correctamente anotado @DBRef
            if (exerciseExists == null) {
                return new ApiResponse<>(false, "Still no exercises", null);
            }

            ExerciseGetByIdDTO exerciseGetByIdDTO = new ExerciseGetByIdDTO(exerciseExists);

            return new ApiResponse<>(true, "Exercise.", exerciseGetByIdDTO);
        } catch (Exception e) {
            e.printStackTrace(); // Puedes registrar con un logger en vez de imprimir
            return new ApiResponse<>(false, "Internal server error: " + e.getMessage(), null);
        }
    }
}
