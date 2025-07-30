package com.example.scheduling.controller;

import com.example.scheduling.model.Shift;
import com.example.scheduling.service.ShiftService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/shift")
public class ShiftController {

    private final ShiftService shiftService;

    public ShiftController(ShiftService shiftService) {
        this.shiftService = shiftService;
     }

    @PostMapping("/create-shift")
    public ResponseEntity<Shift> saveStaff(@RequestBody Shift shift) {
        Shift savedStaff = shiftService.createShift(shift); //this validates if its okay
        return ResponseEntity.ok(savedStaff);
    }
}

