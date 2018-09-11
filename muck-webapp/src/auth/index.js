// @flow

import { createSagas } from 'redux-box';
import { module as authenticationServices } from './authenticationServices';
import { module as authenticate } from './authenticate';
import { module as accessToken } from './accessToken';
import type { AuthenticationServicesState, AuthenticationServicesModule } from "./authenticationServices";
import type { AuthenticateState, AuthenticateModule } from "./authenticate";
import type { AccessTokenState, AccessTokenModule } from "./accessToken";

/** The shape of the state for this module */
type AuthModuleState = AuthenticationServicesState & AuthenticateState & AccessTokenState;

/** The name of this Redux Box module */
const MODULE_NAME = 'auth';

/** The initial state for this module */
const initialState: AuthModuleState = {
    ...authenticationServices.initialState,
    ...authenticate.initialState,
    ...accessToken.initialState
};

/** The actions for this module */
const actions = {
    ...authenticationServices.actions,
    ...authenticate.actions,
    ...accessToken.actions
};

/** The mutations for this module */
const mutations = {
    ...authenticationServices.mutations,
    ...authenticate.mutations,
    ...accessToken.mutations
};

/** The sagas for this module */
const sagas = {
    ...authenticationServices.sagas,
    ...authenticate.sagas,
    ...accessToken.sagas
};

/** The selectors for this module */
const selectors = {
    ...authenticationServices.selectors,
    ...authenticate.selectors,
    ...accessToken.selectors
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
export type AuthModule = AuthenticationServicesModule & AuthenticateModule & AccessTokenModule;
