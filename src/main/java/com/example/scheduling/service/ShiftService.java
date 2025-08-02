package com.example.scheduling.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.scheduling.model.Shift;
import com.example.scheduling.model.Staff;
import com.example.scheduling.repository.ShiftRepository;
import com.example.scheduling.repository.StaffRepository;

@Service
public class ShiftService {

        private final ShiftRepository shiftRepository;
        //syntax: why private? 

        private final StaffRepository staffRepository;

        private final StaffService staffService;
        

        public ShiftService(ShiftRepository shiftRepository, StaffRepository staffRepository, StaffService staffService) {
        this.shiftRepository = shiftRepository;
        this.staffRepository = staffRepository;
        this.staffService = staffService;
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

        public Shift assignShift(Long shiftId, Long staffId) {
            //curr shift is the shift we're assigning to the staff member 

            Shift currShift = shiftRepository.findById(shiftId);
            Staff assignedStaff = staffRepository.findById(staffId);

            if (currShift == null)
            {
                throw new IllegalArgumentException("Shift not found with the id: " + shiftId);
            }

            if (assignedStaff == null) {
                throw new IllegalArgumentException("Staff not found with the id: " + staffId);
            }

            if (assignedStaff.getRole() != currShift.getRequiredRole()){
                throw new IllegalArgumentException("This staff's role doesn't align with the required role for the shift: " + currShift.getRequiredRole());
            }

            // Check if shift is already assigned
            if (currShift.getStaffId() != null) {
                throw new IllegalArgumentException("Shift is already assigned to staff member with ID: " + currShift.getStaffId());
            }

            LocalDateTime assignedStart = LocalDateTime.of(currShift.getShiftStartDate(), currShift.getShiftStartTime());
            LocalDateTime assignedEnd = LocalDateTime.of(currShift.getShiftEndDate(), currShift.getShiftEndTime());

            if (staffService.isStaffAvailable(staffId, assignedStart, assignedEnd)){
                return shiftRepository.assignShift(shiftId, staffId);
            }
            else{
                throw new IllegalArgumentException("Staff is not available during this time.");

            }



        }

    public List<Shift> getAllShifts() {
        return shiftRepository.findAll();
    }

}

