import React, { useState } from 'react';
import { 
  Box, 
  Select, 
  MenuItem, 
  FormControl, 
  InputLabel, 
  Button, 
  Typography 
} from '@mui/material';
import { DatePicker, TimePicker } from '@mui/x-date-pickers';
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import dayjs from 'dayjs';
import { createShift } from '../../api/ShiftAPI';

function CreateShiftForm() {
  const [formData, setFormData] = useState({
    requiredRole: '',
    shiftStartDate: null,
    shiftEndDate: null,
    shiftStartTime: null,
    shiftEndTime: null
  });
  const [message, setMessage] = useState('');
  const [messageColor, setMessageColor] = useState('');

  const handleInputChange = (field, value) => {
    setFormData(prev => ({
      ...prev,
      [field]: value
    }));
  };

  const handleSubmit = async () => {
    try {
      setMessage('');
      
      if (!formData.requiredRole || !formData.shiftStartDate || 
          !formData.shiftEndDate || !formData.shiftStartTime || !formData.shiftEndTime) {
        setMessage('All fields are required');
        setMessageColor('red');
        return;
      }

      const shiftData = {
        requiredRole: formData.requiredRole,
        shiftStartDate: formData.shiftStartDate.format('YYYY-MM-DD'),
        shiftEndDate: formData.shiftEndDate.format('YYYY-MM-DD'),
        shiftStartTime: formData.shiftStartTime.format('HH:mm'),
        shiftEndTime: formData.shiftEndTime.format('HH:mm')
      };

      const response = await createShift(shiftData);
      setMessage('Shift created successfully');
      setMessageColor('green');
      setFormData({
        requiredRole: '',
        shiftStartDate: null,
        shiftEndDate: null,
        shiftStartTime: null,
        shiftEndTime: null
      });
    } catch (error) {
      setMessage(error.message || 'Failed to create shift');
      setMessageColor('red');
    }
  };

  return (
    <LocalizationProvider dateAdapter={AdapterDayjs}>
      <Box sx={{ p: 3, border: '1px solid #ccc', borderRadius: 2, maxWidth: 500, mx: 'auto', my: 2 }}>
        <Typography variant="h4" component="h2" gutterBottom>
          Create Shift
        </Typography>
        
        <Typography variant="h6" gutterBottom sx={{ mt: 2 }}>
          Assigned Role Type
        </Typography>
        <FormControl fullWidth margin="normal">
          <InputLabel>Role</InputLabel>
          <Select
            value={formData.requiredRole}
            label="Role"
            onChange={(e) => handleInputChange('requiredRole', e.target.value)}
          >
            <MenuItem value="HOST">HOST</MenuItem>
            <MenuItem value="SERVER">SERVER</MenuItem>
            <MenuItem value="COOK">COOK</MenuItem>
            <MenuItem value="MANAGER">MANAGER</MenuItem>
          </Select>
        </FormControl>
        
        <Typography variant="h6" gutterBottom sx={{ mt: 2 }}>
          Shift Date
        </Typography>
        <Box sx={{ display: 'flex', gap: 2, mb: 2 }}>
          <DatePicker
            label="Start Date"
            value={formData.shiftStartDate}
            onChange={(date) => handleInputChange('shiftStartDate', date)}
            minDate={dayjs()}
            sx={{ flex: 1 }}
          />
          <DatePicker
            label="End Date"
            value={formData.shiftEndDate}
            onChange={(date) => handleInputChange('shiftEndDate', date)}
            minDate={formData.shiftStartDate || dayjs()}
            sx={{ flex: 1 }}
          />
        </Box>
        
        <Typography variant="h6" gutterBottom>
          Shift Start Time
        </Typography>
        <TimePicker
          label="Start Time"
          value={formData.shiftStartTime}
          onChange={(time) => handleInputChange('shiftStartTime', time)}
          sx={{ width: '100%', mb: 2 }}
        />
        
        <Typography variant="h6" gutterBottom>
          Shift End Time
        </Typography>
        <TimePicker
          label="End Time"
          value={formData.shiftEndTime}
          onChange={(time) => handleInputChange('shiftEndTime', time)}
          sx={{ width: '100%', mb: 2 }}
        />
        
        <Button 
          variant="contained" 
          onClick={handleSubmit}
          sx={{ mt: 2 }}
          fullWidth
        >
          SUBMIT
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
    </LocalizationProvider>
  );
}

export default CreateShiftForm; 