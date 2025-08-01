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
    
    return shifts.filter(shift => 
      shift.staffId === null && shift.requiredRole === staffRole
    );
  };

  const handleSubmit = async () => {
    try {
      setMessage('');
      
      if (!selectedStaff || !selectedShift) {
        setMessage('Please select both a staff member and a shift');
        setMessageColor('red');
        return;
      }

      const response = await assignShift(selectedShift, selectedStaff);
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
          {selectedStaff && (
            <MenuItem disabled>
              <Box sx={{ display: 'flex', justifyContent: 'space-between', width: '100%', fontWeight: 'bold' }}>
                <span>SHIFT START DATE</span>
                <span>SHIFT END DATE</span>
                <span>SHIFT START TIME</span>
                <span>SHIFT END TIME</span>
              </Box>
            </MenuItem>
          )}
          {getUnassignedShiftsForRole().map((shift) => (
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