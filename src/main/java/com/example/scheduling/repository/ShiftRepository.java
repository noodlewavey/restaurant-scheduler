package com.example.scheduling.repository;

import java.util.HashMap;
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
}

