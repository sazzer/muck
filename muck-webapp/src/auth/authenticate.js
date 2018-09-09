// @flow

/** The shape of the state for this sub-module */
export type AuthenticateState = {

};

/** Type representing the action to start authentication */
type StartAuthenticationAction = {
    type: string,
    payload: {
        service: string
    }
};

/** Action key for starting authentication with a particular service */
const START_AUTHENTICATION_ACTION = "AUTH/START_AUTHENTICATION_ACTION";

/** The representation of this sub-module */
export const module = {
    initialState: {
    },
    actions: {
        startAuthentication: (service: string) => ({
            type: START_AUTHENTICATION_ACTION,
            payload: {
                service
            }
        })
    },
    mutations: {
    },
    sagas: {
    },
    selectors: {
    }
};

/** The shape of this sub-module */
export type AuthenticateModule = {
    startAuthentication: (string) => void
};
