// @flow

import React from 'react';
import {Interpolate} from "react-i18next";
import {Dropdown} from "semantic-ui-react";
import {Link} from "react-router-dom";
import type {UserProfile} from "../../users/userProfiles";
import type {UsersModule} from "../../users";
import {module as usersModule} from "../../users";
import {connectStore} from "redux-box";

/** Shape of the Props for the User Menu */
export type UserMenuProps = {
    user: UserProfile
}

/**
 * The User Menu for user related actions
 */
export function UserMenu({user}: UserMenuProps) {
    return (
        <Dropdown item text={user.data.displayName}>
            <Dropdown.Menu>
                <Link to='/userProfile'>
                    <Dropdown.Item>
                        <Interpolate i18nKey='page.header.user.menu.profile' />
                    </Dropdown.Item>
                </Link>
                <Dropdown.Divider />
                <Dropdown.Item>
                    <Interpolate i18nKey='page.header.user.menu.logout' />
                </Dropdown.Item>
            </Dropdown.Menu>
        </Dropdown>
    );
}

/**
 * Wrapper around the User Menu component that deals with the Redux Store
 */
function ConnectedUserMenu({users}: {users: UsersModule}) {
    const currentUserId = users.selectCurrentUserId();
    const currentUser = currentUserId && users.selectUserById(currentUserId);
    if (currentUser) {
        return <UserMenu user={currentUser} />
    } else {
        return null;
    }
}

/**
 * Wrapper around the Connected User Menu component that actually provides the Redux store
 */
export default connectStore({users: usersModule})(ConnectedUserMenu);
