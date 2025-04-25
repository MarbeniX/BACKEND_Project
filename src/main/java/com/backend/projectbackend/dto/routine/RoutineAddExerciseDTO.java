package com.backend.projectbackend.dto.routine;

import com.backend.projectbackend.model.Exercise;

public class RoutineAddExerciseDTO {
    private String id;
    private String title;
    private String description;
    private Exercise.Muscle muscle;
    private Exercise.Difficulty difficulty;

    public RoutineAddExerciseDTO() {}

    public RoutineAddExerciseDTO(Exercise exercise) {
        this.id = exercise.getId().toHexString();
        this.title = exercise.getTitle();
        this.description = exercise.getDescription();
        this.muscle = exercise.getMuscle();
        this.difficulty = exercise.getDifficulty();
    }

    //Setters and getters

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Exercise.Muscle getMuscle() { return muscle; }
    public void setMuscle(Exercise.Muscle muscle) { this.muscle = muscle; }

    public Exercise.Difficulty getDifficulty() { return difficulty; }
    public void setDifficulty(Exercise.Difficulty difficulty) { this.difficulty = difficulty; }
}
