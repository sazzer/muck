// @flow

import React from 'react';
import {UserProfileScreen} from "./UserProfileScreen";
import LoggedIn from "../LoggedIn";

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
        <LoggedIn>
            <UserProfileScreen user={user}/>
        </LoggedIn>
    );
}
