// Admin.js
import React, { useState, useEffect } from "react";
import config from "../Config";
import {
    Button,
    Modal,
    Form,
    FormGroup,
    FormLabel,
    FormControl,
} from "react-bootstrap";
import "bootstrap/dist/css/bootstrap.min.css";
import axios from "axios";
import CreatePublicEvent from "./CreatePublicEvent";

const Admin = () => {
    const [rsoList, setRsoList] = useState([]);
    const [showModal, setShowModal] = useState(false);
    const [selectedRsoId, setSelectedRsoId] = useState("");
    const [eventName, setEventName] = useState("");
    const [eventDescription, setEventDescription] = useState("");
    const [eventCategory, setEventCategory] = useState("");
    const [showPublicEventModal, setShowPublicEventModal] = useState(false);
    const handleOpenPublicEventModal = () => setShowPublicEventModal(true);
    const handleClosePublicEventModal = () => setShowPublicEventModal(false);


    const url = config.url;

    const handleCloseModal = () => setShowModal(false);
    const handleShowModal = () => setShowModal(true);

    useEffect(() => {
        const fetchData = async () => {
            try {
                const token = localStorage.getItem("token");
                console.log(token);

                const response = await axios.get(`${url}/api/rsoadmin/rso`, {
                    headers: {
                        Authorization: `Bearer ${token}`,
                    },
                });
                setRsoList(response.data);
                console.log(response.data)
            } catch (error) {
                console.error("Error fetching RSO list:", error);
            }
        };

        fetchData();
    }, [url]);

    const handleCreateRsoEvent = async (e) => {
        e.preventDefault();
        const token = localStorage.getItem("token");
        try {
            const response = await axios.post(`${url}/api/event/create/rso`, {
                rso: {
                    id: selectedRsoId,
                },
                event: {
                    name: eventName,
                    description: eventDescription,
                    category: eventCategory.toUpperCase(),
                },
            }, {
                headers: {
                    "Content-Type": "application/json",
                    Authorization: `Bearer ${token}`,
                },
            });
            // Handle the successful response here
            handleCloseModal();
        } catch (error) {
            console.error("Error creating RSO event:", error);
        }
    };

    return (
        <div>
            <h1>Admin Page</h1>
            <h2>RSO List</h2>
            <ul>
                {rsoList.map((rso) => (
                    <li key={rso.id}>{rso.name}</li>
                ))}
            </ul>
            <Button variant="primary" onClick={handleShowModal}>
                Create RSO Event
            </Button>
            <Button variant="primary" onClick={handleOpenPublicEventModal}>
                Create Public Event
            </Button>


            <Modal show={showModal} onHide={handleCloseModal}>
                <Modal.Header closeButton>
                    <Modal.Title>Create RSO Event</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Form onSubmit={handleCreateRsoEvent}>
                        <FormGroup>
                            <FormLabel>RSO</FormLabel>
                            <FormControl
                                as="select"
                                value={selectedRsoId}
                                onChange={(e) => setSelectedRsoId(e.target.value)}
                                required
                            >
                                <option value="">Select an RSO</option>
                                {rsoList.map((rso) => (
                                    <option key={rso.id} value={rso.id}>
                                        {rso.name}
                                    </option>
                                ))}
                            </FormControl>
                        </FormGroup>
                        <FormGroup>
                            <FormLabel>Event Name</FormLabel>
                            <FormControl
                                type="text"
                                value={eventName}
                                onChange={(e) => setEventName(e.target.value)}
                                required
                            />
                        </FormGroup>
                        <FormGroup>
                            <FormLabel>Event Description</FormLabel>
                            <FormControl
                                type="text"
                                value={eventDescription}
                                onChange={(e) => setEventDescription(e.target.value)}
                                required
                            />
                        </FormGroup>
                        <FormGroup>
                            <FormLabel>Event Category</FormLabel>
                            <FormControl
                                as="select"
                                value={eventCategory}
                                onChange={(e) => setEventCategory(e.target.value)}
                                required
                            >
                                <option value="">Select a category</option>
                                <option value="academic">Academic</option>
                                <option value="arts">Arts</option>
                                <option value="career">Career</option>
                                <option value="performance">Performance</option>
                                <option value="entertainment">Entertainment</option>
                                <option value="health">Health</option>
                                <option value="holiday">Holiday</option>
                                <option value="meeting">Meeting</option>
                                <option value="forum">Forum</option>
                                <option value="recreation">Recreation</option>
                                <option value="service">Service</option>
                                <option value="social">Social</option>
                                <option value="speaker">Speaker</option>
                                <option value="sports">Sports</option>
                                <option value="tour">Tour</option>
                                <option value="other">Other</option>
                                <option value="workshop">Workshop</option>
                            </FormControl>
                        </FormGroup>
                        <Button variant="primary" type="submit">
                            Create RSO Event
                        </Button>
                    </Form>
                </Modal.Body>
            </Modal>
            <Modal show={showPublicEventModal} onHide={handleClosePublicEventModal}>
                <Modal.Header closeButton>
                    <Modal.Title>Create Public Event</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <CreatePublicEvent />
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={handleClosePublicEventModal}>
                        Close
                    </Button>
                </Modal.Footer>
            </Modal>

        </div>
    );
};

export default Admin;    