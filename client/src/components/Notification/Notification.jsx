import { useContext, useEffect, useState } from 'react';
import { ChatContext } from '../../context/ChatContext';
import { getAllNotificationsApi } from '../../redux/notification/notificationsApi';
import { useDispatch, useSelector } from 'react-redux';
import { Link } from 'react-router-dom';
import NotificationIcon from '../../assets/notification_icon.png';
import NotificationsNoneIcon from '@mui/icons-material/NotificationsNone';

export default function Notification() {
    const dispatch = useDispatch();
    const { currentUser } = useContext(ChatContext);
    useEffect(() => {
        if (currentUser?.uid) {
            const unsubscribe = getAllNotificationsApi(dispatch, currentUser?.uid);
            return () => unsubscribe;
        }
    }, [currentUser]);

    const notifications = useSelector((state) => state.notifications.allNotification.data);

    const handleClickNotification = (item) => {
        console.log(item);
    };
    const [notificationCount, setNotificationCount] = useState(0);
    useEffect(() => {
        if (notifications !== undefined && notifications !== null) {
            console.log('notifications', notifications);
            setNotificationCount(
                notifications.reduce((acc, notification) => {
                    return acc + (notification.isViewed ? 0 : 1);
                }, 0),
            );
        }
    }, [notifications]);
    return (
        <>
            <ul className="header__navbar-list ml-6">
                {/* <li className="header__navbar-item">
                        <a href="" className="header__navbar-item-link">
                            <i className="header__navbar-icon far fa-question-circle"></i>
                            Hỗ trợ
                        </a>
                    </li> */}

                <li className=" header__navbar-item header__navbar-item--openNotify">
                    <NotificationsNoneIcon className="min-w-[20px] min-h-[20px] text-black relative" />
                    {notificationCount !== 0 && (
                        <span className="text-center min-w-[16px] min-h-[16px] absolute top-[-6px] left-3 bg-red-700 text-white rounded-xl font-bold text-[12px]">
                            {notificationCount}
                        </span>
                    )}
                    <a href="" className="hover:text-gray-500 header__navbar-item-link text-black font-semibold">
                        <i className="header__navbar-icon far fa-bell"></i>
                        Thông báo
                    </a>
                    <div className="header__notify">
                        <header className="header__notify-header">
                            <h3>Thông báo mới nhận</h3>
                        </header>
                        <ul className="header__notify-list">
                            {/* <li className="header__notify-item header__notify-item--viewed">
                                        <a href="" className="header__notify-link">
                                            <img
                                                src="https://images.fpt.shop/unsafe/filters:quality(5)/fptshop.com.vn/uploads/images/tin-tuc/147816/Originals/iPhone-14-Pro-11.jpg"
                                                alt=""
                                                className="header__notify-img"
                                            ></img>
                                            <div className="header__notify-info">
                                                <span className="header__notify-name font-semibold text-gray-800">
                                                    Iphone chính hãng
                                                </span>
                                                <span className="header__notify-desc">Mô tả Iphone chính hãng</span>
                                            </div>
                                        </a>
                                    </li>
                                    <li className="header__notify-item header__notify-item--viewed">
                                        <a href="" className="header__notify-link">
                                            <img
                                                src="https://images.fpt.shop/unsafe/filters:quality(5)/fptshop.com.vn/uploads/images/tin-tuc/147816/Originals/iPhone-14-Pro-11.jpg"
                                                alt=""
                                                className="header__notify-img"
                                            ></img>
                                            <div className="header__notify-info">
                                                <span className="header__notify-name font-semibold text-gray-800">
                                                    Iphone chính hãng
                                                </span>
                                                <span className="header__notify-desc">Mô tả Iphone chính hãng</span>
                                            </div>
                                        </a>
                                    </li> */}
                            {notifications.map((item, index) => (
                                <li
                                    className={
                                        'header__notify-item' + (item?.isViewed ? 'header__notify-item--viewed' : '')
                                    }
                                >
                                    {/* <a href="" className="header__notify-link"> */}
                                    <Link
                                        to={item?.url || ''}
                                        className="header__notify-link"
                                        onClick={() => handleClickNotification(item)}
                                    >
                                        <img
                                            src={item?.img || NotificationIcon}
                                            alt=""
                                            className="header__notify-img"
                                        ></img>
                                        <div className="header__notify-info">
                                            <span className="header__notify-name font-semibold text-gray-800">
                                                {item?.title}
                                            </span>
                                            <span className="header__notify-desc">{item?.content}</span>
                                        </div>
                                    </Link>
                                    {/* </a> */}
                                </li>
                            ))}
                        </ul>
                        <footer className="header__notify-footer">
                            <a href="" className="header__notify-footer-btn">
                                Xem tất cả
                            </a>
                        </footer>
                    </div>
                </li>
                <li className="header__navbar-item "></li>
            </ul>
        </>
    );
}
