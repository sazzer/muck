// @flow

import React from 'react';
import {storiesOf} from '@storybook/react';
import LandingPage from '../LandingPage';

storiesOf('LandingPage/LandingPage', module)
    .addWithJSX('Default View', () => {

        return (
            <LandingPage/>
        );
    }, {
        showFunctions: false
    })
;
