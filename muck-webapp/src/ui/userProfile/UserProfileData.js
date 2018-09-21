// @flow

import React from 'react';
import {Button, Form, Icon, Message} from 'semantic-ui-react'
import {I18n, Interpolate} from 'react-i18next';
import type {UserData} from "../../users/userProfiles";

/**
 * The props for the User Profile Data area
 */
type UserProfileDataProps = {
    user: UserData,

    onUpdate: (UserData, (string | null) => void) => void
};

/**
 * The state for the User Profile Data area
 */
type UserProfileDataState = {
    displayName: string,
    email: string,
    formState: Symbol,
    formResult?: Symbol
};

/** The form is in the initial state */
const FORM_STATE_INITIAL = Symbol('Initial');
/** The form is in the edited state */
const FORM_STATE_EDITED = Symbol('Edited');
/** The form is in the saving state */
const FORM_STATE_SAVING = Symbol('Saving');

/** Indication that the form was saved successfully */
const FORM_RESULT_SUCCESS = Symbol('Success');
/** Indication that there was an error saving the form */
const FORM_RESULT_ERROR = Symbol('Error');

/**
 * The section of the User Profile screen for the main user data
 */
export default class UserProfileData extends React.Component<UserProfileDataProps, UserProfileDataState> {
    /**
     * Construct the component
     * @param props the initial props
     */
    constructor(props: UserProfileDataProps) {
        super(props);
        this.state = {
            email: props.user.email || '',
            displayName: props.user.displayName || '',
            formState: FORM_STATE_INITIAL
        }
    }

    /**
     * Update the props that the component is rendered with
     * @param props the new props
     */
    componentWillReceiveProps(props: UserProfileDataProps) {
        this.setState({
            email: props.user.email || '',
            displayName: props.user.displayName || '',
            formState: FORM_STATE_INITIAL
        })
    }

    /**
     * Actually render the component
     */
    render() {
        return (
            <I18n>
                {
                    (t) => (
                        <Form loading={this.state.formState === FORM_STATE_SAVING}
                              success={this.state.formResult === FORM_RESULT_SUCCESS}
                              error={this.state.formResult === FORM_RESULT_ERROR}
                              onSubmit={this._handleSubmit}>
                            <Form.Field required error={this.state.displayName === '' && this.state.formResult === FORM_RESULT_ERROR}>
                                <label>
                                    <Interpolate i18nKey="userProfile.data.displayName.label" />
                                </label>
                                <input placeholder={t('userProfile.data.displayName.placeholder')}
                                       value={this.state.displayName}
                                       onChange={this._handleChangeDisplayName}
                                       autoFocus />
                            </Form.Field>
                            <Form.Field required error={this.state.email === '' && this.state.formResult === FORM_RESULT_ERROR}>
                                <label>
                                    <Interpolate i18nKey="userProfile.data.email.label" />
                                </label>
                                <input placeholder={t('userProfile.data.email.placeholder')}
                                       value={this.state.email}
                                       onChange={this._handleChangeEmail} />
                            </Form.Field>
                            <Button primary labelPosition='left' icon type='submit' disabled={this.state.formState === FORM_STATE_INITIAL}>
                                <Icon name='save outline' />
                                <Interpolate i18nKey="userProfile.data.buttons.save" />
                            </Button>
                            <Message success
                                     header={t('userProfile.data.messages.success.header')}
                                     content={t('userProfile.data.messages.success.message')} />
                            <Message error
                                     header={t('userProfile.data.messages.error.header')}
                                     content={t('userProfile.data.messages.error.message')} />
                        </Form>
                    )
                }
            </I18n>
        )
    }

    /**
     * Handler for when the Display Name changes
     * @param e the event to handle
     * @private
     */
    _handleChangeDisplayName = (e: SyntheticEvent<HTMLInputElement>) => {
        this.setState({
            displayName: e.currentTarget.value,
            formState: FORM_STATE_EDITED,
            formResult: undefined
        });
    };

    /**
     * Handler for when the Email Address changes
     * @param e the event to handle
     * @private
     */
    _handleChangeEmail = (e: SyntheticEvent<HTMLInputElement>) => {
        this.setState({
            email: e.currentTarget.value,
            formState: FORM_STATE_EDITED,
            formResult: undefined
        });
    };

    /**
     * Handler for the form submission
     * @private
     */
    _handleSubmit = () => {
        if (this.state.formState === FORM_STATE_EDITED) {
            if (this.state.email === '' || this.state.displayName === '') {
                this.setState({
                    formResult: FORM_RESULT_ERROR
                });
            } else {
                this.setState({
                    formState: FORM_STATE_SAVING
                });

                const user = {
                    email: this.state.email,
                    displayName: this.state.displayName
                };

                this.props.onUpdate(user, (err) => {
                    if (err) {
                        this.setState({
                            formState: FORM_STATE_EDITED,
                            formResult: FORM_RESULT_ERROR
                        });
                    } else {
                        this.setState({
                            formState: FORM_STATE_EDITED,
                            formResult: FORM_RESULT_SUCCESS
                        });
                    }
                });
            }
        }
    }
}
