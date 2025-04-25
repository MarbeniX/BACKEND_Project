package com.backend.projectbackend.dto.routine;

import com.backend.projectbackend.model.Exercise;
import com.backend.projectbackend.model.Routine;
import com.backend.projectbackend.model.Routine.Category;

import java.util.List;

public class RoutineResponseDTO {
    private String id;
    private String name;
    private String description;
    private String creationDate;
    private List<Exercise> exercises;
    private String userId;
    private Category category;

    public RoutineResponseDTO() {}

    public RoutineResponseDTO(Routine routine) {
        this.id = routine.getId().toHexString();
        this.name = routine.getName();
        this.description = routine.getDescription();
        this.creationDate = routine.getCreationDate().toString();
        this.exercises = routine.getExercises();
        this.userId = routine.getUserId().toHexString();
        this.category = routine.getCategory();
    }

    //Setters and getters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCreationDate() { return creationDate; }
    public void setCreationDate(String creationDate) { this.creationDate = creationDate; }

    public List<Exercise> getExercises() { return exercises; }
    public void setExercises(List<Exercise> exercises) { this.exercises = exercises; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }
}
