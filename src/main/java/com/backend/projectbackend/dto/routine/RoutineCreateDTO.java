package com.backend.projectbackend.dto.routine;

import com.backend.projectbackend.model.Routine.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RoutineCreateDTO {

    @NotBlank
    @Size(min = 3, message = "Routine's name must be at least 3 characters")
    private String name;
    private String description;
    private Category category;

    public RoutineCreateDTO() {}

    public RoutineCreateDTO(String name, String description, Category category) {
        this.name = name;
        this.description = description;
        this.category = category;
    }

    //Getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }
}
