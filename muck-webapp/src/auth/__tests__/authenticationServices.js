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
                {
                    name: 'google',
                    href: '/google'
                }, {
                    name: 'twitter',
                    href: '/twitter'
                }
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
                    {
                        name: 'google',
                        href: '/google'
                    }
                ]
            }
        };
        testSubject.storeAuthenticationServicesMutation(state, action);

        expect(state).toEqual({
            services: [
                {
                    name: 'google',
                    href: '/google'
                }
            ]
        });
    });
});

describe('loadAuthenticationServicesSaga', () => {
    describe('When The Auth Services endpoint is not available', () => {
        const gen = testSubject.loadAuthenticationServicesSaga();

        it('Yields to get the API Root', () => {
            const getApiRootYield = gen.next();
            expect(getApiRootYield.done).toBeFalsy();
            expect(getApiRootYield.value).toEqual(call(api.getApiRoot));
        });

        it('Yields that we\'ve finished', () => {
            const loadResourceResponse = {
                getLink: jest.fn()
            };
            loadResourceResponse.getLink.mockReturnValueOnce(undefined);

            const loadResourceYield = gen.next(loadResourceResponse);

            expect(loadResourceResponse.getLink).toHaveBeenCalledTimes(1);
            expect(loadResourceResponse.getLink).toBeCalledWith('externalAuthenticationServices');

            expect(loadResourceYield.done).toBeTruthy();
            expect(loadResourceYield.value).toBeUndefined();
        });
    });
    describe('When Auth Services are available', () => {
        const gen = testSubject.loadAuthenticationServicesSaga();

        it('Yields to get the API Root', () => {
            const getApiRootYield = gen.next();
            expect(getApiRootYield.done).toBeFalsy();
            expect(getApiRootYield.value).toEqual(call(api.getApiRoot));
        });

        it('Yields to load the External Authentication Services resource', () => {
            const loadResourceResponse = {
                getLink: jest.fn()
            };
            loadResourceResponse.getLink.mockReturnValueOnce({
                'href': '/externalAuthServices'
            });

            const loadResourceYield = gen.next(loadResourceResponse);

            expect(loadResourceResponse.getLink).toHaveBeenCalledTimes(1);
            expect(loadResourceResponse.getLink).toBeCalledWith('externalAuthenticationServices');

            expect(loadResourceYield.done).toBeFalsy();
            expect(loadResourceYield.value).toEqual(call(api.loadResource, '/externalAuthServices'));
        });

        it('Yields to store the Authenticaiton Services', () => {
            const loadResourceResponse = {
                getLinks: jest.fn()
            };
            loadResourceResponse.getLinks.mockReturnValueOnce([
                {
                    name: 'google',
                    href: '/google'
                }, {
                    name: 'twitter',
                    href: '/twitter'
                }
            ]);
            const loadResourceYield = gen.next(loadResourceResponse);

            expect(loadResourceResponse.getLinks).toHaveBeenCalledTimes(1);
            expect(loadResourceResponse.getLinks).toBeCalledWith('services');

            expect(loadResourceYield.done).toBeFalsy();
            expect(loadResourceYield.value).toEqual(put({
                type: 'AUTH/STORE_AUTHENTICATION_SERVICES_ACTION',
                payload: {
                    services: [
                        {
                            name: 'google',
                            href: '/google'
                        }, {
                            name: 'twitter',
                            href: '/twitter'
                        }
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

        it('Yields to get the API Root', () => {
            const getApiRootYield = gen.next();
            expect(getApiRootYield.done).toBeFalsy();
            expect(getApiRootYield.value).toEqual(call(api.getApiRoot));
        });

        it('Yields to load the External Authentication Services resource', () => {
            const loadResourceResponse = {
                getLink: jest.fn()
            };
            loadResourceResponse.getLink.mockReturnValueOnce({
                'href': '/externalAuthServices'
            });

            const loadResourceYield = gen.next(loadResourceResponse);

            expect(loadResourceResponse.getLink).toHaveBeenCalledTimes(1);
            expect(loadResourceResponse.getLink).toBeCalledWith('externalAuthenticationServices');

            expect(loadResourceYield.done).toBeFalsy();
            expect(loadResourceYield.value).toEqual(call(api.loadResource, '/externalAuthServices'));
        });

        it('Yields to store the Authenticaiton Services', () => {
            const loadResourceResponse = {
                getLinks: jest.fn()
            };
            loadResourceResponse.getLinks.mockReturnValueOnce([]);
            const loadResourceYield = gen.next(loadResourceResponse);

            expect(loadResourceResponse.getLinks).toHaveBeenCalledTimes(1);
            expect(loadResourceResponse.getLinks).toBeCalledWith('services');

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
