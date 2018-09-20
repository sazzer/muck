// @flow

import React from 'react';
import UserProfileScreen from "./UserProfileScreen";
import LoggedIn from "../LoggedIn";

/**
 * Representation of the User Profile screen
 */
export default function UserProfile() {
    return (
        <LoggedIn>
            <UserProfileScreen />
        </LoggedIn>
    );
}
