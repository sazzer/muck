// @flow

import React from 'react';
import {Container, Tab} from 'semantic-ui-react';
import {I18n} from 'react-i18next';
import {connectStore} from "redux-box";
import type {UserData, UserLogin, UserProfile} from "../../users/userProfiles";
import UserProfileHeader from "./UserProfileHeader";
import UserProfileData from "./UserProfileData";
import UserProfileAccounts from './UserProfileAccounts';
import type {UsersModule} from "../../users";
import {module as usersModule} from "../../users";
import type {Errors} from "../../errors";

/**
 * The props for the User Profile Screen
 */
type UserProfileScreenProps = {
    user: UserProfile,
    updateUserProfile: (UserData, Array<UserLogin>, (Errors | null) => void) => void
};

/**
 * Component to represent the User Profile Screen
 */
export class UserProfileScreen extends React.Component<UserProfileScreenProps> {
    render() {
        return (
            <I18n>
                {
                    (t) => {
                        const panes = [
                            {
                                menuItem: t('userProfile.data.header'),
                                render: () => <UserProfileData user={this.props.user.data} onUpdate={this._updateUserData} />
                            },
                            {
                                menuItem: t('userProfile.logins.header'),
                                render: () => <UserProfileAccounts logins={this.props.user.logins} />
                            }
                        ];

                        return (
                            <Container data-test='user-profile-page'>
                                <UserProfileHeader user={this.props.user} />
                                <Tab menu={{fluid: true, vertical: true, tabular: true}} panes={panes} />
                            </Container>
                        );
                    }
                }
            </I18n>
        );
    }

    /**
     * Handler for when the User Data is updated
     * @param data the new User Data
     * @param callback The callback for when the update has finished
     * @private
     */
    _updateUserData = (data: UserData, callback: (Errors | null) => void) => {
        this.props.updateUserProfile(
            data,
            this.props.user.logins,
            callback
        );
    }
}
/**
 * Wrapper around the User Profile Screen component that deals with the Redux Store
 */
function ConnectedUserProfileScreen({users}: {users: UsersModule}) {
    const currentUserId = users.selectCurrentUserId();
    const currentUser = currentUserId && users.selectUserById(currentUserId);
    if (currentUserId && currentUser) {
        const updateUserProfileHandler = function (userData: UserData, logins: Array<UserLogin>, callback: (Errors | null) => void) {
            return users.updateUserProfile(currentUserId, userData, logins, callback);
        };

        return <UserProfileScreen user={currentUser} updateUserProfile={updateUserProfileHandler} />
    } else {
        return null;
    }
}

/**
 * Wrapper around the Connected User Profile Screen component that actually provides the Redux store
 */
export default connectStore({users: usersModule})(ConnectedUserProfileScreen);
