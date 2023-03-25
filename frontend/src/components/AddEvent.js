import React, { useState, useEffect } from "react";
import config from "../Config";

const AddEvent = () => {
  const [eventData, setEventData] = useState([]);
  const url = config.url;

  useEffect(() => {
    const fetchData = async () => {
      try {
        const token = localStorage.getItem("token");

        const eventResponse = await fetch(`${url}/api/event/all`, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        const eventJson = await eventResponse.json();
        setEventData(eventJson);
      } catch (error) {
        console.error("Protected resource error:", error);
        // Handle the protected resource error here
      }
    };

    fetchData();
  }, [url]);

  const handleJoinEvent = async (event) => {
    try {
      const token = localStorage.getItem("token");
      const response = await fetch(`${url}/api/event/join`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify(event),
      });

      if (response.ok) {
        alert("Joined the event successfully!");
      } else {
        alert("Failed to join the event.");
      }
    } catch (error) {
      console.error("Error joining the event:", error);
    }
  };

  return (
    <div>
      <h1>All Events</h1>
      {eventData.map((event, index) => (
        <div key={index}>
          <pre>{JSON.stringify(event, null, 2)}</pre>
          <button onClick={() => handleJoinEvent(event)}>Join Event</button>
        </div>
      ))}
    </div>
  );
};

export default AddEvent;
