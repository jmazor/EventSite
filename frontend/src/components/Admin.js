// Admin.js
import {
    GoogleMap,
    Marker,
    useLoadScript,
} from "@react-google-maps/api";
import axios from "axios";
import "bootstrap/dist/css/bootstrap.min.css";
import React, { useCallback, useEffect, useState, useMemo } from "react";
import {
    Button,
    Form,
    FormControl,
    FormGroup,
    FormLabel,
    Modal,
    ModalFooter
} from "react-bootstrap";
import config from "../Config";
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
    const [startDate, setStartDate] = useState("");
    const [endDate, setEndDate] = useState("");
    const [locationName, setLocationName] = useState("");
    const [locationURL, setLocationURL] = useState("");
    const [phone, setPhone] = useState("");
    const [email, setEmail] = useState("");
    const [markerPosition, setMarkerPosition] = useState(null);
    const [locationError, setLocationError] = useState(false);


    const url = config.url;

    const handleCloseModal = () => {
        setShowModal(false);
        setMarkerPosition(null); // Reset the marker position
      };
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

    const googleMapsApiKey = config.googleMapsApiKey;


    const mapContainerStyle = {
        width: "100%",
        height: "400px",
    };

    const defaultCenter = {
        lat: 28.6024,
        lng: -81.2001,
    };

    const { isLoaded, loadError } = useLoadScript({
        googleMapsApiKey,
        libraries: [],
    });

    const onMapClick = useCallback((e) => {
        setMarkerPosition({
            lat: e.latLng.lat(),
            lng: e.latLng.lng(),
        });
        setLocationURL(`https://www.google.com/maps?q=${e.latLng.lat()},${e.latLng.lng()}`);
    }, []);

    const MemoizedMap = useMemo(() => {
        return (
            <GoogleMap
                id="location-map"
                mapContainerStyle={mapContainerStyle}
                zoom={10}
                center={defaultCenter}
                onClick={onMapClick}
            >
                {markerPosition && <Marker position={markerPosition} />}
            </GoogleMap>
        );
    }, [onMapClick, markerPosition, showModal]);


    const handleCreateRsoEvent = async (e) => {
        e.preventDefault();

        if (!markerPosition) {
            setLocationError(true);
            return;
        } else {
            setLocationError(false);
        }

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
                    startDate: startDate,
                    endDate: endDate,
                    locationName: locationName,
                    locationUrl: locationURL,
                    phone,
                    email,
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
                            <FormLabel>Event Name:</FormLabel>
                            <FormControl
                                type="text"
                                value={eventName}
                                onChange={(e) => setEventName(e.target.value)}
                                required
                            />
                        </FormGroup>
                        <FormGroup>
                            <FormLabel>Category:</FormLabel>
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
                        <FormGroup>
                            <FormLabel>Description:</FormLabel>
                            <FormControl
                                as="textarea"
                                value={eventDescription}
                                onChange={(e) => setEventDescription(e.target.value)}
                                required
                            />
                        </FormGroup>
                        <FormGroup>
                            <FormLabel>Start Date and Time:</FormLabel>
                            <FormControl
                                type="datetime-local"
                                value={startDate}
                                onChange={(e) => setStartDate(e.target.value)}
                                required
                            />
                        </FormGroup>
                        <FormGroup>
                            <FormLabel>End Date and Time:</FormLabel>
                            <FormControl
                                type="datetime-local"
                                value={endDate}
                                onChange={(e) => setEndDate(e.target.value)}
                                required
                            />
                        </FormGroup>
                        <FormGroup>
                            <FormLabel>Location Name:</FormLabel>
                            <FormControl
                                type="text"
                                value={locationName}
                                onChange={(e) => setLocationName(e.target.value)}
                                required
                            />
                        </FormGroup>
                        <FormGroup>
                            <FormLabel>Event Location:</FormLabel>
                            {MemoizedMap}
                            {locationError && (
                                <div className="text-danger">
                                    Please select a location on the map.
                                </div>
                            )}
                            {locationError && (
                                <div className="text-danger">
                                    Please select a location on the map.
                                </div>
                            )}
                        </FormGroup>
                        <FormGroup>
                            <FormLabel>Phone:</FormLabel>
                            <FormControl
                                type="text"
                                value={phone}
                                onChange={(e) => setPhone(e.target.value)}
                                required
                            />
                        </FormGroup>
                        <FormGroup>
                            <FormLabel>Email:</FormLabel>
                            <FormControl
                                type="email"
                                value={email}
                                onChange={(e) => setEmail(e.target.value)}
                                required
                            />
                        </FormGroup>
                        <ModalFooter style={{ display: "flex" }}>
                            <Button variant="primary" type="submit" style={{ marginRight: "auto" }}>
                                Create
                            </Button>
                            <Button variant="secondary" style={{ marginLeft: "auto" }} onClick={handleCloseModal}>          Close
                            </Button>
                        </ModalFooter>

                    </Form>
                </Modal.Body>
            </Modal>
            <Modal show={showPublicEventModal} onHide={handleClosePublicEventModal}>
                <Modal.Header closeButton>
                    <Modal.Title>Create Public Event</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <CreatePublicEvent onClose={handleClosePublicEventModal} />
                </Modal.Body>
            </Modal>
        </div>
    );
};

export default Admin;    