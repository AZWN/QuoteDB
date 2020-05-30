import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap/dist/js/bootstrap.min';
import 'startbootstrap-freelancer/dist/css/styles.css';
import '../css/quotedb.css';

import React, { Fragment } from 'react';
import ReactDOM from 'react-dom';
import { BrowserRouter as Router, Switch, Route, Link } from "react-router-dom";
import { Container, Row, Col } from "reactstrap";

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
                    <Switch>
                        {this.state.loggedIn ? this.renderUserRoutes() : this.renderAnonymousRoutes()}
                    </Switch>
                </Router>
            </div>
        );
    }
    renderSharedRoutes() {
        return (<Fragment>
            <Route exact path="/">
                <div className="main-header">
                    <div className="main-header-content">
                        <h1 className="primary-text">AppCiting</h1>
                        <p className="secondary-text">Life itself is a quotation (Jorge Luis Borges)</p>
                        <div className="main-button-container">
                            <Link className="btn btn-square btn-primary" to="/about">Take the Tour!</Link>
                            {this.state.loggedIn ?
                                <Link className="btn btn-square btn-secondary" to="/quotes">Browse Quotes</Link> :
                                <Link className="btn btn-square btn-secondary" to="/login">Login</Link>}
                        </div>
                    </div>
                </div>
                <Container className="main-body quotedb-container">
                    <Row>
                        <Col sm={{size: 4}}>
                            <h3>Easy</h3>
                            <p>AppCiting makes it easy to collect, manage and share your quotes.
                                Using our fast and responsive web interface or one of the apps, you can quickly save your quotes,
                                and access them from anywhere you want.</p>
                        </Col>
                        <Col sm={{size: 4}}>
                            <h3>Free</h3>
                            <p>AppCiting is unlimited and free to use, without any advertisements and spam.</p>
                        </Col>
                        <Col sm={{size: 4}}>
                            <h3>Flexible</h3>
                            <p>Using our advanced mechanisms to organize you quotes,
                                including a high-end search engine and fine-grained sharing options, you are in full control
                                over your quote collections.</p>
                        </Col>
                    </Row>
                </Container>
            </Route>
        </Fragment>);
    }
    renderAnonymousRoutes() {
        return (<Fragment>
            {this.renderSharedRoutes()}
            <Route path="/register">
                <Container className="quotedb-container">
                    <Row>
                        <Col sm={{ size: 6, offset: 3 }} md={{ size: 6, offset: 3 }} lg={{ size: 4, offset: 4}}>
                            <RegisterForm />
                        </Col>
                    </Row>
                </Container>
            </Route>
            <Route path="/login">
                <Container className="quotedb-container">
                    <Row>
                        <Col sm={{ size: 6, offset: 3 }} md={{ size: 6, offset: 3 }} lg={{ size: 4, offset: 4}}>
                            <LoginForm onLogin={() => this.updateLoggedIn()}/>
                        </Col>
                    </Row>
                </Container>
            </Route>
        </Fragment>);
    }
    renderUserRoutes() {
        return (<Fragment>
            {this.renderSharedRoutes()}
        </Fragment>);
    }
}

ReactDOM.render(
    <QuoteDBAppFrame />,
    document.getElementById('react-root')
);
