import { Link } from "react-router-dom";
import "./widget.scss";
import KeyboardArrowUpIcon from "@mui/icons-material/KeyboardArrowUp";
import PersonOutlinedIcon from "@mui/icons-material/PersonOutlined";
import AccountBalanceWalletOutlinedIcon from "@mui/icons-material/AccountBalanceWalletOutlined";
import ShoppingCartOutlinedIcon from "@mui/icons-material/ShoppingCartOutlined";
import MonetizationOnOutlinedIcon from "@mui/icons-material/MonetizationOnOutlined";
import { useEffect, useState } from "react";
import axiosInstance from "../../api";

const Widget = ({ type }) => {
    const [count, setCount] = useState(0); // State để lưu số lượng

    useEffect(() => {
        const fetchCount = async () => {
            try {
                if (type === "user") {
                    // Gọi API để lấy số lượng người dùng
                    const response = await axiosInstance.get("users/count");
                    setCount(response.data); // Cập nhật số lượng người dùng
                } else if (type === "order") {
                    // Gọi API để lấy số lượng đơn hàng
                    const response = await axiosInstance.get("orders/count");
                    setCount(response.data); // Cập nhật số lượng đơn hàng
                }
            } catch (error) {
                console.error(`Lỗi khi lấy số lượng ${type}:`, error);
            }
        };
        fetchCount();
    }, [type]);

    let data;

    //temporary
    const diff = 20;

    switch (type) {
        case "user":
            data = {
                title: "USERS",
                isMoney: false,
                link: "See all users",
                icon: (
                    <PersonOutlinedIcon
                        className="icon"
                        style={{
                            color: "crimson",
                            backgroundColor: "rgba(255, 0, 0, 0.2)",
                        }}
                    />
                ),
            };
            break;
        case "order":
            data = {
                title: "ORDERS",
                isMoney: false,
                link: "View all orders",
                icon: (
                    <ShoppingCartOutlinedIcon
                        className="icon"
                        style={{
                            backgroundColor: "rgba(218, 165, 32, 0.2)",
                            color: "goldenrod",
                        }}
                    />
                ),
            };
            break;
        case "earning":
            data = {
                title: "EARNINGS",
                isMoney: true,
                link: "View net earnings",
                icon: (
                    <MonetizationOnOutlinedIcon
                        className="icon"
                        style={{
                            backgroundColor: "rgba(0, 128, 0, 0.2)",
                            color: "green",
                        }}
                    />
                ),
            };
            break;
        case "balance":
            data = {
                title: "BALANCE",
                isMoney: true,
                link: "See details",
                icon: (
                    <AccountBalanceWalletOutlinedIcon
                        className="icon"
                        style={{
                            backgroundColor: "rgba(128, 0, 128, 0.2)",
                            color: "purple",
                        }}
                    />
                ),
            };
            break;
        default:
            break;
    }

    return (
        <div className="widget">
            <div className="left">
                <span className="title">{data?.title}</span>
                <span className="counter">
                    {count} {data?.isMoney && "VNĐ"}
                </span>
                <Link to={`${type}s`}>
                    <span className="link">{data?.link}</span>
                </Link>
            </div>
            <div className="right">
                <div className="percentage positive">
                    <KeyboardArrowUpIcon />
                    {diff} %
                </div>
                {data?.icon}
            </div>
        </div>
    );
};

export default Widget;