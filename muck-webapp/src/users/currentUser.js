// @flow

/** The shape of the state for this sub-module */
export type CurrentUserState = {
    currentUserId?: string
};

/** The shape of the action for storing the ID of the current user */
export type StoreCurrentUserIdAction = {
    type: string,
    payload: {
        userId: string
    }
};


/** Action key for storing the ID of the current user into the Redux store */
const STORE_CURRENT_USER_ACTION = "USERS/STORE_CURRENT_USER_ACTION";

/**
 * Construct the action for storing the ID of the current user into the store
 * @param userId the User ID
 */
export function storeCurrentUser(userId: string) : StoreCurrentUserIdAction {
    return {
        type: STORE_CURRENT_USER_ACTION,
        payload: {
            userId
        }
    };
}

/**
 * Mutation for storing the ID of the current user into the state
 * @param state the state to update
 * @param action the action to update from
 */
export function storeCurrentUserMutation(state: CurrentUserState, action: StoreCurrentUserIdAction) {
    state.currentUserId = action.payload.userId;
}

/** The representation of this sub-module */
export const module = {
    initialState: {
    },
    actions: {
    },
    mutations: {
        [STORE_CURRENT_USER_ACTION]: storeCurrentUserMutation
    },
    sagas: {
    },
    selectors: {
    }
};

/** The shape of this sub-module */
export type CurrentUserModule = {
};
