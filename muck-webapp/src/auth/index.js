// @flow

import { createSagas } from 'redux-box';
import { module as authenticationServices } from './authenticationServices';
import type { AuthenticationServicesState, AuthenticationServicesModule } from "./authenticationServices";

/** The shape of the state for this module */
type AuthModuleState = AuthenticationServicesState;

/** The name of this Redux Box module */
const MODULE_NAME = 'auth';

/** The initial state for this module */
const initialState: AuthModuleState = {
    ...authenticationServices.initialState
};

/** The actions for this module */
const actions = {
    ...authenticationServices.actions
};

/** The mutations for this module */
const mutations = {
    ...authenticationServices.mutations
};

/** The sagas for this module */
const sagas = {
    ...authenticationServices.sagas
};

/** The selectors for this module */
const selectors = {
    ...authenticationServices.selectors
};

/** The actual module */
export const module = {
    name: MODULE_NAME,
    state: initialState,
    actions,
    mutations,
    sagas: createSagas(sagas),
    selectors
};

/** The shape of this module */
export type AuthModule = AuthenticationServicesModule;
