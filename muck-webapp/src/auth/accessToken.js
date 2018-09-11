// @flow

/** Type representing an access token */
export type AccessToken = {
    bearerToken: string,
    expires: Date
};

/** The shape of the state for this sub-module */
export type AccessTokenState = {
    accessToken?: AccessToken
};

/** The shape of the action for storing the access token */
export type StoreAccessTokenAction = {
    type: string,
    payload: {
        bearerToken: string,
        expires: Date
    }
};


/** Action key for storing the access token into the Redux store */
const STORE_ACCESS_TOKEN_ACTION = "AUTH/STORE_ACCESS_TOKEN_ACTION";

/**
 * Construct the action for storing the access token into the store
 * @param bearerToken the actual bearer token
 * @param expires the expiry of the token
 */
export function storeAccessToken(bearerToken: string, expires: Date) : StoreAccessTokenAction {
    return {
        type: STORE_ACCESS_TOKEN_ACTION,
        payload: {
            bearerToken,
            expires
        }
    };
}

/**
 * Mutation for storing the access token into the state
 * @param state the state to update
 * @param action the action to update from
 */
export function storeAccessTokenMutation(state: AccessTokenState, action: StoreAccessTokenAction) {
    state.accessToken = {
        bearerToken: action.payload.bearerToken,
        expires: action.payload.expires
    };
}

/** The representation of this sub-module */
export const module = {
    initialState: {
    },
    actions: {
    },
    mutations: {
        [STORE_ACCESS_TOKEN_ACTION]: storeAccessTokenMutation
    },
    sagas: {
    },
    selectors: {
    }
};

/** The shape of this sub-module */
export type AccessTokenModule = {
};
