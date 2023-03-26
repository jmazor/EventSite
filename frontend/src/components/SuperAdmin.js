import React from 'react';
import { Link } from 'react-router-dom';

function SuperAdmin() {
    return (
        <div>
            <Link to="/createUniversity">
                <button>CreateUniversity</button>
            </Link>
            <Link to="/createPrivateEvent">
                <button>CreatePrivate</button>
            </Link>
            <Link to="/approveEvents">
                <button>ApproveEvent</button>
            </Link>
            <Link to="/createPublicEvent">
                <button>PublicEvent</button>
            </Link>
        </div>
    );
}

export default SuperAdmin;
