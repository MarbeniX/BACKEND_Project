package com.backend.projectbackend.service;

import com.backend.projectbackend.dto.routine.RoutineAddExerciseDTO;
import com.backend.projectbackend.dto.routine.RoutineResponseDTO;
import com.backend.projectbackend.model.Exercise;
import com.backend.projectbackend.model.Exercise.Muscle;
import com.backend.projectbackend.model.Exercise.Difficulty;
import com.backend.projectbackend.dto.routine.RoutineCreateDTO;
import com.backend.projectbackend.model.Routine;
import com.backend.projectbackend.model.User;
import com.backend.projectbackend.repository.AuthRepository;
import com.backend.projectbackend.repository.ExerciseRepository;
import com.backend.projectbackend.repository.RoutineRepository;
import com.backend.projectbackend.util.responses.ApiResponse;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

//LOGIN
@Service
public class RoutineService {
    public final RoutineRepository routineRepository;
    private final AuthRepository authRepository;
    private final ExerciseRepository exerciseRepository;

    public RoutineService(RoutineRepository routineRepository, AuthRepository authRepository, ExerciseRepository exerciseRepository) {
        this.routineRepository = routineRepository;
        this.authRepository = authRepository;
        this.exerciseRepository = exerciseRepository;
    }

    public ApiResponse<String> createRoutine(RoutineCreateDTO request, User user) {
        try {
            //Routine creation and assignation
            Routine routine = new Routine();
            routine.setName(request.getName());
            routine.setDescription(request.getDescription());
            routine.setUserId(user.getId());
            routine.setCategory(request.getCategory());
            Routine savedRoutine = routineRepository.save(routine);

            List<Routine> routines = user.getRoutines();
            if (routines == null) {
                routines = new ArrayList<>();
            }

            routines.add(savedRoutine);
            user.setRoutines(routines);

            authRepository.save(user);

            return new ApiResponse<>(true, "Routine created.", null);
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse<>(false, "Internal server error: " + e.getMessage(), null);
        }
    }

    public ApiResponse<List<RoutineResponseDTO>> getRoutines(User user) {
        try {
            List<Routine> routines = user.getRoutines(); // Esto solo funciona si tienes correctamente anotado @DBRef
            if (routines == null || routines.isEmpty()) {
                return new ApiResponse<>(false, "Still no routines", null);
            }
            List<RoutineResponseDTO> routineDTOs = routines.stream()
                    .map(RoutineResponseDTO::new)
                    .collect(Collectors.toList());

            return new ApiResponse<>(true, "Routines found", routineDTOs);
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse<>(false, "Internal server error: " + e.getMessage(), null);
        }
    }

    public ApiResponse<String> updateRoutine(String id, RoutineCreateDTO request, User user) {
        try {
            Optional<Routine> optionalRoutine = routineRepository.findById(new ObjectId(id));
            if (optionalRoutine.isEmpty()) {
                return new ApiResponse<>(false, "Routine not found", null);
            }

            Routine routine = optionalRoutine.get();
            if (!routine.getUserId().equals(user.getId())) {
                return new ApiResponse<>(false, "This routine doesn't own you", null);
            }

            routine.setName(request.getName());
            routine.setDescription(request.getDescription());
            routine.setCategory(request.getCategory());

            routineRepository.save(routine);

            return new ApiResponse<>(true, "Routine updated.", null);
        } catch (Exception e) {
            e.printStackTrace(); // Puedes registrar con un logger en vez de imprimir
            return new ApiResponse<>(false, "Internal server error: " + e.getMessage(), null);
        }
    }

    public ApiResponse<RoutineResponseDTO> getRoutineById(String id, User user ) {
        try {
            Optional<Routine> optionalRoutine = routineRepository.findById(new ObjectId(id));
            if (optionalRoutine.isEmpty()) {
                return new ApiResponse<>(false, "Routine not found", null);
            }
            Routine routine = optionalRoutine.get();
            if (!routine.getUserId().equals(user.getId())) {
                return new ApiResponse<>(false, "This routine doesn't own you", null);
            }
            RoutineResponseDTO dto = new RoutineResponseDTO(routine);
            return new ApiResponse<>(true, "Routine found.", dto);
        } catch (Exception e) {
            e.printStackTrace(); // Puedes registrar con un logger en vez de imprimir
            return new ApiResponse<>(false, "Internal server error: " + e.getMessage(), null);
        }
    }

    public ApiResponse<String> deleteRoutine(String id, User user ) {
        try {
            Optional<Routine> optionalRoutine = routineRepository.findById(new ObjectId(id));
            if (optionalRoutine.isEmpty()) {
                return new ApiResponse<>(false, "Routine not found", null);
            }
            Routine routine = optionalRoutine.get();
            if (!routine.getUserId().equals(user.getId())) {
                return new ApiResponse<>(false, "This routine doesn't own you", null);
            }

            routineRepository.delete(routine);
            user.getRoutines().removeIf(r -> r.getId().equals(routine.getId()));
            authRepository.save(user);

            return new ApiResponse<>(true, "Routine deleted.", null);
        } catch (Exception e) {
            e.printStackTrace(); // Puedes registrar con un logger en vez de imprimir
            return new ApiResponse<>(false, "Internal server error: " + e.getMessage(), null);
        }
    }

    public List<RoutineAddExerciseDTO> searchExercises(String title, Muscle muscle, Difficulty difficulty) {
        if (title != null && muscle != null && difficulty != null) {
            List<Exercise> exercises = exerciseRepository.findByTitleContainingIgnoreCaseAndMuscleAndDifficulty(title, muscle, difficulty);
            List<RoutineAddExerciseDTO> routineDTOs = exercises.stream()
                    .map(RoutineAddExerciseDTO::new)
                    .collect(Collectors.toList());
            return new ArrayList<>(routineDTOs);
        } else if (title != null && muscle != null) {
            List<Exercise> exercises = exerciseRepository.findByTitleContainingIgnoreCaseAndMuscle(title, muscle);
            List<RoutineAddExerciseDTO> routineDTOs = exercises.stream()
                    .map(RoutineAddExerciseDTO::new)
                    .collect(Collectors.toList());
            return new ArrayList<>(routineDTOs);
        } else if (title != null && difficulty != null) {
            List<Exercise> exercises = exerciseRepository.findByTitleContainingIgnoreCaseAndDifficulty(title, difficulty);
            List<RoutineAddExerciseDTO> routineDTOs = exercises.stream()
                    .map(RoutineAddExerciseDTO::new)
                    .collect(Collectors.toList());
            return new ArrayList<>(routineDTOs);
        } else if (title != null) {
            List<Exercise> exercises = exerciseRepository.findByTitleContainingIgnoreCase(title);
            List<RoutineAddExerciseDTO> routineDTOs = exercises.stream()
                    .map(RoutineAddExerciseDTO::new)
                    .collect(Collectors.toList());
            return new ArrayList<>(routineDTOs);
        } else if (muscle != null) {
            List<Exercise> exercises = exerciseRepository.findByMuscle(muscle);
            List<RoutineAddExerciseDTO> routineDTOs = exercises.stream()
                    .map(RoutineAddExerciseDTO::new)
                    .collect(Collectors.toList());
            return new ArrayList<>(routineDTOs);
        } else if (difficulty != null) {
            List<Exercise> exercises = exerciseRepository.findByDifficulty(difficulty);
            List<RoutineAddExerciseDTO> routineDTOs = exercises.stream()
                    .map(RoutineAddExerciseDTO::new)
                    .collect(Collectors.toList());
            return new ArrayList<>(routineDTOs);
        } else {
            List<Exercise> exercises = exerciseRepository.findAll();
            List<RoutineAddExerciseDTO> routineDTOs = exercises.stream()
                    .map(RoutineAddExerciseDTO::new)
                    .collect(Collectors.toList());
            return new ArrayList<>(routineDTOs);
        }
    }

    public ApiResponse<String> addExercise(String idRoutine, String idExercise,  User user ) {
        try {
            Optional<Routine> optionalRoutine = routineRepository.findById(new ObjectId(idRoutine));
            if (optionalRoutine.isEmpty()) {
                return new ApiResponse<>(false, "Routine not found", null);
            }
            Routine routine = optionalRoutine.get();
            if (!routine.getUserId().equals(user.getId())) {
                return new ApiResponse<>(false, "This routine doesn't own you", null);
            }

            Exercise exercise = exerciseRepository.findById(new ObjectId(idExercise)).get();
            List<Exercise> exercises = routine.getExercises();
            if (exercises == null) {
                exercises = new ArrayList<>();
            }
            exercises.add(exercise);
            routine.setExercises(exercises);
            routineRepository.save(routine);
            return new ApiResponse<>(true, "Exercises added.", null);
        } catch (Exception e) {
            e.printStackTrace(); // Puedes registrar con un logger en vez de imprimir
            return new ApiResponse<>(false, "Internal server error: " + e.getMessage(), null);
        }
    }

    public ApiResponse<String> removeExercise(String idRoutine, String idExercise,  User user ) {
        try {
            Optional<Routine> optionalRoutine = routineRepository.findById(new ObjectId(idRoutine));
            if (optionalRoutine.isEmpty()) {
                return new ApiResponse<>(false, "Routine not found", null);
            }
            Routine routine = optionalRoutine.get();
            if (!routine.getUserId().equals(user.getId())) {
                return new ApiResponse<>(false, "This routine doesn't own you", null);
            }

            boolean removed = routine.getExercises().removeIf(exercise ->
                    exercise.getId().toHexString().equals(idExercise)
            );
            routineRepository.save(routine);
            return new ApiResponse<>(true, "Exercise removed from routine.", null);
        } catch (Exception e) {
            e.printStackTrace(); // Puedes registrar con un logger en vez de imprimir
            return new ApiResponse<>(false, "Internal server error: " + e.getMessage(), null);
        }
    }
}
