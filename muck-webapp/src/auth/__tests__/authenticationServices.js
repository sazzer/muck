// @flow

import { put, call } from 'redux-saga/effects';
import * as testSubject from '../authenticationServices';
import type {StoreAuthenticationServicesAction} from "../authenticationServices";
import * as api from '../../api';

describe('selectAuthenticationServices', () => {
    describe('With the initial state', () => {
        const state = {
            ...testSubject.module.initialState
        };

        it('Returns no authentication services', () => {
            expect(testSubject.selectAuthenticationServices(state)).toEqual([]);
        });
    });

    describe('With some services loaded', () => {
        const state = {
            ...testSubject.module.initialState,
            services: [
                'google',
                'twitter'
            ]
        };

        it('Returns the correct authentication services', () => {
            expect(testSubject.selectAuthenticationServices(state)).toEqual(['google', 'twitter']);
        });
    });
});

describe('storeAuthenticationServicesMutation', () => {
    it('Correctly updates the state', () => {
        const state = {
            ...testSubject.module.initialState
        };
        const action: StoreAuthenticationServicesAction = {
            type: '',
            payload: {
                services: [
                    'google'
                ]
            }
        };
        testSubject.storeAuthenticationServicesMutation(state, action);

        expect(state).toEqual({
            services: [
                'google'
            ]
        });
    });
});

describe('loadAuthenticationServicesSaga', () => {
    describe('When Auth Services are available', () => {
        const gen = testSubject.loadAuthenticationServicesSaga();

        it('Yields to load the External Authentication Services resource', () => {
            const loadResourceYield = gen.next();

            expect(loadResourceYield.done).toBeFalsy();
            expect(loadResourceYield.value).toEqual(call(api.default, {url: '/authentication/external'}));
        });

        it('Yields to store the Authentication Services', () => {
            const loadResourceYield = gen.next({
                data: {
                    services: [
                        'google',
                        'twitter'
                    ]
                }
            });

            expect(loadResourceYield.done).toBeFalsy();
            expect(loadResourceYield.value).toEqual(put({
                type: 'AUTH/STORE_AUTHENTICATION_SERVICES_ACTION',
                payload: {
                    services: [
                        'google',
                        'twitter'
                    ]
                }
            }));
        });

        it('Yields that we\'ve finished', () => {
            const yieldResult = gen.next();
            expect(yieldResult.done).toBeTruthy();
            expect(yieldResult.value).toBeUndefined();
        });
    });
    describe('When No Auth Services are available', () => {
        const gen = testSubject.loadAuthenticationServicesSaga();

        it('Yields to load the External Authentication Services resource', () => {
            const loadResourceYield = gen.next();

            expect(loadResourceYield.done).toBeFalsy();
            expect(loadResourceYield.value).toEqual(call(api.default, {url: '/authentication/external'}));
        });

        it('Yields to store the Authentication Services', () => {
            const loadResourceYield = gen.next({
                data: {
                    services: []
                }
            });

            expect(loadResourceYield.done).toBeFalsy();
            expect(loadResourceYield.value).toEqual(put({
                type: 'AUTH/STORE_AUTHENTICATION_SERVICES_ACTION',
                payload: {
                    services: []
                }
            }));
        });

        it('Yields that we\'ve finished', () => {
            const yieldResult = gen.next();
            expect(yieldResult.done).toBeTruthy();
            expect(yieldResult.value).toBeUndefined();
        });
    });
});
