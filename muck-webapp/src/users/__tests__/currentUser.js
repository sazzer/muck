// @flow

import type {StoreCurrentUserIdAction} from "../currentUser";
import * as testSubject from '../currentUser';
import {storeCurrentUser} from '../currentUser';

describe('storeCurrentUser', () => {
    it('Produces the correct action', () => {
        expect(storeCurrentUser('someUserId')).toEqual({
            type: 'USERS/STORE_CURRENT_USER_ACTION',
            payload: {
                userId: 'someUserId'
            }
        })
    })
});

describe('storeCurrentUserMutation', () => {
    it('Correctly updates the state', () => {
        const state = {
            ...testSubject.module.initialState
        };
        const action: StoreCurrentUserIdAction = {
            type: 'USERS/STORE_CURRENT_USER_ACTION',
            payload: {
                userId: 'someUserId'
            }
        };
        testSubject.storeCurrentUserMutation(state, action);

        expect(state).toEqual({
            currentUserId: 'someUserId'
        });
    });
});

describe('selectCurrentUserId', () => {
    describe('From an empty state', () => {
        const state = {
            ...testSubject.module.initialState
        };

        it('Returns nothing', () => {
            expect(testSubject.selectCurrentUserId(state)).toBeUndefined();
        });
    });
    describe('From a populated state', () => {
        const state = {
            currentUserId: 'userId'
        };

        it('Returns the correct value', () => {
            expect(testSubject.selectCurrentUserId(state)).toBe('userId');
        });
    });
});
