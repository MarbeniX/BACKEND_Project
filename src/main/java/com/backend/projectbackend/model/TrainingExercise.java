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
    private Long restTime;
    private Integer weight;
    private Integer reps;

    public TrainingExercise() {}

    public TrainingExercise(ObjectId exerciseId, Long timeToComplete, Integer setNumber, Long restTime, Integer weight, Integer reps) {
        this.timeToComplete = timeToComplete;
        this.setNumber = setNumber;
        this.restTime = restTime;
        this.weight = weight;
        this.reps = reps;
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

    @Override
    public String toString() {
        return "TrainingExercise{" +
                "id=" + id +
                ", timeToComplete=" + timeToComplete +
                ", setNumber=" + setNumber +
                ", exerciseId=" + exerciseId +
                ", restTime=" + restTime +
                ", weight=" + weight +
                '}';
    }
}
