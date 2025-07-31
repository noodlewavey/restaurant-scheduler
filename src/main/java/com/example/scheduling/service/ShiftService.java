package com.example.scheduling.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.example.scheduling.model.Shift;
import com.example.scheduling.repository.ShiftRepository;

@Service
public class ShiftService {

        private final ShiftRepository shiftRepository;
        //syntax: why private? 

        public ShiftService(ShiftRepository shiftRepository) {
        this.shiftRepository = shiftRepository;
    }
        public Shift createShift(Shift shift) {
            //convert shiftStartDate+shiftStartTime and enddate+endttime into flattened localDatetIME OBJECT 

            //check if requiredrole, shiftstartdate, shfitenddate, shiftstarttime, shiftendttime is not null
            //id, staffid can be null for now 
            //not validaiting requiredrole because it will be selected from a dropdown

            if (shift.getRequiredRole() == null ||
                shift.getShiftStartDate() == null ||
                shift.getShiftEndDate() == null ||
                shift.getShiftStartTime() == null ||
                shift.getShiftEndTime() == null) {
                
                throw new IllegalArgumentException("All shift fields (requiredRole, start/end date and time) must be provided.");
            }

            //check if time is in bounds

            LocalDateTime startDateTime = LocalDateTime.of(shift.getShiftStartDate(), shift.getShiftStartTime());
            LocalDateTime endDateTime = LocalDateTime.of(shift.getShiftEndDate(), shift.getShiftEndTime());
            if (!startDateTime.isBefore(endDateTime)) {
                throw new IllegalArgumentException("Shift end time must be after start time.");
            }
            if (startDateTime.isBefore(LocalDateTime.now())) {
                throw new IllegalArgumentException("Can only schedule shifts into the future!");
            }

            //we can have overlap, can schedule multiple hosts, serrvers, blah blah

            return shiftRepository.save(shift);

    
}
}
