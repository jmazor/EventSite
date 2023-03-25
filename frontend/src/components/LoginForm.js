import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import config from "../Config";

const url = config.url;

function LoginForm() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const navigate = useNavigate();

  const handleSubmit = async (event) => {
    event.preventDefault();
    try {
      const response = await fetch(`${url}/api/login`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ username, password }),
      });
      const data = await response.json();
      console.log("Login response:", data);
      // Store the JWT token in localStorage
      localStorage.setItem("token", data.token);
      
      // Check the user's roles in the API response
      if (data.roles.includes("ROLE_STUDENT")) {
        // If the user is a student, redirect to the HomePage
        navigate("/home");
      } else if (data.roles.includes("ROLE_SUPER_ADMIN")) {
        // If the user is a super admin, redirect to the admin page
        navigate("/admin");
      } else {
        // If the user has a different role, handle it appropriately
      }
    } catch (error) {
      console.error("Login error:", error);
      // Handle the login error here
    }
  };
  

  const handleLogout = () => {
    // Clear the JWT token from localStorage
    localStorage.removeItem("token");
    // Handle the logout here
  };

  const handleProtectedResource = async () => {
    try {
      const token = localStorage.getItem("token");
      const response = await fetch(`${url}/api/protected-resource`, {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
      });
      const data = await response.json();
      console.log("Protected resource response:", data);
      // Handle the protected resource response here
    } catch (error) {
      console.error("Protected resource error:", error);
      // Handle the protected resource error here
    }
  };

  return (
    <>
      <form onSubmit={handleSubmit}>
        <label>
          Username:
          <input
            type="text"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
          />
        </label>
        <br />
        <label>
          Password:
          <input
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
          />
        </label>
        <br />
        <button type="submit">Login</button>
      </form>
      <button onClick={handleLogout}>Logout</button>
      <button onClick={handleProtectedResource}>Protected Resource</button>
    </>
  );
}

export default LoginForm;
