// @flow

import React from 'react';
import {shallow} from 'enzyme';
import {ConnectedHasAccessToken} from '../LoggedIn';

/** Create the component to test */
function setup({hasAccessToken, children}) {
    const auth = {
        selectHasAccessToken: jest.fn(),

        // These are needed for Flow
        loadAuthenticationServices: jest.fn(),
        selectAccessToken: jest.fn(),
        selectAuthenticationServices: jest.fn(),
        startAuthentication: jest.fn()
    };

    auth.selectHasAccessToken.mockReturnValueOnce(hasAccessToken);

    return {
        auth,
        element: shallow(<ConnectedHasAccessToken auth={auth}>{ children }</ConnectedHasAccessToken>)
    };
}

describe('ConnectedHasAccessToken', () => {
    it('Renders correctly when the access token is missing', () => {
        const { element } = setup({
            hasAccessToken: false,
            children: "Missing"
        });

        expect(element).toMatchSnapshot();
    });
    it('Renders correctly when the access token is present', () => {
        const { element } = setup({
            hasAccessToken: true,
            children: "Present"
        });

        expect(element).toMatchSnapshot();
    });
});
