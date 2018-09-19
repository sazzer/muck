// @flow

import type {StoreAccessTokenAction} from "../accessToken";
import * as testSubject from '../accessToken';
import {storeAccessToken} from '../accessToken';

const now = new Date();
const accessToken = {
    bearerToken: 'someToken',
    expires: now
};

describe('storeAccessToken', () => {
    it('Produces the correct action', () => {
        expect(storeAccessToken('someToken', now)).toEqual({
            type: 'AUTH/STORE_ACCESS_TOKEN_ACTION',
            payload: accessToken
        })
    })
});

describe('storeAccessTokenMutation', () => {
    it('Correctly updates the state', () => {
        const state = {
            ...testSubject.module.initialState
        };
        const action: StoreAccessTokenAction = {
            type: '',
            payload: accessToken
        };
        testSubject.storeAccessTokenMutation(state, action);

        expect(state).toEqual({accessToken});
    });
});

describe('selectAccessToken', () => {
    it('Returns undefined from an empty state', () => {
        const state = {
            ...testSubject.module.initialState
        };
        expect(testSubject.selectAccessToken(state)).toBeUndefined();
    });
    it('Returns the access token from the state', () => {
        const state = {
            ...testSubject.module.initialState,
            accessToken
        };
        expect(testSubject.selectAccessToken(state)).toBe(accessToken);
    });
});

describe('selectHasAccessToken', () => {
    it('Returns false from an empty state', () => {
        const state = {
            ...testSubject.module.initialState
        };
        expect(testSubject.selectHasAccessToken(state)).toBeFalsy();
    });
    it('Returns true from a populated state', () => {
        const state = {
            ...testSubject.module.initialState,
            accessToken
        };
        expect(testSubject.selectHasAccessToken(state)).toBeTruthy();
    });
});
