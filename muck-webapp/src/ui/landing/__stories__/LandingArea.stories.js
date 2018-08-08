// @flow

import React from 'react';
import {storiesOf} from '@storybook/react';
import LandingArea from '../LandingArea';

storiesOf('LandingPage/LandingArea', module)
    .addWithJSX('Default View', () => {

        return (
            <LandingArea/>
        );
    }, {
        showFunctions: false
    })
;
