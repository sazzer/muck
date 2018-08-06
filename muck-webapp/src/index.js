import React from 'react';
import ReactDOM from 'react-dom';
import {I18nextProvider, translate} from 'react-i18next';
import {ConnectedRouter as Router} from 'connected-react-router';
import {Provider} from 'react-redux';
import {history, store} from './redux';
import i18n from './i18n';
import App from './App';

/**
 * The contents of the app, wrapped in the translations layer
 */
const TranslatedAppContents = translate(['muck'], {wait: true})(App);

/**
 * The wrapper around the main application to set everything up
 * @return {*} the main application
 */
const AppWrapper = () => (
    <I18nextProvider i18n={ i18n }>
        <Provider store={store}>
            <Router history={history}>
                <TranslatedAppContents />
            </Router>
        </Provider>
    </I18nextProvider>
);

ReactDOM.render(<AppWrapper />, document.getElementById('root'));
