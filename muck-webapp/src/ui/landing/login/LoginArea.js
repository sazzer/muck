// @flow

import React from 'react';
import {Header} from "semantic-ui-react";
import {Interpolate} from "react-i18next";
import SocialLogins from './SocialLogins';

/**
 * React component to display the options for logging in
 * @constructor
 */
export default function LoginArea() {
    return (
        <div>
            <Header dividing as="h3">
                <Interpolate i18nKey="page.landing.login.title" />
            </Header>
            <SocialLogins buttons={['google']}/>
        </div>
    )
}
