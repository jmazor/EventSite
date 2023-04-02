import React, { useState, useCallback } from "react";
import config from "../Config";
import {
  Button,
  Form,
  FormGroup,
  FormLabel,
  FormControl,
  ModalFooter
} from "react-bootstrap";
import "bootstrap/dist/css/bootstrap.min.css";
import {
  GoogleMap,
  useLoadScript,
  Marker,
} from "@react-google-maps/api";

const url = config.url;

const googleMapsApiKey = config.googleMapsApiKey;
console.log("key" + googleMapsApiKey)

const mapContainerStyle = {
  width: "100%",
  height: "400px",
};

const defaultCenter = {
  lat: 28.6024,
  lng: -81.2001, // UCF
};


function CreatePublicEvent({ onClose }) {

  const [name, setName] = useState("");
  const [category, setCategory] = useState("");
  const [description, setDescription] = useState("");
  const [startDate, setStartDate] = useState("");
  const [endDate, setEndDate] = useState("");
  const [locationName, setLocationName] = useState("");
  const [locationURL, setLocationURL] = useState("");
  const [phone, setPhone] = useState("");
  const [email, setEmail] = useState("");
  const [markerPosition, setMarkerPosition] = useState(null);
  const [locationError, setLocationError] = useState(false);


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

  const handleSubmit = async (event) => {
    event.preventDefault();

    const token = localStorage.getItem("token");
    if (!markerPosition) {
      setLocationError(true);
      return;
    } else {
      setLocationError(false);
    }

    try {
      const response = await fetch(`${url}/api/event/create/public`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify({
          name,
          category: category.toUpperCase(),
          description,
          startDate: startDate,
          endDate: endDate,
          locationName: locationName,
          locationUrl: locationURL,
          phone,
          email,
        }),
      });
      const data = await response.json();
      console.log("CreatePublicEvent response:", data);
      // Handle the successful response here
    } catch (error) {
      console.error("CreatePublicEvent error:", error);
      // Handle the error here
    }
  };

  if (loadError) return "Error loading maps";
  if (!isLoaded) return "Loading Maps";


  return (
    <Form onSubmit={handleSubmit}>
      <FormGroup>
        <FormLabel>Event Name:</FormLabel>
        <FormControl
          type="text"
          value={name}
          onChange={(e) => setName(e.target.value)}
          required
        />
      </FormGroup>
      <FormGroup>
        <FormLabel>Category:</FormLabel>
        <FormControl
          as="select"
          value={category}
          onChange={(e) => setCategory(e.target.value)}
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
          value={description}
          onChange={(e) => setDescription(e.target.value)}
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
  <GoogleMap
    id="location-map"
    mapContainerStyle={mapContainerStyle}
    zoom={10}
    center={defaultCenter}
    onClick={onMapClick}
  >
    {markerPosition && <Marker position={markerPosition} />}
  </GoogleMap>
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
        <Button variant="secondary" style={{ marginLeft: "auto" }} onClick={onClose}>          Close
        </Button>
      </ModalFooter>

    </Form>

  );
}


export default CreatePublicEvent;