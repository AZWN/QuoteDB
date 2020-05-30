import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap/dist/js/bootstrap.min';
import 'startbootstrap-freelancer/dist/css/styles.css';
import '../css/quotedb.css';

import React from 'react';
import ReactDOM from 'react-dom';
import { BrowserRouter as Router, Switch, Route, Redirect } from "react-router-dom";
import { Container, Row, Col } from "reactstrap";

import PropTypes from 'prop-types';
import { isLoggedIn } from './api/auth';

import Header from './ui/header/Header';
import RegisterForm from "./ui/auth/RegisterForm";
import LoginForm from './ui/auth/LoginForm';

export class QuoteDBAppFrame extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            loggedIn: isLoggedIn()
        };
    }
    updateLoggedIn() {
        this.setState( {
            loggedIn: isLoggedIn()
        });
    }
    render() {
        return (
            <div className="test quotedb-app-frame">
                <Router>
                    <Header onLogout={() => this.updateLoggedIn()}/>
                    <Container className="quotedb-container">
                        <Switch>
                            <Route path="/register">
                                <Row>
                                    <Col sm={{ size: 6, offset: 3 }} md={{ size: 6, offset: 3 }} lg={{ size: 4, offset: 4}}>
                                        <RegisterForm />
                                    </Col>
                                </Row>
                            </Route>
                            <Route path="/login">
                                <Row>
                                    <Col sm={{ size: 6, offset: 3 }} md={{ size: 6, offset: 3 }} lg={{ size: 4, offset: 4}}>
                                        <LoginForm onLogin={() => this.updateLoggedIn()}/>
                                    </Col>
                                </Row>
                            </Route>
                            <Route path="/">
                                <RedirectHome loggedIn={this.state.loggedIn}/>
                            </Route>
                        </Switch>
                    </Container>
                </Router>
            </div>
        );
    }
}

class RedirectHome extends React.Component {
    constructor(props) {
        super(props);
        this.loggedIn = props.loggedIn;
    }
    render() {
        if (this.loggedIn) {
            return (<Redirect to="/dashboard"/>);
        }
        return (<Redirect to="/login"/>);
    }
}

RedirectHome.propTypes = {
    loggedIn: PropTypes.bool
};

ReactDOM.render(
    <QuoteDBAppFrame />,
    document.getElementById('react-root')
);
