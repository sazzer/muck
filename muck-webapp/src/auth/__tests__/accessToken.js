// @flow

import type {StoreAccessTokenAction} from "../accessToken";
import * as testSubject from '../accessToken';
import {storeAccessToken} from '../accessToken';

describe('storeAccessToken', () => {
    it('Produces the correct action', () => {
        const now = new Date();
        expect(storeAccessToken('someToken', now)).toEqual({
            type: 'AUTH/STORE_ACCESS_TOKEN_ACTION',
            payload: {
                bearerToken: 'someToken',
                expires: now
            }
        })
    })
});

describe('storeAccessTokenMutation', () => {
    it('Correctly updates the state', () => {
        const state = {
            ...testSubject.module.initialState
        };
        const now = new Date();
        const action: StoreAccessTokenAction = {
            type: '',
            payload: {
                bearerToken: 'someToken',
                expires: now
            }
        };
        testSubject.storeAccessTokenMutation(state, action);

        expect(state).toEqual({
            accessToken: {
                bearerToken: 'someToken',
                expires: now
            }
        });
    });
});
