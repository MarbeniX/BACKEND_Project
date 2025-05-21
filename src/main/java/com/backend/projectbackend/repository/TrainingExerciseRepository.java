package com.backend.projectbackend.repository;

import com.backend.projectbackend.model.TrainingExercise;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainingExerciseRepository extends MongoRepository<TrainingExercise, ObjectId> {
}
