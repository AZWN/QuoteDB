import React from 'react';
import { withRouter } from 'react-router-dom';
import PropTypes from 'prop-types';

import { Button, Form, FormFeedback, FormGroup, Input, Label } from "reactstrap";
import $ from 'jquery';

import { login } from '../../api/auth';

class LoginForm extends React.Component {
    constructor(props) {
        super(props);
        this.state = {};
        this.onLogin = props.onLogin;
    }
    fieldPresent(sel) {
        const elem = $(sel)[0];
        const val = elem.value;
        return !!val;
    }
    submitForm(event) {
        event.preventDefault();
        const hasUserName = this.fieldPresent('#userName');
        const hasPassword = this.fieldPresent('#password');
        const passwordError = !hasPassword ? 'Please fill in your password!' : undefined;
        const newState = { hasUserName, hasPassword, passwordError };
        this.setState(newState);
        if (hasUserName && hasPassword) {
            // Try to actually login
            const userName = $('#userName')[0].value;
            const password = $('#password')[0].value;
            login(userName, password).then(() => {
                this.onLogin();
                this.props.history.push('/');
            }, req => {
                const passwordError = req.status === 401 ? 'Username/password combination invalid' : 'Unexpected error when logging in';
                this.setState({ passwordError, hasPassword: false });
            });
        }
    }
    propertyInvalid(prop) {
        return (prop in this.state && !this.state[prop]);
    }
    render() {
        return (
            <Form className="auth-form" noValidate onSubmit={e => this.submitForm(e)}>
                <h4>Login</h4>
                <FormGroup>
                    <Label for="userName">Username</Label>
                    <Input type="userName" name="userName" id="userName" placeholder="Username"
                        valid={this.state.hasUserName} invalid={this.propertyInvalid('hasUserName')}/>
                    <FormFeedback valid={false}>Please enter a username!</FormFeedback>
                </FormGroup>
                <FormGroup>
                    <Label for="password">Password</Label>
                    <Input type="password" name="password" id="password" placeholder="Password"
                        valid={this.state.hasPassword} invalid={this.propertyInvalid('hasPassword')}/>
                    <FormFeedback valid={false}>{this.state.passwordError}</FormFeedback>
                </FormGroup>
                <Button type="submit" color="primary">Login</Button>
            </Form>
        );
    }
}

LoginForm.propTypes = {
    onLogin: PropTypes.func.isRequired,
    history: PropTypes.shape({
        push: PropTypes.func.isRequired
    }).isRequired
};

export default withRouter(LoginForm);
