// @flow

import {call, put} from 'redux-saga/effects';
import {storeAccessToken} from "./accessToken";
import {storeCurrentUser} from "../users/currentUser";
import {buildUri} from "../api";

/** The URI Template for starting authentication */
const AUTHENTICATE_URI_TEMPLATE = '/authentication/external/{service}/start';

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

/** Type representing the result of successfully authenticating */
type AuthenticationResult = {
    userId: string,
    bearerToken: string,
    expires: Date
};

/** Action key for starting authentication with a particular service */
const START_AUTHENTICATION_ACTION = "AUTH/START_AUTHENTICATION_ACTION";

/**
 * Actually open the window for the authentication flow
 * @param service the service to open the window for
 */
function openAuthenticationWindow(service: string): Promise<AuthenticationResult> {
    return new Promise((resolve) => {
        const authUri = buildUri(AUTHENTICATE_URI_TEMPLATE, {service});
        const openedWindow = window.open(authUri, 'muckAuth', 'dependent');

        function callback(event) {
            if (event.data && event.data.message === 'authenticationResult') {
                window.removeEventListener('message', callback);
                openedWindow.close();
                resolve(event.data.params);
            }
        }

        window.addEventListener('message', callback);
    });
}

/**
 * Saga to actually start authentication with a named service
 * @param action the action to trigger authentication from
 */
export function* startAuthenticationSaga(action: StartAuthenticationAction): Generator<any, any, any> {
    const authenticationResult: AuthenticationResult = yield call(openAuthenticationWindow, action.payload.service);

    yield put(storeAccessToken(authenticationResult.bearerToken, authenticationResult.expires));

    yield put(storeCurrentUser(authenticationResult.userId));
}

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
        [START_AUTHENTICATION_ACTION]: startAuthenticationSaga
    },
    selectors: {
    }
};

/** The shape of this sub-module */
export type AuthenticateModule = {
    startAuthentication: (string) => void
};
