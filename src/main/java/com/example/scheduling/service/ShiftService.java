package com.example.scheduling.service;

import java.time.LocalDateTime;

import com.example.scheduling.model.Shift;
import com.example.scheduling.repository.ShiftRepository;

public class ShiftService {

        private final ShiftRepository shiftRepository;
        //syntax: why private? 

        public ShiftService(ShiftRepository shiftRepository) {
        this.shiftRepository = shiftRepository;
    }
        public Shift createShift(Shift shift) {
            //convert shiftStartDate+shiftStartTime and enddate+endttime into flattened localDatetIME OBJECT 

            LocalDateTime startDateTime = LocalDatetime.of(shift.getShiftStartDate(), shift.getShiftStartTime());
            

    
            return shift
    
}
}
