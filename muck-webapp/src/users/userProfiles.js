// @flow

/** The type to represent a user */
import {call, put} from 'redux-saga/effects';
import {loadResource} from "../api";

/**
 * Type representing the details of a login with a provider
 */
export type UserLogin = {
    provider: string,
    providerId: string,
    displayName: string
}

/**
 * Type representing the Hypermedia links to other resources
 */
export type UserLinks = {
    self: string
};

/**
 * Type representing the data of a user
 */
export type UserData = {
    email?: string,
    displayName: string,
};

/**
 * Type representing a user
 */
export type UserProfile = {
    links: UserLinks,
    data: UserData,
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

/** The shape of the action for updating the user profile */
export type UpdateUserProfileAction = {
    type: string,
    payload: {
        data: UserData,
        logins: Array<UserLogin>,
        callback: (string | null) => void
    }
};

/** Action key for loading a user from the server */
const LOAD_USER_PROFILE_ACTION = "USERS/LOAD_USER_PROFILE_ACTION";

/** Action key for storing a user into the store */
const STORE_USER_PROFILE_ACTION = "USERS/STORE_USER_PROFILE_ACTION";

/** Action key for updating a user on the server */
const UPDATE_USER_PROFILE_ACTION = "USERS/UPDATE_USER_PROFILE_ACTION";

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
 * Construct the action for updating a user on the server
 * @param userData the user data to save
 * @param logins the user logins to save
 * @param callback optional callback to trigger after the update
 */
export function updateUserProfile(userData: UserData, logins: Array<UserLogin>, callback: (string | null) => void) : UpdateUserProfileAction {
    return {
        type: UPDATE_USER_PROFILE_ACTION,
        payload: {
            data: userData,
            logins,
            callback
        }
    }
}

/**
 * Selector to get the user with the given ID
 * @param state the state to get the user from
 * @param userId The ID of the user to get
 */
export function selectUserById(state: UserProfilesState, userId: string) : ?UserProfile {
    return state.users[userId];
}

/**
 * Saga for actually loading the authentication services services from the server
 */
export function* loadUserProfileSaga(action: LoadUserProfileAction): Generator<any, any, any> {
    const user = yield call(loadResource, action.payload.userId);

    const userProfile: UserProfile = {
        links: {
            self: user.getLink('self').href
        },
        data: {
            email: user.data.email,
            displayName: user.data.displayName,
        },
        logins: user.data.logins
    };
    yield put(storeUserProfile(userProfile));
}

/**
 * Saga for updating the user profile on the server
 */
export function updateUserProfileSaga(action: UpdateUserProfileAction) {
    action.payload.callback(null);
}

/**
 * Mutation for storing the user profile into the state
 * @param state the state to update
 * @param action the action to update from
 */
export function storeUserProfileMutation(state: UserProfilesState, action: StoreUserProfileAction) {
    state.users[action.payload.user.links.self] = action.payload.user;
}

/** The representation of this sub-module */
export const module = {
    initialState: {
        users: {}
    },
    actions: {
        updateUserProfile
    },
    mutations: {
        [STORE_USER_PROFILE_ACTION]: storeUserProfileMutation
    },
    sagas: {
        [LOAD_USER_PROFILE_ACTION]: loadUserProfileSaga,
        [UPDATE_USER_PROFILE_ACTION]: updateUserProfileSaga
    },
    selectors: {
        selectUserById: (state: UserProfilesState) => (userId: string) => selectUserById(state, userId)
    }
};

/** The shape of this sub-module */
export type UserProfilesModule = {
    updateUserProfile: (UserData, Array<UserLogin>, (string | null) => void) => void,

    selectUserById: (string) => ?UserProfile
};
