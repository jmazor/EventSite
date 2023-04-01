import React from "react";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import Index from "./components/Index";
import HomePage from "./components/Home";
import AdminForm from "./components/AdminForm.js";
import CreateUniversity from "./components/CreateUniversity.js";
import SuperAdmin from "./components/SuperAdmin";
import AddEvent from "./components/AddEvent";
import AddRso from "./components/AddRso";
import CreatePrivateEvent from "./components/CreatePrivateEvent";
import CreatePublicEvent from "./components/CreatePublicEvent";
import CreateRsoEvent from "./components/CreateRsoEvent";
import ApproveEvents from "./components/ApproveEvents";
import EventPage from "./components/EventPage";
import Admin from "./components/Admin";
import JoinNewRso from "./components/JoinNewRso";
import 'bootstrap/dist/css/bootstrap.min.css';

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Index />} />
        <Route path="/home" element={<HomePage />} />
        <Route path="/adminCreate" element={<AdminForm />} />
        <Route path="/createUniversity" element={<CreateUniversity />} />
        <Route path="/superadmin" element={<SuperAdmin />} />
        <Route path="/add-event" element={<AddEvent />} />
        <Route path="/add-rso" element={<AddRso />} />
        <Route path="/createPrivateEvent" element={<CreatePrivateEvent />} />
        <Route path="/createPublicEvent" element={<CreatePublicEvent />} />
        <Route path="/createRsoEvent" element={<CreateRsoEvent />} />
        <Route path="/approveEvents" element={<ApproveEvents />} />
        <Route path="/admin" element={<Admin />} />
        <Route path="/event/:eventId" element={<EventPage />} />
        <Route path="/join/:rsoId" element={<JoinNewRso />} /> 
      </Routes>
    </Router>
  );
}

export default App;
