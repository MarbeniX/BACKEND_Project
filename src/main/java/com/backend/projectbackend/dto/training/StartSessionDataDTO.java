package com.backend.projectbackend.dto.training;

public class StartSessionDataDTO {
    private String routineId;

    public StartSessionDataDTO() {}

    public StartSessionDataDTO(String routineId) {
        this.routineId = routineId;
    }

    public String getRoutineId() {return routineId;}
    public void setRoutineId(String routineId) {this.routineId = routineId;}
}
