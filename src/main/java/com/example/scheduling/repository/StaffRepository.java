package com.example.scheduling.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import com.example.scheduling.model.Staff;



public class StaffRepository {

    private final Map<Long, Staff> staffMembers = new HashMap<>();
    //db where staff id points to their stafff profile, a staff object
    private final AtomicLong idCreator = new AtomicLong()

    // public Staff save(Staff staffMember){
    //     Long id = idCreator.incrementAndGet();
    //     staffMember.setId(id);
    //     staffMember.put()
    // }
    //data type generates unique IDs, can increment
    
    
}
