// @flow

import {createSagas} from 'redux-box';
import type {CurrentUserState} from './currentUser';
import {module as currentUser} from './currentUser';

/** The shape of the state for this module */
type UsersModuleState = CurrentUserState;

/** The name of this Redux Box module */
const MODULE_NAME = 'users';

/** The initial state for this module */
const initialState: UsersModuleState = {
    ...currentUser.initialState
};

/** The actions for this module */
const actions = {
    ...currentUser.actions
};

/** The mutations for this module */
const mutations = {
    ...currentUser.mutations
};

/** The sagas for this module */
const sagas = {
    ...currentUser.sagas
};

/** The selectors for this module */
const selectors = {
    ...currentUser.selectors
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
export type UsersModule = {};
