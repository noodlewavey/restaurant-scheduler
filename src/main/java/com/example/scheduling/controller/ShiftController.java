package com.example.scheduling.controller;

import com.example.scheduling.model.Shift;
import com.example.scheduling.model.AssignShiftRequest;
import com.example.scheduling.service.ShiftService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/shift")
public class ShiftController {

    private final ShiftService shiftService;

    public ShiftController(ShiftService shiftService) {
        this.shiftService = shiftService;
     }

    @PostMapping("/create-shift")
    public ResponseEntity<Shift> createShift(@RequestBody Shift shift) {
        Shift savedShift = shiftService.createShift(shift); //this validates if its okay
        return ResponseEntity.ok(savedShift);
    }

    @PostMapping("/{shiftId}/assign")
    public ResponseEntity<Shift> assignStaffToShift(@PathVariable Long shiftId,
                                   @RequestBody AssignShiftRequest request) {
                                    //syntax here...
        Shift assignedShift = shiftService.assignShift(shiftId, request.getStaffId());
        return ResponseEntity.ok(assignedShift);
    }

    @GetMapping("/get-all-shifts")
    public ResponseEntity<List<Shift>> getAllShifts() {
        List<Shift> shifts = shiftService.getAllShifts();
        return ResponseEntity.ok(shifts);
    }
}

