import './index.scss'
import { Outlet } from 'react-router-dom';

import Sidebar from './../../seller/sidebar/Sidebar';
import Navbar from './../../seller/navbar/Navbar';
function DefaultLayout({ children }) {
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

export default DefaultLayout;
