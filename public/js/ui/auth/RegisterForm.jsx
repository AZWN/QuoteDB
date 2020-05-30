import React from 'react';
import { withRouter } from 'react-router-dom';

import { Button, Form, FormFeedback, FormGroup, Input, Label } from "reactstrap";
import $ from 'jquery';

import { register } from "../../api/auth";

class RegisterForm extends React.Component {
    constructor(props) {
        super(props);
        this.state = {};
    }
    fieldPresent(sel) {
        const elem = $(sel)[0];
        const val = elem.value;
        return !!val;
    }
    submitForm(event) {
        event.preventDefault();
        const hasUserName = this.fieldPresent('#userName');
        const userNameError = hasUserName ? 'Please enter a username!' : undefined;
        const hasPassword = this.fieldPresent('#password');
        const repeatPasswordValid = hasPassword && ($('#password')[0].value === $('#confirmPassword')[0].value);
        const newState = { hasUserName, hasPassword, repeatPasswordValid, userNameError };
        this.setState(newState);
        if (hasUserName && hasPassword && repeatPasswordValid) {
            // Try to actually register
            const userName = $('#userName')[0].value;
            const password = $('#password')[0].value;
            const self = this;
            register(userName, password)
                .done(() => self.props.history.push('/login'))
                .fail(req => this.setState({ userNameError: req.responseJSON.message, hasUserName: false }));
        }
    }
    propertyInvalid(prop) {
        return (prop in this.state && !this.state[prop]);
    }
    render() {
        return (
            <Form className="auth-form" noValidate onSubmit={e => this.submitForm(e)}>
                <h4>Register</h4>
                <FormGroup>
                    <Label for="userName">Username</Label>
                    <Input type="userName" name="userName" id="userName" placeholder="Username"
                        valid={this.state.hasUserName} invalid={this.propertyInvalid('hasUserName')}/>
                    <FormFeedback valid={false}>{this.state.userNameError}</FormFeedback>
                </FormGroup>
                <FormGroup>
                    <Label for="password">Password</Label>
                    <Input type="password" name="password" id="password" placeholder="Password"
                        valid={this.state.hasPassword} invalid={this.propertyInvalid('hasPassword')}/>
                    <FormFeedback valid={false}>Please fill in a password!</FormFeedback>
                </FormGroup>
                <FormGroup>
                    <Label for="confirmPassword">Repeat Password</Label>
                    <Input type="password" name="confirmPassword" id="confirmPassword" placeholder="Repeat password"
                        valid={this.state.repeatPasswordValid} invalid={this.propertyInvalid('repeatPasswordValid')}/>
                    <FormFeedback valid={false}>Passwords do not match!</FormFeedback>
                </FormGroup>
                <Button type="submit" color="primary">Register</Button>
            </Form>
        );
    }
}

export default withRouter(RegisterForm);