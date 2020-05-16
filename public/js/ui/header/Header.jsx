import React from 'react';
import { Navbar, NavbarBrand, Container, NavItem, Nav } from 'reactstrap';
import { Link } from "react-router-dom";

import auth from "../../api/auth";
import Collapse from "reactstrap/lib/Collapse";

export class Header extends React.Component {
    constructor(props) {
        super(props);
        this.onLogout = props.onLogout;
    }
    logout() {
        auth.logout()
        this.onLogout();
    }
    render() {
        let links = auth.isLoggedIn() ?
            [<NavItem key="logout">
                <Link to="/logout" className="nav-link" onClick={() => this.logout()}>Logout</Link>
            </NavItem>] :
            [<NavItem key="register">
                <Link to="/register" className="nav-link">Register</Link>
            </NavItem>,
            <NavItem key="login" >
                <Link to="/login" className="nav-link">Login</Link>
            </NavItem>] ;
        return (
            <Navbar id="mainNav" expand="lg" light className="bg-secondary">
                <Container>
                    <NavbarBrand href="/">QuoteDB</NavbarBrand>
                </Container>
                <Collapse isOpen={true}>
                    <Nav navbar>
                        {links}
                    </Nav>
                </Collapse>
            </Navbar>
        )
    }
}