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
                    <p className="title">MAIN</p>
                    <Link to="/seller" style={{ textDecoration: 'none' }}>
                        <li>
                            <DashboardIcon className="icon" />
                            <span>Dashboard</span>
                        </li>
                    </Link>
                    <p className="title">LISTS</p>
                    <Link to="/seller/users" style={{ textDecoration: 'none' }}></Link>
                    <Link to="/seller/feedbacks" style={{ textDecoration: 'none' }}>
                        <li>
                            <StoreIcon className="icon" />
                            <span>Feedbacks</span>
                        </li>
                    </Link>
                    <Link to="/seller/products" style={{ textDecoration: 'none' }}>
                        <li>
                            <StoreIcon className="icon" />
                            <span>Products</span>
                        </li>
                    </Link>
                    <Link to="/seller/orders" style={{ textDecoration: 'none' }}>
                        <li>
                            <CreditCardIcon className="icon" />
                            <span>Orders</span>
                        </li>
                    </Link>
        <Link to="/seller/discount/list" style={{ textDecoration: "none" }}>
                        <li>
                            <CreditCardIcon className="icon" />
                            <span>Khuyến mãi</span>
                        </li>
                    </Link>
    
                    <Link to="/discount" style={{ textDecoration: "none" }}></Link>


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
