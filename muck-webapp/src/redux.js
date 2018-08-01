// @flow

import {createStore} from 'redux-box';
import {routerMiddleware, connectRouter} from 'connected-react-router';
import {createBrowserHistory} from 'history';

export const history = createBrowserHistory();

const reduxBoxConfig = {
    middlewares: [
        routerMiddleware(history)
    ],
    decorateReducer: connectRouter(history)
};

export const store = createStore([
], reduxBoxConfig);
