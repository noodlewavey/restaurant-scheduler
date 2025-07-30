package com.example.scheduling.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Shift {
    private Long staffId;
    //staffId FK to Staff id 
    private Role requiredRole;
    //this role means we require a server for this shift
    private Long id;
    private LocalDate shiftStartDate;
    private LocalDate shiftEndDate;
    //to handle the case where shift ends after midnight, we need to record end date too. 
    private LocalTime shiftStartTime;
    private LocalTime shiftEndTime;


    public Long getStaffId() {
        return staffId;
    }

    public Role getRequiredRole() {
        return requiredRole;
    }

    public Long getId() {
        return id;
    }

    public LocalDate getShiftStartDate() {
        return shiftStartDate;
    }

    public LocalDate getShiftEndDate() {
        return shiftEndDate;
    }

    public LocalTime getShiftStartTime() {
        return shiftStartTime;
    }

    public LocalTime getShiftEndTime() {
        return shiftEndTime;
    }


    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }

    public void setRequiredRole(Role requiredRole) {
        this.requiredRole = requiredRole;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setShiftStartDate(LocalDate shiftStartDate) {
        this.shiftStartDate = shiftStartDate;
    }

    public void setShiftEndDate(LocalDate shiftEndDate) {
        this.shiftEndDate = shiftEndDate;
    }

    public void setShiftStartTime(LocalTime shiftStartTime) {
        this.shiftStartTime = shiftStartTime;
    }

    public void setShiftEndTime(LocalTime shiftEndTime) {
        this.shiftEndTime = shiftEndTime;
    }
}


    
