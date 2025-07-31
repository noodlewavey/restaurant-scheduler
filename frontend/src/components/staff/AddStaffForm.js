import React, { useState } from 'react';
import { 
  Box, 
  TextField, 
  Select, 
  MenuItem, 
  FormControl, 
  InputLabel, 
  Button, 
  Typography 
} from '@mui/material';
import { saveStaffMember } from '../../api/StaffAPI';

function AddStaffForm() {
  const [formData, setFormData] = useState({
    firstName: '',
    lastName: '',
    phoneNumber: '',
    role: ''
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
      const response = await saveStaffMember(formData);
      setMessage('This staff member has been saved');
      setMessageColor('green');
      setFormData({
        firstName: '',
        lastName: '',
        phoneNumber: '',
        role: ''
      });
    } catch (error) {
      setMessage(error.message || 'Failed to save staff member');
      setMessageColor('red');
    }
  };

  return (
    <Box sx={{ p: 3, border: '1px solid #ccc', borderRadius: 2, maxWidth: 400, mx: 'auto', my: 2 }}>
      <Typography variant="h4" component="h2" gutterBottom>
        Add a staff member
      </Typography>
      
      <TextField
        fullWidth
        label="First Name"
        value={formData.firstName}
        onChange={(e) => handleInputChange('firstName', e.target.value)}
        margin="normal"
      />
      
      <TextField
        fullWidth
        label="Last Name"
        value={formData.lastName}
        onChange={(e) => handleInputChange('lastName', e.target.value)}
        margin="normal"
      />
      
      <TextField
        fullWidth
        label="Phone Number"
        value={formData.phoneNumber}
        onChange={(e) => handleInputChange('phoneNumber', e.target.value)}
        margin="normal"
      />
      
      <FormControl fullWidth margin="normal">
        <InputLabel>Role</InputLabel>
        <Select
          value={formData.role}
          label="Role"
          onChange={(e) => handleInputChange('role', e.target.value)}
        >
          <MenuItem value="HOST">HOST</MenuItem>
          <MenuItem value="SERVER">SERVER</MenuItem>
          <MenuItem value="COOK">COOK</MenuItem>
          <MenuItem value="MANAGER">MANAGER</MenuItem>
        </Select>
      </FormControl>
      
      <Button 
        variant="contained" 
        onClick={handleSubmit}
        sx={{ mt: 2 }}
        fullWidth
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

export default AddStaffForm; 