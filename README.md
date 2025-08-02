## Quick Start

1. Start backend: `./mvnw spring-boot:run`
2. Start frontend: `cd frontend && npm start`
3. Open http://localhost:3000
4. Test by adding staff and creating shifts!



## SETUP INSTRUCTIONS:

Ensure you have these installed:

Node.js (v14 or later) and npm (v6 or later)
Java (JDK 11 or later)

**FOR THE BACKEND:**

First clone the repository 

`git clone https://github.com/noodlewavey/restaurant-scheduler.git`

Navigate to the backend directory:

`cd src/main/java/com/example/scheduling`

Build the backend using maven wrapper:

`./mvnw clean install`

Run the backend server

`./mvnw spring-boot:run`

The backend will now begin on **http://localhost:8080**

**FOR THE FRONTEND**

Navigate to the frontend

`cd frontend`

Install dependencies 

`npm install`
Start the dev server

`npm start`

The frontend will be on **http://localhost:3000**

Now you can run and test my application!

## FEATURES IN MY APPLICATION
- Adds new staff members through a form with validation, no duplicates allowed
- Displays all staff members in a table that automatically updates when new staff is needed
- Creates new shifts through a form with date/time pickers 
- Assigns staff to unassigned shifts through a form dropdown that filters by role matching, with availability checking to prevent assigning overlapping shifts
- Displays all shifts in table that automatically updates when shifts are created or assigned.

## API Endpoints

**Staff:**
- `GET /staff/get-staff-members-list` - Get all staff
- `POST /staff/save-staff-member` - Add new staff
- `GET /staff/{staffId}` - Get staff by ID

**Shifts:**
- `POST /shift/create-shift` - Create new shift
- `GET /shift/get-all-shifts` - Get all shifts
- `POST /shift/{shiftId}/assign` - Assign staff to shift

## MY APPROACH:

- This application is built using Spring Boot as the backend and React for the frontend.

- The backend is created with a RESTful API design using the Controller -> Service -> Repo pattern. Instead of a database, I used an in-memory data storage using java HashMaps for faster development

- There is comprehensive error handling for my API endpoints that are propagated to the frontend

- I unit-tested the backend with JUnit and Mockito

- The frontend uses React functional components with hooks for state management. 

- I used Material UI components for a professional look

## KNOWN LIMITATIONS/TRADEOFFS:

- I used key-based remounting for StaffList.js, ShiftList.js (These are components to display all staff and shifts that update in real time). Because this is a timed assessment and an MVP, the remounting of the components that trigger fresh API calls was the simplest thing to implement and debug, and would be guaranteed to be consistent and show all the new data. But in production, I would not do this (poor performance, too many API calls). 

- For the actual 7shifts restaurant scheduling function that supports thousands of restaurants using the platform, for the front end, I would use Redux for state management. 

- Having in-memory HashMaps for the ‘database’ instead of a real database prevents us from doing database indexing, range queries…we only have one index for every staff/shift, which is the id. This can slow down lookups. For example, functions like findShiftsByStaffId() is O(n) with my HashMap approach but with a real database that has an index on staff_id, we can find the shifts in log n time. Again, for a time-constrained MVP, this is fine because our dataset never grows large. 

- I do not have pagination for my staff list and shift list that is being displayed in the front end. 

- I added unit tests for the basic functionalities of the backend, but I don’t have full automated end to end integration tests for the user flow. I mimicked this by testing the user experience manually on my browser, but if I had more time, I would write E2E tests in Cypress. 

## FINAL DATA FLOW


Frontend components -> API functions -> Controllers in backend -> Service Layer -> Repository Layer -> In-memory Hash Map Storage 
