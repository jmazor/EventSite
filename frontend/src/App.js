import React from "react";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import Index from "./components/Index";
import HomePage from "./components/Home";
import AdminForm from "./components/AdminForm.js";
import CreateUniversity from "./components/CreateUniversity.js";
import Admin from "./components/Admin";
import AddEvent from "./components/AddEvent";
import 'bootstrap/dist/css/bootstrap.min.css';

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Index />} />
        <Route path="/home" element={<HomePage />} />
        <Route path="/adminCreate" element={<AdminForm />} />
        <Route path="/createUniversity" element={<CreateUniversity />} />
        <Route path="/admin" element={<Admin />} />
        <Route path="/add-event" element={<AddEvent />} />
        <Route path="*" element={<div>404 Not Found</div>} />
      </Routes>
    </Router>
  );
}

export default App;
