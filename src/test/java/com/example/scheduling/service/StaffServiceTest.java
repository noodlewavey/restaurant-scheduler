package com.example.scheduling.service;

import com.example.scheduling.model.Shift;
import com.example.scheduling.model.Staff;
import com.example.scheduling.model.Role;
import com.example.scheduling.repository.ShiftRepository;
import com.example.scheduling.repository.StaffRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StaffServiceTest {

    @Mock
    private StaffRepository staffRepository;

    @Mock
    private ShiftRepository shiftRepository;

    private StaffService staffService;

    @BeforeEach
    void setUp() {
        staffService = new StaffService(staffRepository, shiftRepository);
    }

    @Test
    void testIsStaffAvailable_NoExistingShifts_ShouldReturnTrue() {
        // Arrange
        Long staffId = 1L;
        LocalDateTime newShiftStart = LocalDateTime.of(2025, 8, 2, 9, 0);
        LocalDateTime newShiftEnd = LocalDateTime.of(2025, 8, 2, 17, 0);

        Staff staff = new Staff("John", "Doe", Role.SERVER, "123-456-7890");
        staff.setId(staffId);

        when(staffRepository.findById(staffId)).thenReturn(staff);
        when(shiftRepository.findShiftsByStaffId(staffId)).thenReturn(Arrays.asList());

        // Act
        boolean result = staffService.isStaffAvailable(staffId, newShiftStart, newShiftEnd);

        // Assert
        assertTrue(result);
        verify(staffRepository).findById(staffId);
        verify(shiftRepository).findShiftsByStaffId(staffId);
    }

    @Test
    void testIsStaffAvailable_NonOverlappingShifts_ShouldReturnTrue() {
        // Arrange
        Long staffId = 1L;
        LocalDateTime newShiftStart = LocalDateTime.of(2025, 8, 2, 18, 0);
        LocalDateTime newShiftEnd = LocalDateTime.of(2025, 8, 2, 23, 0);

        Staff staff = new Staff("John", "Doe", Role.SERVER, "123-456-7890");
        staff.setId(staffId);

        Shift existingShift = new Shift(Role.SERVER, LocalDate.of(2025, 8, 2), LocalDate.of(2025, 8, 2),
                LocalTime.of(9, 0), LocalTime.of(17, 0));
        existingShift.setId(1L);
        existingShift.setStaffId(staffId);

        when(staffRepository.findById(staffId)).thenReturn(staff);
        when(shiftRepository.findShiftsByStaffId(staffId)).thenReturn(Arrays.asList(existingShift));

        // Act
        boolean result = staffService.isStaffAvailable(staffId, newShiftStart, newShiftEnd);

        // Assert
        assertTrue(result);
    }

    @Test
    void testIsStaffAvailable_OverlappingShifts_ShouldReturnFalse() {
        // Arrange
        Long staffId = 1L;
        LocalDateTime newShiftStart = LocalDateTime.of(2025, 8, 2, 10, 0);
        LocalDateTime newShiftEnd = LocalDateTime.of(2025, 8, 2, 17, 0);

        Staff staff = new Staff("John", "Doe", Role.SERVER, "123-456-7890");
        staff.setId(staffId);

        Shift existingShift = new Shift(Role.SERVER, LocalDate.of(2025, 8, 2), LocalDate.of(2025, 8, 2),
                LocalTime.of(9, 0), LocalTime.of(17, 0));
        existingShift.setId(1L);
        existingShift.setStaffId(staffId);

        when(staffRepository.findById(staffId)).thenReturn(staff);
        when(shiftRepository.findShiftsByStaffId(staffId)).thenReturn(Arrays.asList(existingShift));

        // Act
        boolean result = staffService.isStaffAvailable(staffId, newShiftStart, newShiftEnd);

        // Assert
        assertFalse(result);
    }

    @Test
    void testIsStaffAvailable_ExactSameTime_ShouldReturnFalse() {
        // Arrange
        Long staffId = 1L;
        LocalDateTime newShiftStart = LocalDateTime.of(2025, 8, 2, 9, 0);
        LocalDateTime newShiftEnd = LocalDateTime.of(2025, 8, 2, 17, 0);

        Staff staff = new Staff("John", "Doe", Role.SERVER, "123-456-7890");
        staff.setId(staffId);

        Shift existingShift = new Shift(Role.SERVER, LocalDate.of(2025, 8, 2), LocalDate.of(2025, 8, 2),
                LocalTime.of(9, 0), LocalTime.of(17, 0));
        existingShift.setId(1L);
        existingShift.setStaffId(staffId);

        when(staffRepository.findById(staffId)).thenReturn(staff);
        when(shiftRepository.findShiftsByStaffId(staffId)).thenReturn(Arrays.asList(existingShift));

        // Act
        boolean result = staffService.isStaffAvailable(staffId, newShiftStart, newShiftEnd);

        // Assert
        assertFalse(result);
    }

    @Test
    void testIsStaffAvailable_AdjacentShifts_ShouldReturnTrue() {
        // Arrange
        Long staffId = 1L;
        LocalDateTime newShiftStart = LocalDateTime.of(2025, 8, 2, 17, 0);
        LocalDateTime newShiftEnd = LocalDateTime.of(2025, 8, 2, 23, 0);

        Staff staff = new Staff("John", "Doe", Role.SERVER, "123-456-7890");
        staff.setId(staffId);

        Shift existingShift = new Shift(Role.SERVER, LocalDate.of(2025, 8, 2), LocalDate.of(2025, 8, 2),
                LocalTime.of(9, 0), LocalTime.of(17, 0));
        existingShift.setId(1L);
        existingShift.setStaffId(staffId);

        when(staffRepository.findById(staffId)).thenReturn(staff);
        when(shiftRepository.findShiftsByStaffId(staffId)).thenReturn(Arrays.asList(existingShift));

        // Act
        boolean result = staffService.isStaffAvailable(staffId, newShiftStart, newShiftEnd);

        // Assert
        assertTrue(result);
    }

    @Test
    void testIsStaffAvailable_MultipleExistingShifts_OneOverlaps_ShouldReturnFalse() {
        // Arrange
        Long staffId = 1L;
        LocalDateTime newShiftStart = LocalDateTime.of(2025, 8, 2, 10, 0);
        LocalDateTime newShiftEnd = LocalDateTime.of(2025, 8, 2, 17, 0);

        Staff staff = new Staff("John", "Doe", Role.SERVER, "123-456-7890");
        staff.setId(staffId);

        Shift existingShift1 = new Shift(Role.SERVER, LocalDate.of(2025, 8, 1), LocalDate.of(2025, 8, 1),
                LocalTime.of(9, 0), LocalTime.of(17, 0));
        existingShift1.setId(1L);
        existingShift1.setStaffId(staffId);

        Shift existingShift2 = new Shift(Role.SERVER, LocalDate.of(2025, 8, 2), LocalDate.of(2025, 8, 2),
                LocalTime.of(9, 0), LocalTime.of(17, 0));
        existingShift2.setId(2L);
        existingShift2.setStaffId(staffId);

        when(staffRepository.findById(staffId)).thenReturn(staff);
        when(shiftRepository.findShiftsByStaffId(staffId)).thenReturn(Arrays.asList(existingShift1, existingShift2));

        // Act
        boolean result = staffService.isStaffAvailable(staffId, newShiftStart, newShiftEnd);

        // Assert
        assertFalse(result);
    }

    @Test
    void testIsStaffAvailable_MultiDayShifts_Overlapping_ShouldReturnFalse() {
        // Arrange
        Long staffId = 1L;
        LocalDateTime newShiftStart = LocalDateTime.of(2025, 8, 2, 10, 0);
        LocalDateTime newShiftEnd = LocalDateTime.of(2025, 8, 3, 17, 0);

        Staff staff = new Staff("John", "Doe", Role.SERVER, "123-456-7890");
        staff.setId(staffId);

        Shift existingShift = new Shift(Role.SERVER, LocalDate.of(2025, 8, 2), LocalDate.of(2025, 8, 3),
                LocalTime.of(9, 0), LocalTime.of(13, 0));
        existingShift.setId(1L);
        existingShift.setStaffId(staffId);

        when(staffRepository.findById(staffId)).thenReturn(staff);
        when(shiftRepository.findShiftsByStaffId(staffId)).thenReturn(Arrays.asList(existingShift));

        // Act
        boolean result = staffService.isStaffAvailable(staffId, newShiftStart, newShiftEnd);

        // Assert
        assertFalse(result);
    }

    @Test
    void testIsStaffAvailable_StaffNotFound_ShouldThrowException() {
        // Arrange
        Long staffId = 999L;
        LocalDateTime newShiftStart = LocalDateTime.of(2025, 8, 2, 9, 0);
        LocalDateTime newShiftEnd = LocalDateTime.of(2025, 8, 2, 17, 0);

        when(staffRepository.findById(staffId)).thenReturn(null);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            staffService.isStaffAvailable(staffId, newShiftStart, newShiftEnd);
        });

        assertEquals("Staff not found.", exception.getMessage());
    }

    @Test
    void testIsStaffAvailable_YourSpecificCase_ShouldReturnFalse() {
        // Arrange - Your specific case from the data
        Long staffId = 1L;
        LocalDateTime newShiftStart = LocalDateTime.of(2025, 8, 2, 10, 0);
        LocalDateTime newShiftEnd = LocalDateTime.of(2025, 8, 2, 17, 0);

        Staff staff = new Staff("Jasmine", "Blah", Role.SERVER, "123-456-7890");
        staff.setId(staffId);

        // Existing shift: 2025-08-02 09:00 to 2025-08-02 17:00
        Shift existingShift = new Shift(Role.SERVER, LocalDate.of(2025, 8, 2), LocalDate.of(2025, 8, 2),
                LocalTime.of(9, 0), LocalTime.of(17, 0));
        existingShift.setId(1L);
        existingShift.setStaffId(staffId);

        when(staffRepository.findById(staffId)).thenReturn(staff);
        when(shiftRepository.findShiftsByStaffId(staffId)).thenReturn(Arrays.asList(existingShift));

        // Act
        boolean result = staffService.isStaffAvailable(staffId, newShiftStart, newShiftEnd);

        // Assert
        assertFalse(result, "Should detect overlap between 09:00-17:00 and 10:00-17:00");
    }

    @Test
    void testIsStaffAvailable_MultipleOverlappingShifts_ShouldReturnFalse() {
        // Arrange - Test with multiple overlapping shifts (like your real data)
        Long staffId = 1L;
        LocalDateTime newShiftStart = LocalDateTime.of(2025, 8, 2, 10, 0);
        LocalDateTime newShiftEnd = LocalDateTime.of(2025, 8, 2, 17, 0);

        Staff staff = new Staff("Jasmine", "Blah", Role.SERVER, "123-456-7890");
        staff.setId(staffId);

        // Multiple existing shifts that overlap with the new shift
        Shift existingShift1 = new Shift(Role.SERVER, LocalDate.of(2025, 8, 2), LocalDate.of(2025, 8, 2),
                LocalTime.of(9, 0), LocalTime.of(17, 0));
        existingShift1.setId(1L);
        existingShift1.setStaffId(staffId);

        Shift existingShift2 = new Shift(Role.SERVER, LocalDate.of(2025, 8, 2), LocalDate.of(2025, 8, 2),
                LocalTime.of(10, 0), LocalTime.of(17, 0));
        existingShift2.setId(2L);
        existingShift2.setStaffId(staffId);

        Shift existingShift3 = new Shift(Role.SERVER, LocalDate.of(2025, 8, 2), LocalDate.of(2025, 8, 2),
                LocalTime.of(9, 0), LocalTime.of(17, 0));
        existingShift3.setId(3L);
        existingShift3.setStaffId(staffId);

        when(staffRepository.findById(staffId)).thenReturn(staff);
        when(shiftRepository.findShiftsByStaffId(staffId)).thenReturn(Arrays.asList(existingShift1, existingShift2, existingShift3));

        // Act
        boolean result = staffService.isStaffAvailable(staffId, newShiftStart, newShiftEnd);

        // Assert
        assertFalse(result, "Should detect overlap with multiple existing shifts");
    }

    @Test
    void testIsStaffAvailable_RealWorldScenario_ShouldReturnFalse() {
        // Arrange - Real world scenario: staff already has overlapping shifts
        Long staffId = 1L;
        LocalDateTime newShiftStart = LocalDateTime.of(2025, 8, 2, 10, 0);
        LocalDateTime newShiftEnd = LocalDateTime.of(2025, 8, 2, 17, 0);

        Staff staff = new Staff("Jasmine", "Blah", Role.SERVER, "123-456-7890");
        staff.setId(staffId);

        // Simulate the exact data from your application
        Shift existingShift1 = new Shift(Role.SERVER, LocalDate.of(2025, 8, 2), LocalDate.of(2025, 8, 2),
                LocalTime.of(9, 0), LocalTime.of(17, 0));
        existingShift1.setId(1L);
        existingShift1.setStaffId(staffId);

        Shift existingShift2 = new Shift(Role.SERVER, LocalDate.of(2025, 8, 2), LocalDate.of(2025, 8, 2),
                LocalTime.of(10, 0), LocalTime.of(17, 0));
        existingShift2.setId(2L);
        existingShift2.setStaffId(staffId);

        when(staffRepository.findById(staffId)).thenReturn(staff);
        when(shiftRepository.findShiftsByStaffId(staffId)).thenReturn(Arrays.asList(existingShift1, existingShift2));

        // Act
        boolean result = staffService.isStaffAvailable(staffId, newShiftStart, newShiftEnd);

        // Assert
        assertFalse(result, "Should detect overlap with existing shifts 09:00-17:00 and 10:00-17:00");
    }

    // Staff Creation Tests
    @Test
    void testCreateValidStaff() {
        Staff alice = new Staff("Alice", "Kim", Role.SERVER, "123-456-7890");
        when(staffRepository.findAll()).thenReturn(new ArrayList<>());
        when(staffRepository.save(any())).thenReturn(alice);

        Staff result = staffService.createStaff(alice);

        assertEquals("Alice", result.getFirstName());
        verify(staffRepository).save(alice);
    }

    @Test
    void testDuplicateStaffThrowsError() {
        Staff alice = new Staff("Alice", "Kim", Role.SERVER, "123-456-7890");
        List<Staff> existing = new ArrayList<>();
        existing.add(alice);

        when(staffRepository.findAll()).thenReturn(existing);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            staffService.createStaff(new Staff("Alice", "Kim", Role.SERVER, "123-456-7890"));
        });

        assertTrue(exception.getMessage().contains("already been added"));
    }

    @Test
    void testMissingLastNameThrowsError() {
        Staff john = new Staff("John", "", Role.COOK, "111-222-3333");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            staffService.createStaff(john);
        });

        assertTrue(exception.getMessage().contains("First name AND last name"));
    }

    @Test
    void testMissingRoleThrowsError() {
        Staff noRole = new Staff("Jane", "Doe", null, "111-222-3333");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            staffService.createStaff(noRole);
        });

        assertTrue(exception.getMessage().contains("must be assigned a role"));
    }

    @Test
    void testInvalidPhoneNumberThrowsError() {
        Staff badPhone = new Staff("Tom", "Lee", Role.HOST, "not-a-number");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            staffService.createStaff(badPhone);
        });

        assertTrue(exception.getMessage().contains("phone number is invalid"));
    }

    @Test
    void testMissingPhoneNumberThrowsError() {
        Staff missingPhone = new Staff("Amy", "Chen", Role.MANAGER, null);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            staffService.createStaff(missingPhone);
        });

        assertTrue(exception.getMessage().contains("phone number is invalid"));
    }
} 