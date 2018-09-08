// @flow

/** The shape of a single authentication service */
type AuthenticationService = {
    name: string,
    uri: string
}

/** The shape of the state for this sub-module */
export type AuthenticationServicesState = {
    services: Array<AuthenticationService>
};

/**
 * Selector to get the list of authentication services from the state
 * @param state the state to interrogate
 * @return The list of authentication service names
 */
export function selectAuthenticationServices(state: AuthenticationServicesState): Array<string> {
    return state.services.map(service => service.name);
}

/** The representation of this sub-module */
export const module = {
    initialState: {
        services: []
    },
    actions: {},
    mutations: {},
    sagas: {},
    selectors: {
        selectAuthenticationServices: (state: AuthenticationServicesState) => () => selectAuthenticationServices(state)
    }
};

/** The shape of this sub-module */
export type AuthenticationServicesModule = {
    selectAuthenticationServices: () => Array<string>
};
