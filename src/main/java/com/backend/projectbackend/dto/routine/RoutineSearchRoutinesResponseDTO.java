package com.backend.projectbackend.dto.routine;

public class RoutineSearchRoutinesResponseDTO {
    String id;
    String name;

    public RoutineSearchRoutinesResponseDTO() {}

    public RoutineSearchRoutinesResponseDTO(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
