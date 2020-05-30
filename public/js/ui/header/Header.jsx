import React from 'react';
import { Navbar, NavbarBrand, Container, NavItem, Nav, NavbarToggler, Collapse } from 'reactstrap';
import { Link, withRouter } from "react-router-dom";
import PropTypes from 'prop-types';

import { logout, isLoggedIn } from "../../api/auth";

class Header extends React.Component {
    constructor(props) {
        super(props);
        this.onLogout = props.onLogout;
        this.state = {
            open: false
        };
    }
    logout() {
        logout();
        this.onLogout();
    }
    toggleMenu() {
        this.setState(old => {
            return {
                open: !old.open
            };
        });
    }
    closeMenu() {
        this.setState({ open: false });
    }
    render() {
        let links = isLoggedIn() ?
            [<NavItem key="logout">
                <Link to="/login" className="nav-link" onClick={() => { this.closeMenu(); this.logout(); }}>Logout</Link>
            </NavItem>] :
            [<NavItem key="register">
                <Link to="/register" className="nav-link" onClick={() => this.closeMenu()}>Register</Link>
            </NavItem>,
            <NavItem key="login" >
                <Link to="/login" className="nav-link" onClick={() => this.closeMenu()}>Login</Link>
            </NavItem>] ;
        return (
            <Navbar id="mainNav" expand="lg" light className="bg-secondary">
                <Container>
                    <NavbarBrand href="/">QuoteDB</NavbarBrand>
                </Container>
                <NavbarToggler onClick={() => this.toggleMenu()} />
                <Collapse isOpen={this.state.open} navbar>
                    <Nav navbar>
                        {links}
                    </Nav>
                </Collapse>
            </Navbar>
        );
    }
}

Header.propTypes = {
    onLogout: PropTypes.func.isRequired
};

export default withRouter(Header);
