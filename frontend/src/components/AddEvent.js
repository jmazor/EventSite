import React, { useState, useEffect } from "react";
import config from "../Config";
import "bootstrap/dist/css/bootstrap.min.css";


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

  const formatDate = (date) => {
    return new Date(date).toLocaleString();
  };

  return (
    <div className="container">
      <h1 className="my-4">All Events</h1>
      <div className="row">
        {eventData.map((event, index) => (
          <div key={index} className="col-md-4">
            <div className="card mb-4">
              <div className="card-body">
                <h5 className="card-title">{event.name}</h5>
                <p className="card-text">{event.description}</p>

                <p className="card-text">
                  <small className="text-muted">
                    {formatDate(event.startDate)} - {formatDate(event.endDate)}
                  </small>
                </p>
                <p className="card-text">Location: {event.locationName}</p>
                <button
                  className="btn btn-primary"
                  onClick={() => handleJoinEvent(event)}
                >
                  Join Event
                </button>
              </div>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};


export default AddEvent;
