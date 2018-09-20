// @flow

import React from 'react';
import {Container, Tab} from 'semantic-ui-react';
import {I18n} from 'react-i18next';
import {connectStore} from "redux-box";
import type {UserProfile} from "../../users/userProfiles";
import UserProfileHeader from "./UserProfileHeader";
import UserProfileData from "./UserProfileData";
import UserProfileAccounts from './UserProfileAccounts';
import type {UsersModule} from "../../users";
import {module as usersModule} from "../../users";

/**
 * The props for the User Profile Screen
 */
type UserProfileScreenProps = {
    user: UserProfile
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
                                render: () => <UserProfileData user={this.props.user.data} />
                            },
                            {
                                menuItem: t('userProfile.logins.header'),
                                render: () => <UserProfileAccounts logins={this.props.user.logins} />
                            }
                        ];

                        return (
                            <Container>
                                <UserProfileHeader user={this.props.user} />
                                <Tab menu={{fluid: true, vertical: true, tabular: true}} panes={panes} />
                            </Container>
                        );
                    }
                }
            </I18n>
        );
    }
}
/**
 * Wrapper around the User Profile Screen component that deals with the Redux Store
 */
function ConnectedUserProfileScreen({users}: {users: UsersModule}) {
    const currentUserId = users.selectCurrentUserId();
    const currentUser = currentUserId && users.selectUserById(currentUserId);
    if (currentUser) {
        return <UserProfileScreen user={currentUser} />
    } else {
        return null;
    }
}

/**
 * Wrapper around the Connected User Profile Screen component that actually provides the Redux store
 */
export default connectStore({users: usersModule})(ConnectedUserProfileScreen);
