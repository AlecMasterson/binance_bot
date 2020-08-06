import React from 'react';
import {Navbar} from 'react-bootstrap';
import BacktestForm from './components/ta/ConfigForm';
import 'bootstrap/dist/css/bootstrap.min.css';

export default class App extends React.Component<any, any> {
    render() {
        return (
            <>
                <Navbar bg='dark' variant='dark'>
                    <Navbar.Brand href=''>
                        Binance Bot
                    </Navbar.Brand>
                </Navbar>

                <div className='container'>
                    <BacktestForm />
                </div>
            </>
        );
    }
}