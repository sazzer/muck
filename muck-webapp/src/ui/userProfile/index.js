// @flow

import React from 'react';
import {UserProfileScreen} from "./UserProfileScreen";

const user = {
    links: {
        self: ''
    },
    data: {
        displayName: 'Graham',
        email: 'graham@example.com'
    },
    logins: [
        {
            provider: 'google',
            providerId: '123',
            displayName: 'graham@example.com'
        }, {
            provider: 'twitter',
            providerId: '@grahamexample',
            displayName: '@grahamexample'
        }
    ]
};

/**
 * Representation of the User Profile screen
 */
export default function UserProfile() {
    return (
        <UserProfileScreen user={user}/>
    );
}
