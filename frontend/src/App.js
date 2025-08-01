import React, { useState } from 'react';
import './App.css';
import AddStaffForm from './components/staff/AddStaffForm';
import StaffList from './components/staff/StaffList';
import CreateShiftForm from './components/shift/CreateShiftForm';
import AssignShiftForm from './components/shift/AssignShiftForm';
import ShiftList from './components/shift/ShiftList';

function App() {
  const [tabIndex, setTabIndex] = useState(0);
  const [refreshStaffList, setRefreshStaffList] = useState(0);
  const [refreshShiftList, setRefreshShiftList] = useState(0);
  const [refreshAssignForm, setRefreshAssignForm] = useState(0);

  const handleStaffAdded = () => {
    setRefreshStaffList(prev => prev + 1);
  };

  const handleShiftAdded = () => {
    setRefreshShiftList(prev => prev + 1);
  };

  const handleShiftFormSubmit = () => {
    setRefreshShiftList(prev => prev + 1);
  };

  const handleShiftAssigned = () => {
    setRefreshShiftList(prev => prev + 1);
  };

  return (
    <div className="App">
      <div>
        <button onClick={() => setTabIndex(0)}>Staff Management</button>
        <button onClick={() => setTabIndex(1)}>Shift Scheduling</button>
      </div>

      {tabIndex === 0 && (
        <div>
          <AddStaffForm onStaffAdded={handleStaffAdded} />
          <StaffList key={refreshStaffList} />
        </div>
      )}

      {tabIndex === 1 && (
        <div>
          <CreateShiftForm onShiftAdded={handleShiftAdded} onShiftFormSubmit={handleShiftFormSubmit} />
          <AssignShiftForm refreshTrigger={refreshShiftList} onShiftAssigned={handleShiftAssigned} />
          <ShiftList key={refreshShiftList} />
        </div>
      )}
    </div>
  );
}

export default App;
