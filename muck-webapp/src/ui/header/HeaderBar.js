// @flow

import React from 'react';
import {Menu} from 'semantic-ui-react';
import {Interpolate} from 'react-i18next';
import {Link} from 'react-router-dom';
import EncyclopediaMenu from './EncyclopediaMenu';
import UserMenu from './UserMenu';

/**
 * Representation of the header bar for the application
 */
export default function HeaderBar() {
    return (
        <Menu attached='top' borderless data-test="header-bar">
            <Menu.Item header>
                <Link to="/">
                    <Interpolate i18nKey='page.title' />
                </Link>
            </Menu.Item>
            <Menu.Menu position='right'>
                <EncyclopediaMenu/>
                <UserMenu />
            </Menu.Menu>
        </Menu>
    );
}
