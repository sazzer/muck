// @flow

import React from 'react';
import {storiesOf} from '@storybook/react';
import EncyclopediaMenu from '../EncyclopediaMenu';
import {Menu} from "semantic-ui-react";

storiesOf('Header/EncyclopediaMenu', module)
    .addWithJSX('EncyclopediaMenu', () => {

        return (
            <Menu attached='top' borderless>
                <Menu.Menu position='right'>
                    <EncyclopediaMenu/>
                </Menu.Menu>
            </Menu>
        );
    }, {
        showFunctions: false
    })
;
