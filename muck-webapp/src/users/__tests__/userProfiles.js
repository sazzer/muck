// @flow

import * as testSubject from '../userProfiles';
import type {StoreUserProfileAction} from "../userProfiles";

const user = {
    links: {
        self: 'http://example.com',
    },
    data: {
        displayName: 'Test User',
    },
    logins: []
};

describe('loadUserProfile', () => {
    it('Produces the correct action', () => {
        expect(testSubject.loadUserProfile('someUserId')).toEqual({
            type: 'USERS/LOAD_USER_PROFILE_ACTION',
            payload: {
                userId: 'someUserId'
            }
        })
    })
});

describe('storeUserProfile', () => {
    it('Produces the correct action', () => {
        expect(testSubject.storeUserProfile(user)).toEqual({
            type: 'USERS/STORE_USER_PROFILE_ACTION',
            payload: {
                user: user
            }
        })
    })
});

describe('updateUserProfile', () => {
    it('Produces the correct action', () => {
        const callback = () => {};

        expect(testSubject.updateUserProfile('someUserId', user.data, user.logins, callback)).toEqual({
            type: 'USERS/UPDATE_USER_PROFILE_ACTION',
            payload: {
                userId: 'someUserId',
                data: user.data,
                logins: user.logins,
                callback
            }
        })
    })
});

describe('storeUserProfileMutation', () => {
    it('Correctly updates the state', () => {
        const state = {
            ...testSubject.module.initialState
        };
        const action: StoreUserProfileAction = {
            type: 'USERS/STORE_USER_PROFILE_ACTION',
            payload: {
                user: user
            }
        };
        testSubject.storeUserProfileMutation(state, action);

        expect(state).toEqual({
            users: {
                'http://example.com': user
            }
        });
    });
});

describe('selectUserById', () => {
    describe('From an empty state', () => {
        const state = {
            ...testSubject.module.initialState
        };

        it('Returns nothing', () => {
            expect(testSubject.selectUserById(state, 'userId')).toBeUndefined();
        });
    });
    describe('From a populated state', () => {
        const state = {
            users: {
                userId: user
            }
        };

        it('Returns the correct value', () => {
            expect(testSubject.selectUserById(state, 'userId')).toBe(user);
        });
    });
});
