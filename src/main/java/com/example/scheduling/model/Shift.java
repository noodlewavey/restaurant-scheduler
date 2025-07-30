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


    


    
}
