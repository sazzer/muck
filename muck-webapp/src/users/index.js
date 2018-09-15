// @flow

import {createSagas} from 'redux-box';
import type {CurrentUserState, CurrentUserModule} from './currentUser';
import type {UserProfilesState, UserProfilesModule} from './userProfiles';
import {module as currentUser} from './currentUser';
import {module as userProfilesModule} from './userProfiles';

/** The shape of the state for this module */
type UsersModuleState = CurrentUserState & UserProfilesState;

/** The name of this Redux Box module */
const MODULE_NAME = 'users';

/** The initial state for this module */
const initialState: UsersModuleState = {
    ...currentUser.initialState,
    ...userProfilesModule.initialState
};

/** The actions for this module */
const actions = {
    ...currentUser.actions,
    ...userProfilesModule.actions
};

/** The mutations for this module */
const mutations = {
    ...currentUser.mutations,
    ...userProfilesModule.mutations
};

/** The sagas for this module */
const sagas = {
    ...currentUser.sagas,
    ...userProfilesModule.sagas
};

/** The selectors for this module */
const selectors = {
    ...currentUser.selectors,
    ...userProfilesModule.selectors
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
export type UsersModule = CurrentUserModule & UserProfilesModule;
