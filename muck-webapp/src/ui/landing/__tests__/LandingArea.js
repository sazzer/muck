// @flow

import React from 'react';
import {shallow} from 'enzyme';
import LandingArea from '../LandingArea';

/** Create the component to test */
function setup() {
    return {
        element: shallow(<LandingArea />)
    };
}

describe('LandingArea', () => {
    it('Renders correctly', () => {
        const { element } = setup();

        expect(element).toMatchSnapshot();
    });
});
