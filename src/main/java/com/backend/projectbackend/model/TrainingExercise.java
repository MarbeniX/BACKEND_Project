package com.backend.projectbackend.model;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document(collection = "trainingExercises")
public class TrainingExercise {
    @MongoId
    private ObjectId id;

    @DBRef
    private Exercise exerciseId;

    private Long timeToComplete;
    private Integer setNumber;
    private Integer weight;
    private Integer reps;
    private ObjectId trainingSessionId;

    public TrainingExercise() {}

    public TrainingExercise(ObjectId exerciseId, Long timeToComplete, Integer setNumber, Integer weight, Integer reps, ObjectId trainingSessionId) {
        this.timeToComplete = timeToComplete;
        this.setNumber = setNumber;
        this.weight = weight;
        this.reps = reps;
        this.trainingSessionId = trainingSessionId;
    }

    public ObjectId getId() { return id; }
    public void setId(ObjectId id) { this.id = id; }

    public Exercise getExerciseId() { return exerciseId; }
    public void setExerciseId(Exercise exerciseId) { this.exerciseId = exerciseId; }

    public Long getTimeToComplete() { return timeToComplete; }
    public void setTimeToComplete(Long timeToComplete) { this.timeToComplete = timeToComplete; }

    public Integer getSetNumber() { return setNumber; }
    public void setSetNumber(Integer setNumber) { this.setNumber = setNumber; }

    public Integer getWeight() { return weight; }
    public void setWeight(Integer weight) { this.weight = weight; }

    public Integer getReps() { return reps; }
    public void setReps(Integer reps) { this.reps = reps; }

    public ObjectId getTrainingSession() { return trainingSessionId; }
    public void setTrainingSession(ObjectId trainingSession) { this.trainingSessionId = trainingSession; }

}
