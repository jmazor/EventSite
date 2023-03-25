// components/CreateUniversity.js
import React, { useState } from "react";
import config from "../Config";

const url = config.url;

function CreateUniversity() {
  const [name, setName] = useState("");
  const [location, setLocation] = useState("");
  const [description, setDescription] = useState("");
  const [numStudents, setNumStudents] = useState("");
  const [picture, setPicture] = useState("");
  const [emailDomain, setEmailDomain] = useState("");

  const handleSubmit = async (event) => {
    event.preventDefault();

    const token = localStorage.getItem("token");

    try {
      const response = await fetch(`${url}/api/admin/createUniversity`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify({
          name,
          location,
          description,
          numStudents,
          picture,
          emailDomain,
        }),
      });
      const data = await response.json();
      console.log("CreateUniversity response:", data);
      // Handle the successful response here
    } catch (error) {
      console.error("CreateUniversity error:", error);
      // Handle the error here
    }
  };

  return (
    <form onSubmit={handleSubmit}>
      <label>
        University Name:
        <input
          type="text"
          value={name}
          onChange={(e) => setName(e.target.value)}
        />
      </label>
      <br />
      <label>
        Location:
        <input
          type="text"
          value={location}
          onChange={(e) => setLocation(e.target.value)}
        />
      </label>
      <br />
      <label>
        Description:
        <textarea
          value={description}
          onChange={(e) => setDescription(e.target.value)}
        />
      </label>
      <br />
      <label>
        Number of Students:
        <input
          type="number"
          value={numStudents}
          onChange={(e) => setNumStudents(e.target.value)}
        />
      </label>
      <br />
      <label>
        Picture URL:
        <input
          type="text"
          value={picture}
          onChange={(e) => setPicture(e.target.value)}
        />
      </label>
      <br />
      <label>
        Email Domain:
        <input
          type="text"
          value={emailDomain}
          onChange={(e) => setEmailDomain(e.target.value)}
        />
      </label>
      <br />
      <button type="submit">Create University</button>
    </form>
  );
}

export default CreateUniversity;
