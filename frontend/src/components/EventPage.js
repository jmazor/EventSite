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

        const commentsResponse = await fetch(`${url}/api/comment/${eventId}`, {
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

  const addComment = async (commentData) => {
    try {
      const token = localStorage.getItem("token");

      const response = await fetch(`${url}/api/comment/create`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify(commentData),
      });

      const json = await response.json();

      setComments([...comments, json]);
    } catch (error) {
      console.error("addComment error:", error);
    }
  };

  const editComment = async (commentData) => {
    try {
      const token = localStorage.getItem("token");

      const response = await fetch(`${url}/api/comment/edit`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify(commentData),
      });

      const json = await response.json();

      const updatedComments = comments.map((comment) =>
        comment.id === json.id ? json : comment
      );
      setComments(updatedComments);
    } catch (error) {
      console.error("editComment error:", error);
    }
  };

  const deleteComment = async (commentData) => {
    try {
      const token = localStorage.getItem("token");

      const response = await fetch(`${url}/api/comment/delete`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify(commentData),
      });

      const json = await response.json();

      const updatedComments = comments.filter(
        (comment) => comment.id !== commentData.id
      );
      setComments(updatedComments);
    } catch (error) {
      console.error("deleteComment error:", error);
    }
  };

  return (
    <div>
      {eventData && (
        <>
          <h1>Event Details</h1>
          <pre>{JSON.stringify(eventData, null, 2)}</pre>
        </>
      )}
      <h1>Comments</h1>
      {comments.map((comment) => (
        <Comment
          key={comment.id}
          comment={comment}
          onEdit={editComment}
          onDelete={deleteComment}
        />
      ))}
      <AddCommentForm onAdd={addComment} eventId={eventId} />
    </div>
  );
};

const Comment = ({ comment, onEdit, onDelete }) => {
    const [isEditing, setIsEditing] = useState(false);
    const [editedText, setEditedText] = useState(comment.text);
  
    const handleEdit = async (event) => {
      event.preventDefault();
  
      const editedComment = { ...comment, text: editedText };
  
      await onEdit(editedComment);
  
      setIsEditing(false);
    };
  
    const handleDelete = async (event) => {
      event.preventDefault();
  
      await onDelete(comment);
  
      setIsEditing(false);
    };
  
    return (
      <div>
        {!isEditing ? (
          <>
            <p>{comment.text}</p>
            <p>By {comment.user.email}</p>
            <p>On {new Date(comment.date).toLocaleString()}</p>
            <button onClick={() => setIsEditing(true)}>Edit</button>
            <button onClick={handleDelete}>Delete</button>
          </>
        ) : (
          <form onSubmit={handleEdit}>
            <label>
              Edit Comment:
              <input
                type="text"
                value={editedText}
                onChange={(event) => setEditedText(event.target.value)}
              />
            </label>
            <button type="submit">Save</button>
            <button type="button" onClick={() => setIsEditing(false)}>
              Cancel
            </button>
          </form>
        )}
      </div>
    );
  };
  
  const AddCommentForm = ({ onAdd, eventId }) => {
    const [commentText, setCommentText] = useState("");
  
    const handleSubmit = async (event) => {
      event.preventDefault();
  
      const commentData = {
        text: commentText,
        event: {
          id: eventId,
        },
      };
  
      await onAdd(commentData);
  
      setCommentText("");
    };
  
    return (
      <form onSubmit={handleSubmit}>
        <label>
          Add a Comment:
          <input
            type="text"
            value={commentText}
            onChange={(event) => setCommentText(event.target.value)}
          />
        </label>
        <button type="submit">Submit</button>
      </form>
    );
  };
export default EventPage;  