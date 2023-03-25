import React from 'react';
import { Link } from 'react-router-dom';

function Admin() {
    return (
        <div>
            <Link to="/createUniversity">
                <button>CreateUniversity</button>
            </Link>
        </div>
    );
}

export default Admin;
