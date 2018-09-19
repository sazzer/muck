// @flow

import React from 'react';
import {Button, Form, Icon} from 'semantic-ui-react'
import {I18n, Interpolate} from 'react-i18next';
import type {UserData} from "../../users/userProfiles";

/**
 * The props for the User Profile Data area
 */
type UserProfileDataProps = {
    user: UserData
};

/**
 * The section of the User Profile screen for the main user data
 */
export default function UserProfileData({user}: UserProfileDataProps) {
    return (
        <I18n>
            {
                (t) => (
                    <Form>
                        <Form.Field required>
                            <label>
                                <Interpolate i18nKey="userProfile.data.displayName.label" />
                            </label>
                            <input placeholder={t('userProfile.data.displayName.placeholder')}
                                   value={user.displayName}
                                   autoFocus />
                        </Form.Field>
                        <Form.Field required>
                            <label>
                                <Interpolate i18nKey="userProfile.data.email.label" />
                            </label>
                            <input placeholder={t('userProfile.data.email.placeholder')}
                                   value={user.email} />
                        </Form.Field>
                        <Button primary labelPosition='left' icon type='submit'>
                            <Icon name='save outline' />
                            <Interpolate i18nKey="userProfile.data.buttons.save" />
                        </Button>
                    </Form>
                )
            }
        </I18n>
    )
}
