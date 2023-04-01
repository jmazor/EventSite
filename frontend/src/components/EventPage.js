import React, { useState, useEffect } from "react";
import config from "../Config";
import { useParams } from "react-router-dom";
import { Card, Button, Form } from "react-bootstrap";

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

  const loggedInUserEmail = localStorage.getItem("username");

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
    <Card className="mb-3">
      <Card.Body>
        {!isEditing ? (
          <>
            <Card.Text>{comment.text}</Card.Text>
            <Card.Subtitle className="mb-2 text-muted">
              By {comment.user.email} -{" "}
              {new Date(comment.date).toLocaleString()}
            </Card.Subtitle>
            {loggedInUserEmail === comment.user.email && (
              <>
                <Button
                  variant="primary"
                  className="me-2"
                  onClick={() => setIsEditing(true)}
                >
                  Edit
                </Button>
                <Button variant="danger" onClick={handleDelete}>
                  Delete
                </Button>
              </>
            )}
          </>
        ) : (
          <Form onSubmit={handleEdit}>
            <Form.Group controlId="editComment">
              <Form.Label>Edit Comment:</Form.Label>
              <Form.Control
                type="text"
                value={editedText}
                onChange={(event) => setEditedText(event.target.value)}
              />
            </Form.Group>
            <Button type="submit" className="me-2">
              Save
            </Button>
            <Button type="button" variant="secondary" onClick={() => setIsEditing(false)}>
              Cancel
            </Button>
          </Form>
        )}
      </Card.Body>
    </Card>
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