package com.example.service;

import com.example.scheduling.model.Role;
import com.example.scheduling.model.Shift;
import com.example.scheduling.repository.ShiftRepository;
import com.example.scheduling.service.ShiftService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ShiftServiceTest {

    @Mock
    private ShiftRepository shiftRepository;

    @InjectMocks
    private ShiftService shiftService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

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
}
