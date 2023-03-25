import React, { useState, useEffect } from 'react';
import '../custom.css';
import '../App.css';
import config from '../Config';
import { useNavigate } from "react-router-dom";
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

const LoginPage = () => {
    const navigate = useNavigate();
    const [SignUpModal, setSignUpModal] = useState(false);
    const [loginModal, setLoginModal] = useState(false);
    const onHideSignUp = () => setSignUpModal(!SignUpModal);
    const onHideLogin = () => setLoginModal(!loginModal);
    const [universities, setUniversities] = useState([]);
    const [selectedUniversity, setSelectedUniversity] = useState(null);

    useEffect(() => {
        fetchUniversities();
    }, []);

    const fetchUniversities = async () => {
        try {
            const response = await axios.get(`${config.url}/api/university/all`);
            setUniversities(response.data);
        } catch (error) {
            console.error("Fetch universities error:", error);
        }
    };


    const handleSubmit = async (e) => {
        let result = document.getElementById("loginResult")
        e.preventDefault();
        const data = {
            user: {
                firstName: e.target.firstName.value,
                lastName: e.target.lastName.value,
                email: e.target.email.value,
                password: e.target.password.value,
            },
            university: { id: selectedUniversity },
        };

        // Make the API request to backend to create a new user (update the URL accordingly)
        try {
            const response = await axios.post(`${config.url}/api/user/register`, data);
            console.log(response.data);
            if (response.status === 201) {
                onHideSignUp(); // Close the modal if API returns 200
                onHideLogin();
            }
        } catch (error) {
            console.error('Error:', error);
            result.innerHTML = error.response.data;
        }
    };

    const handleLogin = async (e) => {
        let result = document.getElementById("loginResult")
        e.preventDefault();
        const data = {
            username: e.target.loginEmail.value,
            password: e.target.loginPassword.value,
        };


        // Make the API request to backend to log in the user (update the URL accordingly)
        try {
            const response = await axios.post(`${config.url}/api/login`, data);
            console.log(response.data);
            if (response.status === 200) {
                localStorage.setItem("token", response.data.token);
                // Check the user's roles in the API response
                if (response.data.roles.includes("ROLE_STUDENT")) {
                    // If the user is a student, redirect to the HomePage
                    navigate("/home");
                } else if (response.data.roles.includes("ROLE_SUPER_ADMIN")) {
                    // If the user is a super admin, redirect to the admin page
                    navigate("/admin");
                } else {
                    // If the user has a different role, handle it appropriately
                }
                onHideLogin(); // Close the modal if API returns 200
            }
        } catch (error) {   
            result.innerHTML = error;
            console.error('Error:', error);
        }
    };

    return (
        <div className="App">
            <div className="App-header">
                <h2 id="Title">VUDB</h2>
            </div>
            <div id='loginResultDiv'>
                <h1 id='loginResult'></h1>
            </div>

            <div className="App-body">
                {/*Sign up Button*/}
                <Button className="custom-button" color="primary" onClick={onHideSignUp}>
                    Sign Up
                </Button>
                {/* Login in Button*/}
                <Button className="custom-button login-button" color="primary" onClick={onHideLogin}>
                    Log In
                </Button>


                {/*Modal for login, contains form elements login, password, modal for forgot password */}
                <Modal show={loginModal} onHide={onHideLogin}>
                    <Form onSubmit={handleLogin}>
                        <ModalHeader onHide={onHideLogin}>Log In</ModalHeader>
                        <ModalBody>
                            <FormGroup>
                                <FormLabel htmlFor="loginEmail">Email</FormLabel>
                                <FormControl type="email" name="loginEmail" id="loginEmail" required />
                            </FormGroup>
                            <FormGroup>
                                <FormLabel htmlFor="loginPassword">Password</FormLabel>
                                <FormControl type="password" name="loginPassword" id="loginPassword" required />
                            </FormGroup>
                        </ModalBody>
                        <ModalFooter>
                            <Button color="primary" type="submit">
                                Log In
                            </Button>
                            <Button color="secondary" onClick={onHideLogin}>
                                Cancel
                            </Button>
                        </ModalFooter>
                    </Form>
                </Modal>


                {/*Modal for sign up */}
                <Modal show={SignUpModal} onHide={onHideSignUp}>
                    <Form onSubmit={handleSubmit}>
                        <ModalHeader onHide={onHideSignUp}>Sign Up</ModalHeader>
                        <ModalBody>

                            {/*Contains form elements first name, last name, email, password */}
                            <FormGroup>
                                <FormLabel htmlFor="firstName">First Name</FormLabel>
                                <FormControl type="text" name="firstName" id="firstName" required />
                            </FormGroup>

                            <FormGroup>
                                <FormLabel htmlFor="lastName">Last Name</FormLabel>
                                <FormControl type="text" name="lastName" id="lastName" required />
                            </FormGroup>

                            <FormGroup>
                                <FormLabel htmlFor="email">Email</FormLabel>
                                <FormControl type="email" name="email" id="email" required placeholder='example@test.com' />
                            </FormGroup>

                            <FormGroup>
                                <FormLabel htmlFor="password">Password</FormLabel>
                                <FormControl type="password" name="password" id="password" required />
                            </FormGroup>
                            <FormGroup>
                                <FormLabel htmlFor="university">University</FormLabel>
                                <FormControl
                                    as="select"
                                    name="university"
                                    id="university"
                                    value={selectedUniversity || ''}
                                    onChange={(e) => setSelectedUniversity(e.target.value)}
                                    required
                                >
                                    <option value="">Select a university</option>
                                    {universities.map((university) => (
                                        <option key={university.id} value={university.id}>
                                            {university.name}
                                        </option>
                                    ))}
                                </FormControl>
                            </FormGroup>


                        </ModalBody>


                        <ModalFooter>
                            <Button color="primary" type="submit">
                                Sign Up
                            </Button>

                            <Button color="secondary" onClick={onHideSignUp}>
                                Cancel
                            </Button>
                        </ModalFooter>
                    </Form>
                </Modal>

            </div>
        </div>
    );
};

export default LoginPage;