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
