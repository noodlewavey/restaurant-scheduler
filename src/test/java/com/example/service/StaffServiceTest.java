package com.example.service;

import com.example.scheduling.model.Role;
import com.example.scheduling.model.Staff;
import com.example.scheduling.repository.StaffRepository;
import com.example.scheduling.service.StaffService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class StaffServiceTest {

    @Mock
    private StaffRepository staffRepository;

    @InjectMocks
    private StaffService staffService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

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
