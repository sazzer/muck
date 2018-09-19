// @flow

import React from 'react';
import {Container, Tab} from 'semantic-ui-react';
import {I18n} from 'react-i18next';
import type {UserProfile} from "../../users/userProfiles";
import UserProfileHeader from "./UserProfileHeader";
import UserProfileData from "./UserProfileData";
import UserProfileAccounts from './UserProfileAccounts';
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
