import './index.scss'
import { Outlet } from 'react-router-dom';

import Sidebar from './../../shipment/sidebar/Sidebar';
import Navbar from './../../shipment/navbar/Navbar';
function ShipmentLayout({ children }) {
    return (
        <div className="list">
        <Sidebar/>
        <div className="listContainer">
            <Navbar/>
            <Outlet></Outlet>
        </div>
        </div>
    );
}

export default ShipmentLayout;
