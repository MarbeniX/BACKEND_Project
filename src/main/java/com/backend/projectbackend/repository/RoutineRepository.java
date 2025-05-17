package com.backend.projectbackend.repository;

import com.backend.projectbackend.model.Routine;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoutineRepository extends MongoRepository<Routine, ObjectId> {
    List<Routine> findByUserIdAndNameContainingIgnoreCaseAndCategory(ObjectId userId, String name, Routine.Category category);
    List<Routine> findByUserIdAndNameContainingIgnoreCase(ObjectId userId, String name);
    List<Routine> findByUserIdAndCategory(ObjectId userId, Routine.Category category);
    List<Routine> findByUserId(ObjectId userId);
}
