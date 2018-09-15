// @flow

/** The type to represent a user */
import {call} from 'redux-saga/effects';
import {getApiRoot, loadResource} from "../api";

export type UserProfile = {

};

/** The shape of the state for this sub-module */
export type UserProfilesState = {
    users: { [string] : UserProfile }
};

/** The shape of the action for loading a specific user from the server */
export type LoadUserProfileAction = {
    type: string,
    payload: {
        userId: string
    }
};


/** Action key for loading a user from the server */
const LOAD_USER_PROFILE_ACTION = "USERS/LOAD_USER_PROFILE_ACTION";

/**
 * Construct the action for loading a user from the server
 * @param userId the User ID
 */
export function loadUserProfile(userId: string) : LoadUserProfileAction {
    return {
        type: LOAD_USER_PROFILE_ACTION,
        payload: {
            userId
        }
    };
}

/**
 * Saga for actually loading the authentication services services from the server
 */
export function* loadUserProfileSaga(action: LoadUserProfileAction): Generator<any, any, any> {
    const apiDetails = yield call(getApiRoot);
    const userLink = apiDetails.getLink('user');
    if (userLink) {
        const user = yield call(loadResource, userLink.href, {
            pathParams: {
                id: action.payload.userId
            }
        });
        console.log(user);
    }
}

/** The representation of this sub-module */
export const module = {
    initialState: {
        users: {}
    },
    actions: {
    },
    mutations: {
    },
    sagas: {
        [LOAD_USER_PROFILE_ACTION]: loadUserProfileSaga
    },
    selectors: {
    }
};

/** The shape of this sub-module */
export type UserProfilesModule = {
};
