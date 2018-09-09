// @flow

import { select, call } from 'redux-saga/effects';
import { selectAuthenticationService } from "./authenticationServices";
import type {AuthenticationService} from "./authenticationServices";
import applySelector from '../redux/applySelector';

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

/**
 * Actually open the window for the authentication flow
 * @param service the service to open the window for
 */
function openAuthenticationWindow(service: AuthenticationService): Promise<any> {
    return new Promise((resolve) => {
        const openedWindow = window.open(service.href, 'muckAuth', 'dependent');

        function callback(event) {
            if (event.data && event.data.message === 'authenticationResult') {
                window.removeEventListener('message', callback);
                openedWindow.close();
                resolve(event.data);
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
    const service = yield select(applySelector, 'auth', selectAuthenticationService, [action.payload.service]);
    const authenticationResult = yield call(openAuthenticationWindow, service);
    console.log(authenticationResult);
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
