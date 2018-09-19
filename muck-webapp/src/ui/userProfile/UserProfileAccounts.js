// @flow

import React from 'react';
import {List} from 'semantic-ui-react';
import {Interpolate} from 'react-i18next';
import type {UserLogin} from "../../users/userProfiles";

/**
 * The props for the User Profile Accounts area
 */
type UserProfileAccountsProps = {
    logins: Array<UserLogin>
};

/**
 * The section of the User Profile screen for rendering the login accounts
 */
export default function UserProfileAccounts({logins}: UserProfileAccountsProps) {
    const rows = logins.map((login) => (
        <List.Item>
            <List.Icon name={login.provider} />
            <List.Content>
                <List.Header>{ login.displayName }</List.Header>
                <List.Description>
                    <Interpolate i18nKey={`userProfile.logins.provider.${login.provider}`} />
                </List.Description>
            </List.Content>
        </List.Item>
    ));

    return (
        <List relaxed>
            { rows }
        </List>
    )
}
