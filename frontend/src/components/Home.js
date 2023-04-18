import React, { useState, useEffect } from "react";
import config from "../Config";
import { useNavigate, Link } from "react-router-dom";
import {
  Button,
  Modal,
  Form,
  Card,
  Table,
} from "react-bootstrap";
import "bootstrap/dist/css/bootstrap.min.css";

const HomePage = () => {
  const roles = localStorage.getItem("roles");
  const isAdmin = roles.includes("ROLE_ADMIN");
  const [rsoData, setRsoData] = useState([]);
  const [eventData, setEventData] = useState([]);
  const [showModal, setShowModal] = useState(false);
  const [rsoName, setRsoName] = useState("");
  const [showLeaveModal, setShowLeaveModal] = useState(false);
  const [selectedRso, setSelectedRso] = useState(null);
  const url = config.url;
  const navigate = useNavigate();

  const handleCloseModal = () => setShowModal(false);
  const handleShowModal = () => setShowModal(true);
  const handleCloseLeaveModal = () => setShowLeaveModal(false);
  const handleShowLeaveModal = (rso) => {
    setSelectedRso(rso);
    setShowLeaveModal(true);
  };

  const handleRsoCreate = async (e) => {
    e.preventDefault();
    const token = localStorage.getItem("token");
    try {
      const response = await fetch(`${url}/api/rso/create`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify({ name: rsoName }),
      });
      const jsonData = await response.json();
      console.log(jsonData);
      handleCloseModal();
      // Refresh RSO data or handle new RSO data here
    } catch (error) {
      console.error("Error creating RSO:", error);
    }
  };

  const handleLeaveRso = async () => {
    const token = localStorage.getItem("token");
    const rsoId = selectedRso.id;
    try {
      const response = await fetch(`${url}/api/rso/leave/${rsoId}`, {
        method: "DELETE",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
      });
      const jsonData = await response;
      console.log(jsonData);
      handleCloseLeaveModal();
      // Refresh RSO data or handle new RSO data here
    } catch (error) {
      console.error("Error leaving RSO:", error);
    }
  };

  useEffect(() => {
    const fetchData = async () => {
      try {
        const token = localStorage.getItem("token");

        const rsoResponse = await fetch(`${url}/api/user/rso`, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        const rsoJson = await rsoResponse.json();
        setRsoData(rsoJson);

        const eventResponse = await fetch(`${url}/api/event/joined`, {
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
      <h1>Registered RSO</h1>
      {rsoData.length === 0 ? (
        <p>No RSOs to display.</p>
      ) : (
        <div className="d-flex flex-wrap">
          {rsoData.map((rso) => (
            <Card style={{ width: "18rem", margin: "1rem" }} key={rso.id}>
              <Card.Body>
                <Card.Title>{rso.name}</Card.Title>
                <Card.Text>{rso.description}</Card.Text>
                <div style={{ display: "flex", alignItems: "center" }}>
                  <Button variant="primary" onClick={() => handleShowLeaveModal(rso)}>
                    Leave
                  </Button>
                  {rso.status === false && (
                    <p style={{ marginLeft: "2rem" }}>
                      <a href={`${url}/join/${rso.id}`}>{`Join URL`}</a>
                    </p>
                  )}
                </div>
              </Card.Body>
            </Card>
          ))}
        </div>
      )}

      <h1>Registered Events</h1>
      {eventData.length === 0 ? (
        <p>No events to display.</p>
      ) : (
        <Table striped bordered hover style={{ backgroundColor: "#f1f1f1" }}>
          <thead>
            <tr>
              <th>Name</th>
              <th>Category</th>
              <th>Description</th>
              <th>Date Range</th>
              <th>Phone</th>
              <th>Email</th>
              <th>Location</th>
            </tr>
          </thead>
          <tbody>
            {eventData.map((event) => (
              <tr key={event.id}>
                <td>
                  <Link to={`/event/${event.id}`}>{event.name}</Link>
                </td>
                <td>{event.category}</td>
                <td>{event.description}</td>
                <td>
                  {new Date(event.startDate).toLocaleDateString()} - {new Date(event.endDate).toLocaleDateString()}
                </td>
                <td>{event.phone}</td>
                <td>{event.email}</td>
                <td>{event.locationName}</td>
              </tr>
            ))}
          </tbody>
        </Table>
      )}

      <Button variant="primary" onClick={handleAddEvent}>
        Add Event
      </Button>
      <Button variant="primary" onClick={handleAddRso}>
        Add RSO
      </Button>

      {isAdmin && (
        <Button variant="primary" onClick={() => navigate("/admin")}>
          Admin Dashboard
        </Button>
      )}

      <Button variant="primary" onClick={handleShowModal}>
        Create RSO
      </Button>

      <Modal show={showModal} onHide={handleCloseModal}>
        <Modal.Header closeButton>
          <Modal.Title>Create RSO</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form onSubmit={handleRsoCreate}>
            <Form.Group className="mb-3">
              <Form.Label>RSO Name</Form.Label>
              <Form.Control
                type="text"
                value={rsoName}
                onChange={(e) => setRsoName(e.target.value)}
                required
              />
            </Form.Group>
          </Form>
        </Modal.Body>
      </Modal>

      <Modal show={showLeaveModal} onHide={handleCloseLeaveModal}>
        <Modal.Header closeButton>
          <Modal.Title>Leave RSO</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          Are you sure you want to leave {selectedRso?.name}?
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={handleCloseLeaveModal}>
            Cancel
          </Button>
          <Button variant="primary" onClick={handleLeaveRso}>
            Yes
          </Button>
        </Modal.Footer>
      </Modal>
    </div>
  );
};

export default HomePage;
