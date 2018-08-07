// @flow

import React from 'react';
import {Menu} from 'semantic-ui-react';
import {Interpolate} from 'react-i18next';

/**
 * Representation of the header bar for the application
 */
export default function HeaderBar() {
    return (
        <Menu attached='top' borderless>
            <Menu.Item header>
                <Interpolate i18nKey='page.title' />
            </Menu.Item>
            <Menu.Item position='right'>
            </Menu.Item>
        </Menu>
    );
}
