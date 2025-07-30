package com.example.scheduling.controller;

import com.example.scheduling.model.Staff;
import com.example.scheduling.service.StaffService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/staff")
public class StaffController {

    private final StaffService staffService;

    public StaffController(StaffService staffService) {
        this.staffService = staffService;
     }

    @PostMapping("/save-staff-member")
    public ResponseEntity<Staff> saveStaff(@RequestBody Staff staff) {
        Staff savedStaff = staffService.createStaff(staff); //this validates if its okay
        return ResponseEntity.ok(savedStaff);
    }
}
