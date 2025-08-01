import React, { useState, useEffect } from 'react';
import { 
  Box, 
  Table, 
  TableBody, 
  TableCell, 
  TableContainer, 
  TableHead, 
  TableRow, 
  Paper, 
  Typography 
} from '@mui/material';
import { getAllShifts } from '../../api/ShiftAPI';
import { getStaffById } from '../../api/StaffAPI';

function ShiftList() {
  const [shifts, setShifts] = useState([]);
  const [staffNames, setStaffNames] = useState({});
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    const fetchShifts = async () => {
      try {
        const data = await getAllShifts();
        setShifts(data);
        
        // Fetch staff names for assigned shifts
        const staffIds = [...new Set(data.filter(shift => shift.staffId).map(shift => shift.staffId))];
        const staffNamesData = {};
        
        for (const staffId of staffIds) {
          try {
            const staff = await getStaffById(staffId);
            staffNamesData[staffId] = `${staff.firstName} ${staff.lastName}`;
          } catch (error) {
            staffNamesData[staffId] = `Staff ID: ${staffId}`;
          }
        }
        
        setStaffNames(staffNamesData);
        setLoading(false);
      } catch (error) {
        setError(error.message);
        setLoading(false);
      }
    };

    fetchShifts();
  }, []);

  if (loading) {
    return (
      <Box sx={{ p: 3 }}>
        <Typography>Loading shifts...</Typography>
      </Box>
    );
  }

  if (error) {
    return (
      <Box sx={{ p: 3 }}>
        <Typography color="error">{error}</Typography>
      </Box>
    );
  }

  return (
    <Box sx={{ p: 3 }}>
      <Typography variant="h4" component="h2" gutterBottom>
        List of all shifts
      </Typography>
      
      <TableContainer component={Paper}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>Shift ID</TableCell>
              <TableCell>Required Role</TableCell>
              <TableCell>Assigned Staff Member</TableCell>
              <TableCell>Shift Start Date</TableCell>
              <TableCell>Shift End Date</TableCell>
              <TableCell>Shift Start Time</TableCell>
              <TableCell>Shift End Time</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {shifts.map((shift) => (
              <TableRow key={shift.id}>
                <TableCell>{shift.id}</TableCell>
                <TableCell>{shift.requiredRole}</TableCell>
                <TableCell>
                  {shift.staffId 
                    ? staffNames[shift.staffId] || `Staff ID: ${shift.staffId}`
                    : 'Unassigned'
                  }
                </TableCell>
                <TableCell>{shift.shiftStartDate}</TableCell>
                <TableCell>{shift.shiftEndDate}</TableCell>
                <TableCell>{shift.shiftStartTime}</TableCell>
                <TableCell>{shift.shiftEndTime}</TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
    </Box>
  );
}

export default ShiftList; 