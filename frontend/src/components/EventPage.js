import React, { useState, useEffect, useCallback } from "react";
import config from "../Config";
import { useParams } from "react-router-dom";
import { Card, Button, Form, ListGroup, Row, Col, Container } from "react-bootstrap";
import { GoogleMap, Marker, useLoadScript, useGoogleMap } from "@react-google-maps/api";
const containerStyle = {
  width: "100%",
  height: "500px",
};

const api_key = config.googleMapsApiKey
const MapWithMarker = ({ lat, lng }) => {
  const { isLoaded, loadError } = useLoadScript({
    googleMapsApiKey: api_key,
  });

  const [marker, setMarker] = useState(null);

  const onMapLoad = useCallback((map) => {
    const newMarker = new window.google.maps.Marker({
      position: { lat, lng },
      map,
    });
    setMarker(newMarker);
  }, [lat, lng]);

  useEffect(() => {
    return () => {
      if (marker) {
        marker.setMap(null);
      }
    };
  }, [marker]);

  return (
    <>
      {isLoaded ? (
        <GoogleMap
          mapContainerStyle={containerStyle}
          center={{ lat, lng }}
          zoom={16}
          onLoad={onMapLoad}
        />
      ) : (
        <p>Loading map...</p>
      )}
    </>
  );
};



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

  const formatDateRange = (startDate, endDate) => {
    const start = new Date(startDate).toLocaleString();
    const end = new Date(endDate).toLocaleString();
    return `${start} - ${end}`;
  };

  const latLngRegex = /q=([-.\d]+),([-.\d]+)/;
  const match = eventData?.locationUrl ? eventData.locationUrl.match(latLngRegex) : null;
  const lat = match ? parseFloat(match[1]) : null;
  const lng = match ? parseFloat(match[2]) : null;

  return (
      <div>
        {eventData && (
          <Card style={{ width: "100%", backgroundColor: "#8688c0" }}>
            <Card.Body>
              <Card.Title>{eventData.name}</Card.Title>
              <ListGroup variant="flush">
                <ListGroup.Item>
                  Category: {eventData.category}
                </ListGroup.Item>
                <ListGroup.Item>
                  Description: {eventData.description}
                </ListGroup.Item>
                <ListGroup.Item>
                  Date Range: {formatDateRange(eventData.startDate, eventData.endDate)}
                </ListGroup.Item>
                <ListGroup.Item>
                  Phone: {eventData.phone || "N/A"}
                </ListGroup.Item>
                <ListGroup.Item>
                  Email: {eventData.email}
                </ListGroup.Item>
                <ListGroup.Item>
                  Location: {eventData.locationName || "N/A"}{" "}
                  {eventData.locationUrl && (
                    <a href={eventData.locationUrl}>View map</a>
                  )}
                </ListGroup.Item>
              </ListGroup>
            </Card.Body>
            {lat && lng ? (
              <MapWithMarker lat={lat} lng={lng} />
            ) : (
              <Card.Footer>No location found</Card.Footer>
            )}
          </Card>
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
    <Container>
      <h3>Add Comment</h3>
      <Form onSubmit={handleSubmit}>
        <Row>
          <Col md={10}>
            <Form.Group className="mb-0">
              <Form.Control
                as="textarea"
                rows={1}
                style={{ height: "30px" }}
                placeholder="Write a comment..."
                value={commentText}
                onChange={(event) => setCommentText(event.target.value)}
                required
              />
            </Form.Group>
          </Col>
          <Col md={2} className="d-flex align-items-end">
            <Button variant="primary" type="submit">
              Submit
            </Button>
          </Col>
        </Row>
      </Form>
    </Container>
  );
};

export default EventPage;  