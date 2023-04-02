import React, { useState, useEffect } from "react";
import config from "../Config";
import { Card, Button } from "react-bootstrap";

const AddRso = () => {
  const [eventData, setEventData] = useState([]);
  const url = config.url;

  useEffect(() => {
    const fetchData = async () => {
      try {
        const token = localStorage.getItem("token");

        const eventResponse = await fetch(`${url}/api/rso/all`, {
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
      const response = await fetch(`${url}/api/rso/join`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify(event),
      });

      if (response.ok) {
        alert("Joined the rso successfully!");
      } else {
        alert("Failed to join the rso.");
      }
    } catch (error) {
      console.error("Error joining the event:", error);
    }
  };

  return (
    <div>
      <h1>All RSOs</h1>
      <div className="row">
        {eventData.map((event, index) => (
          <div className="col-lg-3 col-md-4 col-sm-6 mb-4" key={index}>
            <Card>
              <Card.Body className="d-flex justify-content-between align-items-center">
                <Card.Title>{event.name}</Card.Title>
                <Button variant="primary" onClick={() => handleJoinEvent(event)}>Join RSO</Button>
              </Card.Body>
            </Card>
          </div>
        ))}
      </div>
    </div>
  );
};

export default AddRso;
