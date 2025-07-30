package com.example.scheduling.service;

import com.example.scheduling.model.Staff;
import com.example.scheduling.repository.StaffRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StaffService {

    private final StaffRepository staffRepository;

    public StaffService(StaffRepository staffRepository) {
        this.staffRepository = staffRepository;
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
}
