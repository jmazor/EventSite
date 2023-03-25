import React, { useState, useEffect } from "react";
import config from "../Config";
import { useNavigate } from "react-router-dom";

const HomePage = () => {
  const [rsoData, setRsoData] = useState([]);
  const [eventData, setEventData] = useState([]);
  const url = config.url;
  const navigate = useNavigate();


  useEffect(() => {
    const fetchData = async () => {
      try {
        const token = localStorage.getItem("token");

        const rsoResponse = await fetch(`${url}/api/rso/all`, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        const rsoJson = await rsoResponse.json();
        setRsoData(rsoJson);

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

  const handleAddEvent = () => {
    navigate("/add-event");
  };

  const handleAddRso = () => {
    navigate("/add-rso");
  };

  return (
    <div>
      <h1>All RSO Data</h1>
      <pre>{JSON.stringify(rsoData, null, 2)}</pre>
      <h1>All Event Data</h1>
      <pre>{JSON.stringify(eventData, null, 2)}</pre>
      <button onClick={handleAddEvent}>Add Event</button>
      <button onClick={handleAddRso}>Add RSO</button>
    </div>
  );
};

export default HomePage;
