import React, { useState } from 'react';
import './App.css';
import AddStaffForm from './components/staff/AddStaffForm';
import StaffList from './components/staff/StaffList';
import CreateShiftForm from './components/shift/CreateShiftForm';
import AssignShiftForm from './components/shift/AssignShiftForm';

function App() {
  const [tabIndex, setTabIndex] = useState(0);
  const [refreshStaffList, setRefreshStaffList] = useState(0);

  const handleStaffAdded = () => {
    setRefreshStaffList(prev => prev + 1);
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
          <CreateShiftForm />
          <AssignShiftForm />
        </div>
      )}
    </div>
  );
}

export default App;
