package com.example.scheduling.repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import com.example.scheduling.model.Staff;



public class StaffRepository {

    private final Map<Long, Staff> staffMembers = new HashMap<>();
    //db where staff id points to their stafff profile, a staff object
    private final AtomicLong idCreator = new AtomicLong();
     //data type generates unique IDs, can increment

    public Staff save(Staff staffMember){
        Long id = idCreator.incrementAndGet();
        staffMember.setId(id);
        staffMembers.put(id, staffMember);
        //persisting object into the map 
        return staffMember;
    }

    public List<Staff> findAll() {
        //Convert staff members list to a list object bc list can let us index over it 
        return new ArrayList<>(staffMembers.values());
    }
   
    
    
}
