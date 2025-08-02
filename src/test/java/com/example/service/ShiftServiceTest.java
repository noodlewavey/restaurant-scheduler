package com.example.service;

import com.example.scheduling.model.Role;
import com.example.scheduling.model.Shift;
import com.example.scheduling.model.Staff;
import com.example.scheduling.repository.ShiftRepository;
import com.example.scheduling.repository.StaffRepository;
import com.example.scheduling.service.ShiftService;
import com.example.scheduling.service.StaffService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class ShiftServiceTest {

    @Mock
    private ShiftRepository shiftRepository;

    @Mock 
    private StaffRepository staffRepository;

    @Mock 
    private StaffService staffService;

    @InjectMocks
    private ShiftService shiftService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
//testing create shift
    @Test
    void testCreateValidShift() {
        Shift shift = new Shift(Role.SERVER,
                LocalDate.of(2025, 9, 2),
                LocalDate.of(2025, 9, 3),
                LocalTime.of(18, 0),
                LocalTime.of(2, 0));

        when(shiftRepository.save(any())).thenReturn(shift);

        Shift result = shiftService.createShift(shift);

        assertEquals(Role.SERVER, result.getRequiredRole());
        verify(shiftRepository).save(shift);
    }

    @Test
    void testRequiredRoleIsNull() {
        Shift shift = new Shift(null,
                LocalDate.of(2025, 9, 2),
                LocalDate.of(2025, 9, 3),
                LocalTime.of(18, 0),
                LocalTime.of(2, 0));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            shiftService.createShift(shift);
        });

        assertTrue(exception.getMessage().contains("must be provided"));
    }

    @Test
    void testStartDateAfterEndDateThrowsError() {
        Shift shift = new Shift(Role.SERVER,
                LocalDate.of(2025, 9, 2),
                LocalDate.of(2025, 8, 31),
                LocalTime.of(10, 0),
                LocalTime.of(12, 0));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            shiftService.createShift(shift);
        });

        assertTrue(exception.getMessage().contains("end time must be after start time"));
    }

    @Test
    void testScheduleInPastThrowsError() {
        Shift shift = new Shift(Role.SERVER,
                LocalDate.of(2024, 7, 30),
                LocalDate.of(2024, 7, 30),
                LocalTime.of(10, 0),
                LocalTime.of(18, 0));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            shiftService.createShift(shift);
        });

        assertTrue(exception.getMessage().contains("into the future"));
    }

    //testing assign shift 

    @Test
    void testValidShiftAssignment() {
        Long shiftId = 1L;
        Long staffId = 2L;

        Shift shift = new Shift(Role.SERVER, LocalDate.of(2025, 9, 2), LocalDate.of(2025, 9, 2),
                LocalTime.of(18, 0), LocalTime.of(23, 0));
        shift.setId(shiftId);

        Staff staff = new Staff("John", "Doe", Role.SERVER, "123-456-7890");
        staff.setId(staffId);

        when(shiftRepository.findById(shiftId)).thenReturn(shift);
        when(staffRepository.findById(staffId)).thenReturn(staff);
        when(staffService.isStaffAvailable(eq(staffId), any(), any())).thenReturn(true);
        
        // Mock the assignShift method to return a shift with staffId set
        Shift assignedShift = new Shift(Role.SERVER, LocalDate.of(2025, 9, 2), LocalDate.of(2025, 9, 2),
                LocalTime.of(18, 0), LocalTime.of(23, 0));
        assignedShift.setId(shiftId);
        assignedShift.setStaffId(staffId);
        when(shiftRepository.assignShift(shiftId, staffId)).thenReturn(assignedShift);

        Shift result = shiftService.assignShift(shiftId, staffId);

        assertEquals(staffId, result.getStaffId());
        verify(shiftRepository).assignShift(shiftId, staffId);
    }

    @Test
    void testShiftIdNotFoundThrowsException() {
        Long invalidShiftId = 93479879237537L;
        Long staffId = 2L;

        when(shiftRepository.findById(invalidShiftId)).thenReturn(null);

        Exception e = assertThrows(IllegalArgumentException.class, () ->
                shiftService.assignShift(invalidShiftId, staffId));

        assertTrue(e.getMessage().contains("Shift not found with the id"));
    }

    @Test
    void testStaffIdNotFoundThrowsException() {
        Long shiftId = 1L;
        Long invalidStaffId = 999L;

        Shift shift = new Shift(Role.COOK, LocalDate.now().plusDays(1), LocalDate.now().plusDays(1),
                LocalTime.of(10, 0), LocalTime.of(18, 0));
        shift.setId(shiftId);

        when(shiftRepository.findById(shiftId)).thenReturn(shift);
        when(staffRepository.findById(invalidStaffId)).thenReturn(null);

        Exception e = assertThrows(IllegalArgumentException.class, () ->
                shiftService.assignShift(shiftId, invalidStaffId));

        assertTrue(e.getMessage().contains("Staff not found with the id"));
    }

    @Test
    void testRoleMismatchThrowsException() {
        Long shiftId = 1L;
        Long staffId = 2L;

        Shift shift = new Shift(Role.MANAGER, LocalDate.of(2025, 9, 1), LocalDate.of(2025, 9, 1),
                LocalTime.of(9, 0), LocalTime.of(17, 0));
        shift.setId(shiftId);

        Staff staff = new Staff("Jane", "Smith", Role.SERVER, "987-654-3210");
        staff.setId(staffId);

        when(shiftRepository.findById(shiftId)).thenReturn(shift);
        when(staffRepository.findById(staffId)).thenReturn(staff);

        Exception e = assertThrows(IllegalArgumentException.class, () ->
                shiftService.assignShift(shiftId, staffId));

        assertTrue(e.getMessage().contains("doesn't align with the required role"));
    }
    
    @Test
void testJasmineWangAvailabilityWithOverlappingShift() {
    // Setup Jasmine Wang
    Long staffId = 100L;
    Staff jasmine = new Staff("Jasmine", "Wang", Role.SERVER, "555-555-5555");
    jasmine.setId(staffId);

    // Shift 1: Aug 2, 2025, 9:00-17:00
    Shift shift1 = new Shift(Role.SERVER,
            LocalDate.of(2025, 8, 2),
            LocalDate.of(2025, 8, 2),
            LocalTime.of(9, 0),
            LocalTime.of(17, 0));
    shift1.setId(1L);

    // Mock repositories
    StaffService realStaffService = new StaffService(staffRepository, shiftRepository);
    when(staffRepository.findById(staffId)).thenReturn(jasmine);
    when(shiftRepository.findShiftsByStaffId(staffId)).thenReturn(List.of(shift1));

    // Shift 2: Aug 2, 2025, 10:00-17:00
    LocalDateTime shift2Start = LocalDateTime.of(2025, 8, 2, 10, 0, 0);
    LocalDateTime shift2End = LocalDateTime.of(2025, 8, 2, 17, 0, 0);

    boolean available = realStaffService.isStaffAvailable(staffId, shift2Start, shift2End);

    assertFalse(available, "Jasmine Wang should NOT be available due to overlapping shift.");
}
    // @Test
    // void testStaffHasOverlappingShiftBeforeEnd() {
    //     Long shiftId = 1L;
    //     Long staffId = 2L;

    //     Shift newShift = new Shift(Role.SERVER, LocalDate.of(2025, 9, 2), LocalDate.of(2025, 9, 2),
    //             LocalTime.of(18, 0), LocalTime.of(23, 0));
    //     newShift.setId(shiftId);

    //     Staff staff = new Staff("Bob", "Johnson", Role.SERVER, "555-123-4567");
    //     staff.setId(staffId);

    //     when(shiftRepository.findById(shiftId)).thenReturn(newShift);
    //     when(staffRepository.findById(staffId)).thenReturn(staff);
    //     when(staffService.isStaffAvailable(eq(staffId), any(), any())).thenReturn(false);

    //     Exception e = assertThrows(IllegalArgumentException.class, () ->
    //             shiftService.assignShift(shiftId, staffId));

    //     assertTrue(e.getMessage().contains("not available during this assigned shift"));
    // }

    // @Test
    // void testStaffHasOverlappingShiftAfterStart() {
    //     Long shiftId = 1L;
    //     Long staffId = 2L;

    //     Shift newShift = new Shift(Role.SERVER, LocalDate.of(2025, 9, 3), LocalDate.of(2025, 9, 3),
    //             LocalTime.of(10, 0), LocalTime.of(14, 0));
    //     newShift.setId(shiftId);

    //     Staff staff = new Staff("Alice", "Brown", Role.SERVER, "111-222-3333");
    //     staff.setId(staffId);

    //     when(shiftRepository.findById(shiftId)).thenReturn(newShift);
    //     when(staffRepository.findById(staffId)).thenReturn(staff);
    //     when(staffService.isStaffAvailable(eq(staffId), any(), any())).thenReturn(false);

    //     Exception e = assertThrows(IllegalArgumentException.class, () ->
    //             shiftService.assignShift(shiftId, staffId));

    //     assertTrue(e.getMessage().contains("not available during this assigned shift"));
    //}
}
