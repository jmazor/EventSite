// components/ApproveEvents.js
import React, { useState, useEffect } from "react";
import config from "../Config";

const url = config.url;

function ApproveEvents() {
  const [events, setEvents] = useState([]);
  const [selectedEvents, setSelectedEvents] = useState([]);

  useEffect(() => {
    fetchEvents();
  }, []);

  const fetchEvents = async () => {
    const token = localStorage.getItem("token");

    try {
      const response = await fetch(`${url}/api/event/need/approval`, {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
      });

      const data = await response.json();
      setEvents(data);
    } catch (error) {
      console.error("Fetch events error:", error);
    }
  };

  const handleCheckboxChange = (event, eventId) => {
    if (event.target.checked) {
      setSelectedEvents([...selectedEvents, eventId]);
    } else {
      setSelectedEvents(selectedEvents.filter((id) => id !== eventId));
    }
  };

  const handleApprove = async () => {
    const token = localStorage.getItem("token");

    try {
      // Create an array of PublicEvent objects from the selectedEvents array
      const publicEvents = selectedEvents.map((eventId) => ({
        id: eventId,
        approval: true,
      }));

      const response = await fetch(`${url}/api/event/approve`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify(publicEvents),
      });

      const data = await response;
      console.log("Approve events response:", data);
      // Handle the successful response here
      // Optionally, refresh the events list
      fetchEvents();
    } catch (error) {
      console.error("Approve events error:", error);
    }
  };

  return (
    <div>
      {events.map((event) => (
        <div key={event.id}>
          <input
            type="checkbox"
            onChange={(e) => handleCheckboxChange(e, event.id)}
          />
          <pre>{JSON.stringify(event, null, 2)}</pre>
        </div>
      ))}
      <button onClick={handleApprove}>Approve Selected Events</button>
    </div>
  );
}

export default ApproveEvents;
