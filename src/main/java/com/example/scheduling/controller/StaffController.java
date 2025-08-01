package com.example.scheduling.controller;

import com.example.scheduling.model.Staff;
import com.example.scheduling.service.StaffService;
import com.example.scheduling.repository.StaffRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/staff")
public class StaffController {

    private final StaffService staffService;
    private final StaffRepository staffRepository;

    public StaffController(StaffService staffService, StaffRepository staffRepository) {
        this.staffService = staffService;
        this.staffRepository = staffRepository;
     }

    @PostMapping("/save-staff-member")
    public ResponseEntity<Staff> saveStaff(@RequestBody Staff staff) {
        Staff savedStaff = staffService.createStaff(staff); //this validates if its okay
        return ResponseEntity.ok(savedStaff);
    }

    @GetMapping("/get-staff-members-list")
    public ResponseEntity<List<Staff>> getStaffMembersList() {
        List<Staff> staffMembers = staffRepository.findAll();
        return ResponseEntity.ok(staffMembers);
    }

    @GetMapping("/{staffId}")
    public ResponseEntity<Staff> getStaffById(@PathVariable Long staffId) {
        Staff staff = staffRepository.findById(staffId);
        if (staff == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(staff);
    }
}
