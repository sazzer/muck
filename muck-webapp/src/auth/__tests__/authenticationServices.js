// @flow

import * as testSubject from '../authenticationServices';

describe('selectAuthenticationServices', () => {
    describe('With the initial state', () => {
        const state = testSubject.module.initialState;

        it('Returns no authentication services', () => {
            expect(testSubject.selectAuthenticationServices(state)).toEqual([]);
        });
    });

    describe('With some services loaded', () => {
        const state = {
            ...testSubject.module.initialState,
            services: [
                {
                    name: 'google',
                    uri: '/google'
                }, {
                    name: 'twitter',
                    uri: '/twitter'
                }
            ]
        };

        it('Returns the correct authentication services', () => {
            expect(testSubject.selectAuthenticationServices(state)).toEqual(['google', 'twitter']);
        });
    });
});
