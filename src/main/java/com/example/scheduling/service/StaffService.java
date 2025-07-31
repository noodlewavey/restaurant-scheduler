package com.example.scheduling.service;

import com.example.scheduling.model.Shift;
import com.example.scheduling.model.Staff;
import com.example.scheduling.repository.ShiftRepository;
import com.example.scheduling.repository.StaffRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class StaffService {

    private final StaffRepository staffRepository;

    private final ShiftRepository shiftRepository;

    public StaffService(StaffRepository staffRepository, ShiftRepository shiftRepository) {
        this.staffRepository = staffRepository;
        this.shiftRepository = shiftRepository;
    }

    public Staff createStaff(Staff staff) {
        
        // 1. Validate the role (must be one of the allowed values)
        // 2. Possibly check for duplicates or other rules
        // 3. Call repository to save the staff
        // 4. Return the saved staff

        //
        //deconstruct requestbody and check if firstname string, lastname string, role follows. enum (SERVER, COOK, MANAGER, HOST), phone number is valid phone number 
        //check repo if staff with this firstname, lastname, and phonenumber exist (this particular combination)
        //if duplicate, return error ("Staff has already been added")
        //i think there is 1-1 mapping between unique employee and role, as in every empolyee has one role, for simplicity
        //if every input type is valid, call repository to save the staff
        //return the saved staff 

        if (staff.getFirstName() == null || staff.getFirstName().isEmpty() ||
            staff.getLastName() == null || staff.getLastName().isEmpty()) {
            throw new IllegalArgumentException("First name AND last name must be provided.");
        }

        if (staff.getPhoneNumber() == null || !staff.getPhoneNumber().matches("^\\+?[0-9\\-\\s]+$")) {
            throw new IllegalArgumentException("The phone number is invalid.");
        }

        if (staff.getRole() == null) {
            throw new IllegalArgumentException("The staff member must be assigned a role.");
        }
        //note: im not validaing the role because it will be selected from a dropdown 

        List<Staff> existingStaff = staffRepository.findAll();
        //syntax
        boolean duplicateExists = existingStaff.stream().anyMatch(existing ->
            existing.getFirstName().equalsIgnoreCase(staff.getFirstName()) &&
            existing.getLastName().equalsIgnoreCase(staff.getLastName()) &&
            existing.getPhoneNumber().equals(staff.getPhoneNumber())
        );

        if (duplicateExists) {
            throw new IllegalArgumentException("This staff member has already been added.");
        }

        return staffRepository.save(staff);

    }

    public boolean isStaffAvailable(Long staffId, LocalDateTime addShiftStart, LocalDateTime addShiftEnd) {
        //syntax....we take in flat addshiftStart, addShiftEnd
    Staff staff = staffRepository.findById(staffId);
    if (staff == null) {
        throw new IllegalArgumentException("Staff not found.");
    }

    List<Shift> assignedShifts = shiftRepository.findShiftsByStaffId(staffId);
    
    for (Shift s : assignedShifts) {
        LocalDateTime existingStart = LocalDateTime.of(s.getShiftStartDate(), s.getShiftStartTime());
        LocalDateTime existingEnd = LocalDateTime.of(s.getShiftEndDate(), s.getShiftEndTime());

        boolean overlaps = !(addShiftEnd.isBefore(existingStart) || addShiftStart.isAfter(existingEnd));
        if (overlaps) return false;
    }

    return true;
}

}
