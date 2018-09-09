// @flow

import React from 'react';
import {connectStore} from 'redux-box';
import {Button, Icon} from "semantic-ui-react";
import {Interpolate} from "react-i18next";
import {module as authModule} from '../../../auth';
import type {AuthModule} from "../../../auth";

/** The colours to use for the buttons */
const BUTTON_COLOURS = {
    'google': 'google plus'
};

type SocialLoginsProps = {
    buttons: Array<string>
}

/**
 * React component to display the options for logging in using social login providers
 * @constructor
 */
export function SocialLogins({buttons}: SocialLoginsProps) {
    const buttonMarkup = buttons.map(button => (
        <Button color={BUTTON_COLOURS[button]} fluid key={button}>
            <Icon name={button} />
            <Interpolate i18nKey={`page.landing.login.socialLogins.${button}`} />
        </Button>
    ));

    return (
        <Button.Group vertical>
            { buttonMarkup }
        </Button.Group>
    );
}

/**
 * Wrapper around the Social Logins component that deals with the Redux Store
 */
class ConnectedSocialLogins extends React.Component<{auth: AuthModule}> {
    componentDidMount() {
        this.props.auth.loadAuthenticationServices();
    }

    render() {
        return <SocialLogins buttons={this.props.auth.selectAuthenticationServices()} />
    }
}

/**
 * Wrapper around the Connected Social Logins component that actually provides the Redux store
 */
export default connectStore({auth: authModule})(ConnectedSocialLogins);
