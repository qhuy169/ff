import './sidebar.scss';
import DashboardIcon from '@mui/icons-material/Dashboard';
import PersonOutlineIcon from '@mui/icons-material/PersonOutline';
import LocalShippingIcon from '@mui/icons-material/LocalShipping';
import CreditCardIcon from '@mui/icons-material/CreditCard';
import StoreIcon from '@mui/icons-material/Store';
import InsertChartIcon from '@mui/icons-material/InsertChart';
import SettingsApplicationsIcon from '@mui/icons-material/SettingsApplications';
import ExitToAppIcon from '@mui/icons-material/ExitToApp';
import NotificationsNoneIcon from '@mui/icons-material/NotificationsNone';
import SettingsSystemDaydreamOutlinedIcon from '@mui/icons-material/SettingsSystemDaydreamOutlined';
import PsychologyOutlinedIcon from '@mui/icons-material/PsychologyOutlined';
import AccountCircleOutlinedIcon from '@mui/icons-material/AccountCircleOutlined';
import AssignmentReturnIcon from '@mui/icons-material/AssignmentReturn';

//import { useContext } from "react";
import { useDispatch, useSelector } from 'react-redux';
import { Link, useNavigate } from 'react-router-dom';
import ExitToApp from '@mui/icons-material/ExitToApp';

const Sidebar = () => {
    // const user = useSelector((state)=> state.auth?.login?.currentUser)
    // const accessToken = user?.accessToken;
    // const id = user?._id;
    const dispatch = useDispatch();
    const navigate = useNavigate();
    const handleLogout = () => {
        // logOut(dispatch,id,navigate, accessToken);
    };
    return (
        <div className="sidebar">
            <div className="top">
                <Link to="/seller" style={{ textDecoration: 'none' }}>
                    <span className="logo">PNTech</span>
                </Link>
            </div>
            <hr />
            <div className="center">
                <ul>
                
                    <p className="title">LISTS</p>
                    <Link to="/shipment/users" style={{ textDecoration: 'none' }}></Link>
         
                    <Link to="/shipment" style={{ textDecoration: 'none' }}>
                        <li>
                            <CreditCardIcon className="icon" />
                            <span>Area</span>
                        </li>
                    </Link>
                    <Link to="/shipment/listorders" style={{ textDecoration: 'none' }}>
                        <li>
                            <NotificationsNoneIcon className="icon" />
                            <span>Delivery</span>
                        </li>
                    </Link>

                    <Link to="/logout" onClick={handleLogout} style={{ textDecoration: 'none' }}>
                        <li>
                            <ExitToAppIcon className="icon" />
                            <span>Logout</span>
                        </li>
                    </Link>
                </ul>
            </div>
            <div className="bottom">
                <div className="colorOption"></div>
                <div className="colorOption"></div>
            </div>
            <hr />
            <div className="footer">
                <Link to="/" style={{ textDecoration: 'none' }}>
                    <div>
                        <AssignmentReturnIcon className="icon" />
                        <span>Go Homepage</span>
                    </div>
                </Link>
            </div>
        </div>
    );
};

export default Sidebar;
