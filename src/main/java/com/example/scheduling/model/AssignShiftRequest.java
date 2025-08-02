package com.example.scheduling.model;

public class AssignShiftRequest {
    private Long staffId;

    public AssignShiftRequest() {
    }

    public AssignShiftRequest(Long staffId) {
        this.staffId = staffId;
    }

    public Long getStaffId() {
        return staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }
} 