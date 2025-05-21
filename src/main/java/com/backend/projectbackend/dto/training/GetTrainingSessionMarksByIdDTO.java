package com.backend.projectbackend.dto.training;

import jakarta.validation.constraints.NotEmpty;

public class GetTrainingSessionMarksByIdDTO {
    @NotEmpty
    private String markId;
    @NotEmpty
    private String exerciseId;
    @NotEmpty
    private Long timeToComplete;
    @NotEmpty
    private Integer setNumber;
    @NotEmpty
    private Integer reps;

    public GetTrainingSessionMarksByIdDTO() {}

    public GetTrainingSessionMarksByIdDTO(String markId, String exerciseId, Long timeToComplete, Integer setNumber, Integer reps) {
        this.markId = markId;
        this.exerciseId = exerciseId;
        this.timeToComplete = timeToComplete;
        this.setNumber = setNumber;
        this.reps = reps;
    }

    public String getMarkId() { return markId; }
    public void setMarkId(String markId) { this.markId = markId; }

    public String getExerciseId() { return exerciseId; }
    public void setExerciseId(String exerciseId) { this.exerciseId = exerciseId; }

    public Long getTimeToComplete() { return timeToComplete; }
    public void setTimeToComplete(Long timeToComplete) { this.timeToComplete = timeToComplete; }

    public Integer getSetNumber() { return setNumber; }
    public void setSetNumber(Integer setNumber) { this.setNumber = setNumber; }

    public Integer getReps() { return reps; }
    public void setReps(Integer reps) { this.reps = reps; }
}
