import React from 'react';
import { Link } from 'react-router-dom';

function NavigationButtons() {
    return (
        <div>
            <Link to="/register">
                <button>Register</button>
            </Link>
            <Link to="/login">
                <button>Login</button>
            </Link>
            <Link to="/adminCreate">
                <button>Admin</button>
            </Link>
        </div>
    );
}

export default NavigationButtons;
