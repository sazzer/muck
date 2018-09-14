// @flow

import {createStore} from 'redux-box';
import {routerMiddleware, connectRouter} from 'connected-react-router';
import {createBrowserHistory} from 'history';
import {module as authModule} from '../auth';
import {module as usersModule} from '../users';

export const history = createBrowserHistory();

const reduxBoxConfig = {
    middlewares: [
        routerMiddleware(history)
    ],
    decorateReducer: connectRouter(history)
};

export const store = createStore([
    authModule,
    usersModule
], reduxBoxConfig);
