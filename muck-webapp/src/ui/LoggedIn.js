// @flow

import React from 'react';
import {connectStore} from "redux-box";
import {Header, Icon, Segment} from 'semantic-ui-react';
import {Interpolate} from 'react-i18next';
import type {AuthModule} from '../auth';
import {module as authModule} from '../auth';

/**
 * Component to show if the user is not currently logged in
 */
function MissingAccessToken() {
    return (
        <Segment basic>
            <Header as='h2' icon textAlign="center">
                <Icon name='lock' />
                <Interpolate i18nKey="page.notLoggedIn.header" />
                <Header.Subheader>
                    <Interpolate i18nKey="page.notLoggedIn.message" />
                </Header.Subheader>
            </Header>
        </Segment>
    );
}

/**
 * Wrapper around the a component to only render it if we've got an access token
 */
export function ConnectedHasAccessToken({auth, children}: {auth: AuthModule, children: any}) {
    return auth.selectHasAccessToken() ? children : <MissingAccessToken/>;
}

export default connectStore({auth: authModule})(ConnectedHasAccessToken);
