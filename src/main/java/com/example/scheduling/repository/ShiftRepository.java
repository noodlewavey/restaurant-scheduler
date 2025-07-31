package com.example.scheduling.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Repository;

import com.example.scheduling.model.Shift;

@Repository
public class ShiftRepository {


    private final Map<Long, Shift> shiftList = new HashMap<>();
    //db where staff id points to their stafff profile, a staff object
    private final AtomicLong idCreator = new AtomicLong();
     //data type generates unique IDs, can increment



     public Shift save(Shift shift){
        Long id = idCreator.incrementAndGet();
        shift.setId(id);
        shiftList.put(id, shift);
        //persisting object into the map 
        return shift;
    }

    public Shift findById(Long id){
        return shiftList.get(id);
        //returns full object 
    }
    
    public List<Shift> findShiftsByStaffId(Long staffId) {
    List<Shift> result = new ArrayList<>();
    for (Shift shift : shiftList.values()) {
        if (staffId != null && staffId.equals(shift.getStaffId())) {
            result.add(shift);
        }
    }
    return result;
}

    public Shift assignShift(Long shiftId, Long staffId){
        Shift shift = shiftList.get(shiftId);
        if (shift.getStaffId() != null){
            throw new IllegalStateException("Shift has already been assigned to a staff member!");
        }
        shift.setStaffId(staffId);
        Long id = shift.getId();
        shiftList.put(id, shift);
        //persisting object into the map 
        return shift;
    }
}

