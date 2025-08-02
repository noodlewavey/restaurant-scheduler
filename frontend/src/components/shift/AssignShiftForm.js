import React, { useState, useEffect } from 'react';
import { 
  Box, 
  Select, 
  MenuItem, 
  FormControl, 
  InputLabel, 
  Typography,
  Button 
} from '@mui/material';
import { getAllStaffMembers } from '../../api/StaffAPI';
import { getAllShifts, assignShift } from '../../api/ShiftAPI';

function AssignShiftForm({ refreshTrigger, onShiftAssigned }) {
  const [staffMembers, setStaffMembers] = useState([]);
  const [shifts, setShifts] = useState([]);
  const [selectedStaff, setSelectedStaff] = useState('');
  const [selectedShift, setSelectedShift] = useState('');
  const [message, setMessage] = useState('');
  const [messageColor, setMessageColor] = useState('');

  const fetchData = async () => {
    try {
      const [staffData, shiftsData] = await Promise.all([
        getAllStaffMembers(),
        getAllShifts()
      ]);
      setStaffMembers(staffData);
      setShifts(shiftsData);
    } catch (error) {
      console.error('Error fetching data:', error);
    }
  };

  useEffect(() => {
    fetchData();
  }, []);

  useEffect(() => {
    fetchData();
  }, [refreshTrigger]);

  const getSelectedStaffRole = () => {
    const staff = staffMembers.find(s => s.id === selectedStaff);
    return staff ? staff.role : null;
  };

  const getUnassignedShiftsForRole = () => {
    const staffRole = getSelectedStaffRole();
    if (!staffRole) return [];
    
    // Get all shifts for this role that are either unassigned or could be assigned to this staff member
    return shifts.filter(shift => {
      // Must match the role
      if (shift.requiredRole !== staffRole) return false;
      
      // If shift is unassigned, it's available
      if (shift.staffId === null) return true;
      
      // If shift is already assigned to this staff member, don't show it
      if (shift.staffId === parseInt(selectedStaff)) return false;
      
      // For shifts assigned to other staff, we could potentially reassign them
      // but for simplicity in MVP, let's only show unassigned shifts
      return false;
    });
  };

  const handleSubmit = async () => {
    try {
      setMessage('');
      
      if (!selectedStaff || !selectedShift) {
        setMessage('Please select both a staff member and a shift');
        setMessageColor('red');
        return;
      }

      console.log('=== FRONTEND: Attempting to assign shift ===');
      console.log('Shift ID:', selectedShift);
      console.log('Staff ID:', selectedStaff);
      
      const response = await assignShift(selectedShift, selectedStaff);
      console.log('=== FRONTEND: Assignment successful ===');
      console.log('Response:', response);
      
      setMessage('Shift has been assigned!');
      setMessageColor('green');
      setSelectedStaff('');
      setSelectedShift('');
      
      // Update local state directly
      setShifts((prev) =>
        prev.map((shift) =>
          shift.id === parseInt(selectedShift) ? { ...shift, staffId: parseInt(selectedStaff) } : shift
        )
      );
      
      // Trigger ShiftList refresh
      if (onShiftAssigned) {
        onShiftAssigned();
      }
    } catch (error) {
      console.log('=== FRONTEND: Assignment failed ===');
      console.log('Error:', error);
      console.log('Error message:', error.message);
      setMessage(error.message || 'Failed to assign shift');
      setMessageColor('red');
    }
  };

  return (
    <Box sx={{ p: 3, border: '1px solid #ccc', borderRadius: 2, maxWidth: 500, mx: 'auto', my: 2 }}>
      <Typography variant="h4" component="h2" gutterBottom>
        Assign Shift to Staff
      </Typography>
      
      <Typography variant="h6" gutterBottom sx={{ mt: 2 }}>
        Assigned Employee
      </Typography>
      <FormControl fullWidth margin="normal">
        <InputLabel>Select Staff Member</InputLabel>
        <Select
          value={selectedStaff}
          label="Select Staff Member"
          onChange={(e) => setSelectedStaff(e.target.value)}
        >
          {staffMembers.map((staff) => (
            <MenuItem key={staff.id} value={staff.id}>
              {staff.firstName} {staff.lastName} 
              <Typography component="span" sx={{ color: 'gray', ml: 1 }}>
                (Staff Id: {staff.id}, Role: {staff.role})
              </Typography>
            </MenuItem>
          ))}
        </Select>
      </FormControl>
      
      <Typography variant="h6" gutterBottom sx={{ mt: 2 }}>
        Available Shifts
      </Typography>
      <FormControl fullWidth margin="normal">
        <InputLabel>Select Shift</InputLabel>
        <Select
          value={selectedShift}
          label="Select Shift"
          onChange={(e) => setSelectedShift(e.target.value)}
          disabled={!selectedStaff}
        >
          {selectedStaff && getUnassignedShiftsForRole().length === 0 && (
            <MenuItem disabled>
              <Typography sx={{ color: 'gray', fontStyle: 'italic' }}>
                No shifts available for {getSelectedStaffRole()} role
              </Typography>
            </MenuItem>
          )}
          {selectedStaff && getUnassignedShiftsForRole().length > 0 && (
            <MenuItem disabled>
              <Box sx={{ display: 'flex', justifyContent: 'space-between', width: '100%', fontWeight: 'bold' }}>
                <span>SHIFT START DATE</span>
                <span>SHIFT END DATE</span>
                <span>SHIFT START TIME</span>
                <span>SHIFT END TIME</span>
              </Box>
            </MenuItem>
          )}
          {selectedStaff && getUnassignedShiftsForRole().map((shift) => (
            <MenuItem key={shift.id} value={shift.id}>
              <Box sx={{ display: 'flex', justifyContent: 'space-between', width: '100%' }}>
                <span>{shift.shiftStartDate}</span>
                <span>{shift.shiftEndDate}</span>
                <span>{shift.shiftStartTime}</span>
                <span>{shift.shiftEndTime}</span>
              </Box>
            </MenuItem>
          ))}
        </Select>
      </FormControl>
      
      <Button 
        variant="contained" 
        onClick={handleSubmit}
        sx={{ mt: 2 }}
        fullWidth
        disabled={!selectedStaff || !selectedShift}
      >
        Submit
      </Button>
      
      {message && (
        <Typography 
          variant="body1" 
          sx={{ mt: 2, color: messageColor }}
        >
          {message}
        </Typography>
      )}
    </Box>
  );
}

export default AssignShiftForm; 