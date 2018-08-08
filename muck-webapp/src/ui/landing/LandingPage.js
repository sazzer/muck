// @flow

import React from 'react';
import {Grid, Container} from 'semantic-ui-react';
import LandingArea from './LandingArea';

/**
 * React component to represent the Landing Page
 */
export default function LandingPage() {
    return (
        <Container>
            <Grid>
                <Grid.Column width={12}>
                    <LandingArea/>
                </Grid.Column>
                <Grid.Column width={4}>
                </Grid.Column>
            </Grid>
        </Container>
    )
}
