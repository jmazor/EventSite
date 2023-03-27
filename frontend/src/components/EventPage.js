import React, { useState, useEffect } from "react";
import config from "../Config";
import { useParams } from "react-router-dom";

const EventPage = () => {
  const [eventData, setEventData] = useState(null);
  const [comments, setComments] = useState([]);
  const url = config.url;
  const { eventId } = useParams();

  useEffect(() => {
    const fetchData = async () => {
      try {
        const token = localStorage.getItem("token");

        const eventResponse = await fetch(`${url}/api/event/${eventId}`, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        const eventJson = await eventResponse.json();
        setEventData(eventJson);

        const commentsResponse = await fetch(`${url}/api/event/${eventId}/comments`, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        const commentsJson = await commentsResponse.json();
        setComments(commentsJson);
      } catch (error) {
        console.error("EventPage error:", error);
      }
    };

    fetchData();
  }, [url, eventId]);

  // Implement addComment, editComment, and deleteComment functions here

  return (
    <div>
      {eventData && (
        <>
          <h1>Event Details</h1>
          <pre>{JSON.stringify(eventData, null, 2)}</pre>
        </>
      )}
      <h1>Comments</h1>
      <pre>{JSON.stringify(comments, null, 2)}</pre>
      {/* Add UI components for adding, editing, and deleting comments here */}
    </div>
  );
};

export default EventPage;
