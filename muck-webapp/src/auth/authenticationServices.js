// @flow

import { put, call } from 'redux-saga/effects';
import requester from "../api";

/** The shape of the state for this sub-module */
export type AuthenticationServicesState = {
    services: Array<string>
};

/** The shape of the action for storing authentication services */
export type StoreAuthenticationServicesAction = {
    type: string,
    payload: {
        services: Array<string>
    }
};

/** Action key for loading the authentication services from the server */
const LOAD_AUTHENTICATION_SERVICES_ACTION = "AUTH/LOAD_AUTHENTICATION_SERVICES_ACTION";

/** Action key for storing the authentication services into the Redux store */
const STORE_AUTHENTICATION_SERVICES_ACTION = "AUTH/STORE_AUTHENTICATION_SERVICES_ACTION";

/**
 * Selector to get the list of authentication services from the state
 * @param state the state to interrogate
 * @return The list of authentication service names
 */
export function selectAuthenticationServices(state: AuthenticationServicesState): Array<string> {
    return state.services;
}

/**
 * Saga for actually loading the authentication services services from the server
 */
export function* loadAuthenticationServicesSaga(): Generator<any, any, any> {
    const authServices = yield call(requester, {
        url: '/authentication/external'
    });

    yield put({
        type: STORE_AUTHENTICATION_SERVICES_ACTION,
        payload: {
            services: authServices.data.services
        }
    });
}

/**
 * Mutation for storing the authentication services into the state
 * @param state the state to update
 * @param action the action to update from
 */
export function storeAuthenticationServicesMutation(state: AuthenticationServicesState, action: StoreAuthenticationServicesAction) {
    state.services = action.payload.services;
}

/** The representation of this sub-module */
export const module = {
    initialState: {
        services: []
    },
    actions: {
        loadAuthenticationServices: () => ({
            type: LOAD_AUTHENTICATION_SERVICES_ACTION
        })
    },
    mutations: {
        [STORE_AUTHENTICATION_SERVICES_ACTION]: storeAuthenticationServicesMutation
    },
    sagas: {
        [LOAD_AUTHENTICATION_SERVICES_ACTION]: loadAuthenticationServicesSaga
    },
    selectors: {
        selectAuthenticationServices: (state: AuthenticationServicesState) => () => selectAuthenticationServices(state)
    }
};

/** The shape of this sub-module */
export type AuthenticationServicesModule = {
    loadAuthenticationServices: () => void,

    selectAuthenticationServices: () => Array<string>
};
