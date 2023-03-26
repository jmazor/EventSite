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
import ApproveEvents from "./components/ApproveEvents";
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
        <Route path="/approveEvents" element={<ApproveEvents />} />
        <Route path="*" element={<div>404 Not Found</div>} />
      </Routes>
    </Router>
  );
}

export default App;
