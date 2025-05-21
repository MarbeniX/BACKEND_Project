package com.backend.projectbackend.repository;

import com.backend.projectbackend.model.TrainingSession;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface TrainingRepository extends MongoRepository<TrainingSession, ObjectId> {
    List<TrainingSession> findAllByUserId(ObjectId userId);
    TrainingSession findByUserId(ObjectId userId);
}