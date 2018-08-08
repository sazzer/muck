// @flow

import React from 'react';
import {Container, Segment} from 'semantic-ui-react';
import HeaderBar from './header';
import LandingPage from './landing';

export default function App() {
    return (
        <div className="App">
            <HeaderBar />
            <Container fluid>
                <Segment basic padded>
                    <LandingPage />
                </Segment>
            </Container>
        </div>
    );
}
