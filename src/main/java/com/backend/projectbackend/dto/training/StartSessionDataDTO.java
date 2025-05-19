package com.backend.projectbackend.dto.training;


import org.bson.types.ObjectId;

public class StartSessionDataDTO {
    private ObjectId RoutineId;

    public StartSessionDataDTO() {}

    public StartSessionDataDTO(ObjectId RoutineId) {
        this.RoutineId = RoutineId;
    }

    public ObjectId getRoutineId() { return RoutineId; }
    public void setRoutineId(ObjectId RoutineId) { this.RoutineId = RoutineId; }
}
