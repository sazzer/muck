// @flow

import React from 'react';
import {Button, Icon} from "semantic-ui-react";
import {Interpolate} from "react-i18next";

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
export default function SocialLogins({buttons}: SocialLoginsProps) {
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
