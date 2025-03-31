import Header from '../../Header';
import Footer from '../../Footer';
import { useState, useEffect } from 'react';
import { Outlet, useNavigate } from 'react-router-dom';
import { useSelector, useDispatch } from 'react-redux';
import { getUserByAccess, logUserByAccess } from '../../../redux/user/userApi';
import { Link } from 'react-router-dom';
import MessengerCustomerChat from 'react-messenger-customer-chat/lib/MessengerCustomerChat';

import './header.scss';
import { DEFAULT_STORE, DEFAULT_VARIABLE } from '../../../utils';
import { ChatContextProvider } from '../../../context/ChatContext';
import Notification from '../../Notification/Notification';
import swal from 'sweetalert';

function CommonLayout({ children }) {
    const navigate = useNavigate();
    const dispatch = useDispatch();
    const [getUserAccess, setUserAccess] = useState(undefined);
    const getAccess = JSON.parse(localStorage.getItem(DEFAULT_STORE.TOKEN));
    useEffect(() => {
        if (getAccess) {
            getUserByAccess(dispatch);
        }
    }, [localStorage.getItem(DEFAULT_STORE.TOKEN)]);
    const user = useSelector((state) => state.user?.user);
    const hanleLogout = () => {
        logUserByAccess(dispatch);
    };

    const handleChat = (e) => {
        e.preventDefault();
        const user = localStorage.getItem(DEFAULT_STORE.TOKEN);
        if (user) {
            navigate(`/chat`)
        } else {
            swal({title: 'Hãy đăng nhập trước khi thực hiện tính năng này!', icon: 'warning'})
        }
    }

    return (
        <>
            <div className="w-full h-[140px] flex justify-center gap-16 items-center ">
                {/* <div className="select-none text- text-[21px] w-[110px] px-6 py-2 font-semibold text-green-400 border-r-4 border-green-200 rounded shadow-lg text-center">
                    Phúc Xi Cúc
                </div> */}
                <Link
                    to={'/'}
                    className="select-none text- text-[21px] px-2 py-2 font-semibold text-green-400 border-r-4 border-green-200 rounded shadow-lg text-center w-[110px] h-[100px]"
                >
                    <img src="/logowb.png" className="w-full h-full"></img>
                </Link>
                {/* <div className="hover:text-gray-500 header__navbar-item--openqr header__navbar-item text-black font-semibold">
                            <Link to="/Seller" className="w-32  text-center " onClick={()=>setSingIn(!singIn)}>
                                            Kênh người bán
                                </Link>
                        </div> */}
                <div className="select-none text-[23px] px-6 py-2 font-semibold text-white bg-blue-400 border-r-4 border-blue-400 rounded shadow-lg shadow-purple-600/50">
                    Sàn giao dịch thương mại điện tử
                </div>
                <nav className=" header__navbar hide-on-mobile-tablet">
                    <ul className="header__navbar-list">
                        <li className="text-black font-semibold header__navbar-item--openqr header__navbar-item header__navbar-item--separate-week hover:text-gray-500">
                            <Link to="/Delivery" className="w-32  text-center ">
                                Tra cứu đơn hàng
                            </Link>
                        </li>

                        <li className="header__navbar-item ">
                            {user && (
                                <li className="header__navbar-item header__navbar-user ml-4">
                                    <img
                                        src={
                                            user.avatar ||
                                            'https://haycafe.vn/wp-content/uploads/2022/03/avatar-facebook-doc.jpg'
                                        }
                                        alt=""
                                        className="header__user-img"
                                    ></img>
                                    <span className="header__user-name text-black">
                                        {user?.fullName || DEFAULT_VARIABLE.FULL_NAME}
                                    </span>

                                    <ul className="header__navbar-user-menu">
                                        <li className="header__navbar-user-item text-gray-600">
                                            <Link to="history">Tài khoản của tôi</Link>
                                        </li>
                                        <li className="header__navbar-user-item text-gray-600">
                                            <Link to="history">Đơn mua</Link>
                                        </li>
                                        
                                        {user.role === 2 && (
                                            <li className="header__navbar-user-item text-gray-600">
                                                <Link to="/SignInSeller" className="">
                                                    Đăng ký bán hàng
                                                </Link>
                                            </li>
                                        )}
                                        {user.role === 1 && (
                                            <li className="header__navbar-user-item text-gray-600">
                                                <Link to="/Seller" className="">
                                                    Quản lý bán hàng
                                                </Link>
                                            </li>
                                        )}
                                        {user.role === 3 && (
                                            <li className="header__navbar-user-item text-gray-600">
                                                <Link to="/shipment" className="">
                                                    Quản lý đơn vận chuyển
                                                </Link>
                                            </li>
                                        )}

                                        <li
                                            className="header__navbar-user-item header__navbar-user-item--separate text-gray-600"
                                            onClick={hanleLogout}
                                        >
                                            <a href="">Đăng xuất</a>
                                        </li>
                                    </ul>
                                </li>
                            )}
                        </li>
                    </ul>

                    <ChatContextProvider>
                        <Notification/>
                    </ChatContextProvider>
                    <Link to={'/chat'} onClick={handleChat} className="select-none px-6 py-2 font-semibold rounded shadow-lg shadow-purple-600/50">
                        Chat
                    </Link>
                </nav>
                {/* <div className='w-[520px] h-[140px] '> <img className=' w-full h-full' src='https://pdp.edu.vn/wp-content/uploads/2021/04/hinh-nen-cong-nghe-1.jpg'></img></div> */}
            </div>
            <Header />
            <main role="main" className="wrapper">
                <div className="content">
                    <Outlet />
                </div>
            </main>
            <Footer />
            <MessengerCustomerChat
                pageId="101178992810439"
                appId="952881962336925"
                htmlRef="https://connect.facebook.net/vi_VN/sdk/xfbml.customerchat.js"
            />
        </>
    );
}

export default CommonLayout;
