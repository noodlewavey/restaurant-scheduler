package com.example.scheduling.service;

import com.example.scheduling.model.Shift;
import com.example.scheduling.repository.ShiftRepository;

public class ShiftService {

        private final ShiftRepository shiftRepository;
        //syntax: why private? 

        public ShiftService(ShiftRepository shiftRepository) {
        this.shiftRepository = shiftRepository;
    }
        public Shift createShift(Shift shift) {
            return shift;
    
}
}
