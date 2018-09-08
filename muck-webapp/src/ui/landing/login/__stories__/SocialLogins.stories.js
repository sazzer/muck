// @flow

import React from 'react';
import {storiesOf} from '@storybook/react';
import SocialLogins from '../SocialLogins';

storiesOf('LandingPage/Login/SocialLogins', module)
    .addWithJSX('Google', () => {

        return (
            <SocialLogins buttons={['google']}/>
        );
    }, {
        showFunctions: false
    })
;
