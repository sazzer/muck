// @flow

import React from 'react';
import {Header, Image} from 'semantic-ui-react';
import {Interpolate} from 'react-i18next';

/** The Base URL to the Public files */
const publicUrl = process.env.PUBLIC_URL || '';

/**
 * React component to represent the Landing Page
 */
export default function LandingArea() {
    return (
        <div>
            <Header dividing as="h3">
                <Interpolate i18nKey="page.landing.title" />
            </Header>
            <Image src={`${publicUrl}/landingPage.jpg`} fluid />
            <p>
                <Interpolate i18nKey="page.landing.blurb" />
            </p>
        </div>
    )
}
