// @flow

import React from 'react';
import {Container, Segment} from 'semantic-ui-react';
import {Switch, Route} from 'react-router-dom';
import HeaderBar from './header';
import LandingPage from './landing';
import UserProfile from './userProfile';

export default function App() {
    return (
        <div className="App">
            <HeaderBar />
            <Container fluid>
                <Segment basic padded>
                    <Switch>
                        <Route exact path="/userProfile" component={UserProfile} />
                        <Route component={LandingPage} />
                    </Switch>
                </Segment>
            </Container>
        </div>
    );
}
