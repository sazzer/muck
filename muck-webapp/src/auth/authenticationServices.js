// @flow

import { put, call } from 'redux-saga/effects';
import { loadResource, getApiRoot } from "../api";

/** The shape of a single authentication service */
export type AuthenticationService = {
    name: string,
    href: string
}

/** The shape of the state for this sub-module */
export type AuthenticationServicesState = {
    services: Array<AuthenticationService>
};

/** The shape of the action for storing authentication services */
export type StoreAuthenticationServicesAction = {
    type: string,
    payload: {
        services: Array<AuthenticationService>
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
    return state.services.map(service => service.name);
}

/**
 * Selector to get the single authentication service with the given name, if there is one
 * @param state the state to interrogate
 * @param serviceName the name of the service
 * @return The service details, if found
 */
export function selectAuthenticationService(state: AuthenticationServicesState, serviceName: string): ?AuthenticationService {
    return state.services.find(service => service.name === serviceName);
}

/**
 * Saga for actually loading the authentication services services from the server
 */
export function* loadAuthenticationServicesSaga(): Generator<any, any, any> {
    const apiDetails = yield call(getApiRoot);
    const authServicesLink = apiDetails.getLink('externalAuthenticationServices');
    if (authServicesLink) {
        const authServices = yield call(loadResource, authServicesLink.href);
        const services = authServices.getLinks('services');
        yield put({
            type: STORE_AUTHENTICATION_SERVICES_ACTION,
            payload: {
                services: services
            }
        })
    }
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
