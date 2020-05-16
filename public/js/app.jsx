import 'bootstrap/dist/css/bootstrap.min.css'
import 'bootstrap/dist/js/bootstrap.min'
import 'startbootstrap-freelancer/dist/css/styles.css'
import '../css/quotedb.css'

import React from 'react'
import ReactDOM from 'react-dom'

import { Header } from './ui/header/header'

import auth from './api/auth';
window.auth = auth;

export class QuoteDBAppFrame extends React.Component {
    render() {
        return (
            <div className="test quotedb-app-frame">
                <Header />
            </div>
        );
    }
}

ReactDOM.render(
    <QuoteDBAppFrame />,
    document.getElementById('react-root')
)
