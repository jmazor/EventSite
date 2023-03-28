import React, { useState, useEffect } from "react";
import config from "../Config";
import { useNavigate, Link } from "react-router-dom";
import {
  Button,
  Modal,
  ModalHeader,
  ModalBody,
  ModalFooter,
  Form,
  FormGroup,
  FormLabel,
  FormControl
} from 'react-bootstrap';
import 'bootstrap/dist/css/bootstrap.min.css';
import axios from 'axios';

const HomePage = () => {
  const [rsoData, setRsoData] = useState([]);
  const [eventData, setEventData] = useState([]);
  const [showModal, setShowModal] = useState(false);
  const [rsoName, setRsoName] = useState("");
  const url = config.url;
  const navigate = useNavigate();

  const handleCloseModal = () => setShowModal(false);
  const handleShowModal = () => setShowModal(true);

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

    const handleCloseModal = () => setShowModal(false);
    const handleShowModal = () => setShowModal(true);

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
      {eventData.map((event) => (
        <div key={event.id}>
          <Link to={`/event/${event.id}`}>{event.name}</Link>
        </div>
      ))}

      <Button variant="primary" onClick={handleAddEvent}>Add Event</Button>
      <Button variant="primary" onClick={handleAddRso}>Add RSO</Button>

      <Button variant="primary" onClick={handleShowModal}>
        Create RSO with Bootstrap
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
            <Button variant="primary" type="submit">
              Submit
            </Button>
          </Form>
        </Modal.Body>
      </Modal>
    </div>
  );
};
export default HomePage;