import React from "react";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import NavigationButtons from "./components/NavigationButtons";
import HomePage from "./components/Home";
import RegisterForm from "./components/RegisterForm";
import LoginForm from "./components/LoginForm";
import AdminForm from "./components/AdminForm.js";
import CreateUniversity from "./components/CreateUniversity.js";
import Admin from "./components/Admin";

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<NavigationButtons />} />
        <Route path="/register" element={<RegisterForm />} />
        <Route path="/home" element={<HomePage />} />
        <Route path="/login" element={<LoginForm />} />
        <Route path="/adminCreate" element={<AdminForm />} />
        <Route path="/createUniversity" element={<CreateUniversity />} />
        <Route path="/admin" element={<Admin />} />
        <Route path="*" element={<div>404 Not Found</div>} />
      </Routes>
    </Router>
  );
}

export default App;
