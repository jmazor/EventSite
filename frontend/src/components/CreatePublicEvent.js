// components/CreatePublicEvent.js
import React, { useState } from "react";
import config from "../Config";

const url = config.url;

function CreatePublicEvent() {
    const [name, setName] = useState("");
    const [category, setCategory] = useState("");
    const [description, setDescription] = useState("");
    const [startDate, setStartDate] = useState("");
    const [endDate, setEndDate] = useState("");
    const [locationName, setLocationName] = useState("");
    const [locationURL, setLocationURL] = useState("");
    const [phone, setPhone] = useState("");
    const [email, setEmail] = useState("");

    const handleSubmit = async (event) => {
        event.preventDefault();

        const token = localStorage.getItem("token");

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
                    locationURL: locationURL,
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

    return (
        <form onSubmit={handleSubmit}>
            <label>
                Event Name:
                <input
                    type="text"
                    value={name}
                    onChange={(e) => setName(e.target.value)}
                />
            </label>
            <br />
            <label>
                Category:
                <select
                    value={category}
                    onChange={(e) => setCategory(e.target.value)}
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
                </select>
            </label>
            <br />
            <label>
                Description:
                <textarea
                    value={description}
                    onChange={(e) => setDescription(e.target.value)}
                />
            </label>
            <br />
            <label>
                Start Date and Time:
                <input
                    type="datetime-local"
                    value={startDate}
                    onChange={(e) => setStartDate(e.target.value)}
                />
            </label>
            <br />
            <label>
                End Date and Time:
                <input
                    type="datetime-local"
                    value={endDate}
                    onChange={(e) => setEndDate(e.target.value)}
                />
            </label>
            <br />
            <label>
                Location
                Name:
                <input
                    type="text"
                    value={locationName}
                    onChange={(e) => setLocationName(e.target.value)}
                />
            </label>
            <br />
            <label>
                Location URL:
                <input
                    type="text"
                    value={locationURL}
                    onChange={(e) => setLocationURL(e.target.value)}
                />
            </label>
            <br />
            <label>
                Phone:
                <input
                    type="text"
                    value={phone}
                    onChange={(e) => setPhone(e.target.value)}
                />
            </label>
            <br />
            <label>
                Email:
                <input
                    type="email"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                />
            </label>
            <br />
            <button type="submit">Create Public Event</button>
        </form>
    );
}

export default CreatePublicEvent;