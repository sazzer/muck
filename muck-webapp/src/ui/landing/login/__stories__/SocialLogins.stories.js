// @flow

import React from 'react';
import {storiesOf} from '@storybook/react';
import {action} from '@storybook/addon-actions';
import {SocialLogins} from '../SocialLogins';

storiesOf('LandingPage/Login/SocialLogins', module)
    .addWithJSX('Google', () => {

        return (
            <SocialLogins buttons={['google']} startAuthenticationAction={action('startAuthentication')}/>
        );
    }, {
        showFunctions: false
    })
;
