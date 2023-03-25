import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import config from "../Config";

const url = config.url;


function RegisterForm() {
  const [firstName, setFirstName] = useState("");
  const [lastName, setLastName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [universities, setUniversities] = useState([]);
  const [selectedUniversity, setSelectedUniversity] = useState(null);
  const navigate = useNavigate();

  useEffect(() => {
    fetchUniversities();
  }, []);

  const fetchUniversities = async () => {
    try {
      const response = await fetch(`${url}/api/university/all`);
      const data = await response.json();
      setUniversities(data);
    } catch (error) {
      console.error("Fetch universities error:", error);
    }
  };

  const handleSignup = async (event) => {
    event.preventDefault();
    try {
      const response = await fetch(`${url}/api/user/register`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          user: { email, firstName, lastName, password },
          university: { id: selectedUniversity },
        }),
      });
      const data = await response.json();
      console.log("Signup response:", data);
      navigate("/login");
    } catch (error) {
      console.error("Signup error:", error);
      // Handle the signup error here
    }
  };

  return (
    <form onSubmit={handleSignup}>
      <label>
        First Name:
        <input
          type="text"
          value={firstName}
          onChange={(e) => setFirstName(e.target.value)}
        />
      </label>
      <br />
      <label>
        Last Name:
        <input
          type="text"
          value={lastName}
          onChange={(e) => setLastName(e.target.value)}
        />
      </label>
      <br />
      <label>
        Email:
        <input
          type="email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
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
      <label>
        University:
        <select
          value={selectedUniversity}
          onChange={(e) => setSelectedUniversity(e.target.value)}
        >
          <option value="">Select a university</option>
          {universities.map((university) => (
            <option key={university.id} value={university.id}>
              {university.name}
            </option>
          ))}
        </select>
      </label>
      <br />
      <button type="submit">Signup</button>
    </form>
  );
}


export default RegisterForm;
