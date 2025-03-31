import { useEffect } from 'react';
import { BrowserRouter as Router } from 'react-router-dom';
import MessengerCustomerChat from 'react-messenger-customer-chat';
import Routes from './routes';
import { HashRouter } from 'react-router-dom';
import ReactGA from 'react-ga';
import './App.css';

function App() {
    const TRACKING_ID = 'UA-263715340-1';
    ReactGA.initialize(TRACKING_ID);
    useEffect(() => {
        ReactGA.pageview(window.location.pathname);
    });
    if (window.location.hash === '#_=_') {
        window.location.hash = '';
    }
    return (
        <HashRouter basename="">
            <div className="App">
                <Routes />
            </div>
        </HashRouter>
    );
}

export default App;
