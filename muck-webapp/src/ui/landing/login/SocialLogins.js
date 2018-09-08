// @flow

import React from 'react';
import {Button, Icon} from "semantic-ui-react";
import {Interpolate} from "react-i18next";

type SocialLoginsProps = {
    buttons: Array<string>
}

/**
 * React component to display the options for logging in using social login providers
 * @constructor
 */
export default function SocialLogins({buttons}: SocialLoginsProps) {
    const buttonMarkup = buttons.map(button => (
        <Button color={button} fluid>
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
