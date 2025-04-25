package com.backend.projectbackend.dto.exercise;

import com.backend.projectbackend.model.Exercise.Muscle;
import com.backend.projectbackend.model.Exercise.Difficulty;

public class ExerciseCreateDTO {
    private String description;
    private String title;
    private Muscle muscle;
    private Difficulty difficulty;

    public ExerciseCreateDTO() {}

    public ExerciseCreateDTO(String description, String title, Muscle muscle, Difficulty difficulty) {
        this.description = description;
        this.title = title;
        this.muscle = muscle;
        this.difficulty = difficulty;
    }

    //Setters and getters
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Muscle getMuscle() {
        return muscle;
    }

    public void setMuscle(Muscle muscle) {
        this.muscle = muscle;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }
}
