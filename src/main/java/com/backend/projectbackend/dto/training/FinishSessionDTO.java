package com.backend.projectbackend.dto.training;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class FinishSessionDTO {
    @NotEmpty
    private List<SessionMarksDTO> marks;

    public FinishSessionDTO() {}

    public FinishSessionDTO(List<SessionMarksDTO> marks) {
        this.marks = marks;
    }

    public List<SessionMarksDTO> getMarks() {
        return marks;
    }

    public void setMarks(List<SessionMarksDTO> marks) {
        this.marks = marks;
    }
}
