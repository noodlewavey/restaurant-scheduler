package com.example.scheduling.controller;

import com.example.scheduling.model.Shift;
import com.example.scheduling.repository.ShiftRepository;
import com.example.scheduling.service.ShiftService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/shift")
public class ShiftController {

    private final ShiftService shiftService;

    private final ShiftRepository shiftRepository;

    public ShiftController(ShiftService shiftService, ShiftRepository shiftRepository) {
        this.shiftService = shiftService;
        this.shiftRepository = shiftRepository;
     }

    @PostMapping("/create-shift")
    public ResponseEntity<Shift> createShift(@RequestBody Shift shift) {
        Shift savedShift = shiftService.createShift(shift); //this validates if its okay
        return ResponseEntity.ok(savedShift);
    }

    @PostMapping("/{shiftId}/assign")
    public ResponseEntity<Shift> assignStaffToShift(@PathVariable Long shiftId,
                                   @RequestParam Long staffId) {
                                    //syntax here...
        // You’ll add logic here later, e.g.:
        // shiftService.assignStaffToShift(shiftId, staffId);
        Shift assignedShift = shiftRepository.assignShift(shiftId, staffId);
        return ResponseEntity.ok(assignedShift);
    }
}

