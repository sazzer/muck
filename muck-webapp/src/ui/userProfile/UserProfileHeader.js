// @flow

import {Header} from 'semantic-ui-react';
import type {UserProfile} from "../../users/userProfiles";
import React from "react";

/**
 * The props for the User Profile Header
 */
type UserProfileHeaderProps = {
    user: UserProfile
};

/**
 * The header of the User Profile area
 */
export default function UserProfileHeader({user}: UserProfileHeaderProps) {
    return (
        <Header dividing as="h3">
            { user.data.displayName }
        </Header>
    );
}
