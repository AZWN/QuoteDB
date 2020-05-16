import React from 'react';
import { Navbar, NavbarBrand, Container } from 'reactstrap';

export class Header extends React.Component {
    render() {
        return (
            <Navbar id="mainNav" expand="lg" light fixed="top" className="bg-secondary">
                <Container>
                    <NavbarBrand href="/">QuoteDB</NavbarBrand>
                </Container>
            </Navbar>
        )
    }
}