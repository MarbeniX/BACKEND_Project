package com.backend.projectbackend.repository;

import com.backend.projectbackend.model.Exercise.Muscle;
import com.backend.projectbackend.model.Exercise.Difficulty;
import com.backend.projectbackend.model.Exercise;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExerciseRepository extends MongoRepository<Exercise, ObjectId> {
    List<Exercise> findByTitleContainingIgnoreCase(String title);
    List<Exercise> findByMuscle(Muscle muscle);
    List<Exercise> findByDifficulty(Difficulty difficulty);

    List<Exercise> findByTitleContainingIgnoreCaseAndMuscleAndDifficulty(
            String title, Muscle muscle, Difficulty difficulty);

    List<Exercise> findByTitleContainingIgnoreCaseAndMuscle(String title, Muscle muscle);
    List<Exercise> findByTitleContainingIgnoreCaseAndDifficulty(String title, Difficulty difficulty);
}