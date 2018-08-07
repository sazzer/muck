// @flow

import React from 'react';
import { Interpolate } from 'react-i18next';

export default function App() {
    return (
        <div className="App">
            <Interpolate i18nKey="page.title" />
        </div>
    );
}
