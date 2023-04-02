import React, { useState, useEffect } from "react";
import { Card, Button } from "react-bootstrap";
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

  const formatDateRange = (startDate, endDate) => {
    const start = new Date(startDate).toLocaleString();
    const end = new Date(endDate).toLocaleString();
    return `${start} - ${end}`;
  };

  return (
    <div>
      {events.map((event) => (
        <Card key={event.id} className="my-3">
          <Card.Header>
            <h5>{event.event.name}</h5>
            <p>{event.event.category}</p>
          </Card.Header>
          <Card.Body>
            <p>{event.event.description}</p>
            <p>
              Location:{" "}
              {event.event.locationName || "N/A"}{" "}
              {event.event.locationUrl && (
                <a href={event.event.locationUrl}>View map</a>
              )}
            </p>
            <p>Phone: {event.event.phone || "N/A"}</p>
            <p>Email: {event.event.email}</p>
          </Card.Body>
          <Card.Footer>
            <small className="text-muted">
              Date: {formatDateRange(event.event.startDate, event.event.endDate)}
            </small>
          </Card.Footer>
          <Card.Footer>
            <input
              type="checkbox"
              onChange={(e) => handleCheckboxChange(e, event.id)}
              />
            </Card.Footer>
          </Card>
        ))}
        <Button onClick={handleApprove} disabled={selectedEvents.length === 0}>
          Approve Selected Events
        </Button>
      </div>
 );
}

export default ApproveEvents;     
