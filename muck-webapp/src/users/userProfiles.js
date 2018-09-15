// @flow

/** The type to represent a user */
import {call, put} from 'redux-saga/effects';
import {getApiRoot, loadResource} from "../api";

/**
 * Type representing the details of a login with a provider
 */
export type UserLogin = {
    provider: string,
    providerId: string,
    displayName: string
}

/**
 * Type representing a user
 */
export type UserProfile = {
    userLink: string,
    email?: string,
    displayName: string,
    logins: Array<UserLogin>
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

/** The shape of the action for storing a specific user into the store */
export type StoreUserProfileAction = {
    type: string,
    payload: {
        user: UserProfile
    }
}

/** Action key for loading a user from the server */
const LOAD_USER_PROFILE_ACTION = "USERS/LOAD_USER_PROFILE_ACTION";

/** Action key for storing a user into the store */
const STORE_USER_PROFILE_ACTION = "USERS/STORE_USER_PROFILE_ACTION";

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
 * Construct the action for storing a user into the store
 * @param user the user to store
 */
export function storeUserProfile(user: UserProfile) : StoreUserProfileAction {
    return {
        type: STORE_USER_PROFILE_ACTION,
        payload: {
            user
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

        const userProfile = {
            userLink: user.getLink('self').href,
            email: user.data.email,
            displayName: user.data.displayName,
            logins: user.data.logins
        };
        yield put(storeUserProfile(userProfile));
    }
}

/**
 * Mutation for storing the user profile into the state
 * @param state the state to update
 * @param action the action to update from
 */
export function storeUserProfileMutation(state: UserProfilesState, action: StoreUserProfileAction) {
    state.users[action.payload.user.userLink] = action.payload.user;
}

/** The representation of this sub-module */
export const module = {
    initialState: {
        users: {}
    },
    actions: {
    },
    mutations: {
        [STORE_USER_PROFILE_ACTION]: storeUserProfileMutation
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
