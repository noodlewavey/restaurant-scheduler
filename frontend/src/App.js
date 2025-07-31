import logo from './logo.svg';
import './App.css';
import { useEffect } from 'react';
import { getAllStaffMembers, saveStaffMember } from './api/StaffAPI';

function App() {
  //testing api calls 
  useEffect(() => {
    getAllStaffMembers().then(console.log);
    //first logs empty list
    
    const testStaff = {
      firstName: "Test",
      lastName: "User",
      role: "SERVER",
      phoneNumber: "123-456-7890"
    };
    
    saveStaffMember(testStaff).then(console.log);
    //logs the test staff member
    //logs output of promise to console 
  }, []);

  return (
    <div className="App">
      <header className="App-header">
        <img src={logo} className="App-logo" alt="logo" />
        <p>
          Edit <code>src/App.js</code> and save to reload.
        </p>
        <a
          className="App-link"
          href="https://reactjs.org"
          target="_blank"
          rel="noopener noreferrer"
        >
          Learn React
        </a>
      </header>
    </div>
  );
}

export default App;
