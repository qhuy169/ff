import React from "react";
import { useEffect } from "react";
import { useSelector } from "react-redux";
import { getAllOrders } from "../../redux/order/ordersApi";
import { useDispatch } from "react-redux";
import { Table } from "flowbite-react";
import { numberWithCommas } from "~/utils";
import OrderDetail from "./OrderDetail";
import "./table.scss";
import clsx from "clsx";
import { useState } from "react";
import { orderService } from "../../services/order.service";
import Sidebar from "~/components/sidebar/Sidebar";
import Navbar from "~/components/navbar/Navbar";
import { EOrderStatus } from "../../utils/variableDefault";
import { db } from "../../firebase";
import { addNotificationByIdApi } from "../../redux/notification/notificationsApi";

const Order = (props) => {
    const style = (text) => {
        switch (text) {
            case "Đã đặt hàng":
            case "Đặt hàng":
                return "text-blue-400 uppercase font-bold";
            case "Đang giao hàng":
                return "text-blue-400";
            case "Đã hủy":
                return "text-red-400 uppercase font-bold";
            case "Đã xác nhận":
                return "text-green-400 font-bold uppercase";
        }
    };
    const [orderDetail, setOrderDetail] = useState({ index: -1, id: null });
    const handleAccept = async(order) => {
        if (confirm("Bạn có muốn Xác nhận đơn hàng không?")) {
            const id = order.id;
            const data = JSON.stringify({ status: "Đã xác nhận" });
            console.log(order)
            await orderService.updateHistoryOrder(id, data);
            addNotificationByIdApi(order.id, 'Cập nhật đơn hàng', `Trạng thái đơn hàng với mã số ${order.id} đã chuyển thành "Đã xác nhận"`)
            console.log(res);

            if (res) {
                alert("Xác nhận thành công");
            }
        }
    };

    const dispatch = useDispatch();

    const orders = useSelector((state) => state.orders.order.data);

    useEffect(() => {
        getAllOrders(dispatch);
    }, []);

    useEffect(() => {
        document.title = props.title;
    }, [props.title]);

    // console.log(orders);

    return (
        <div>
            <Table hoverable={true} className="">
                <Table.Head>
                    <Table.HeadCell> Mã đơn hàng </Table.HeadCell>
                    <Table.HeadCell>Sản phẩm</Table.HeadCell>
                    <Table.HeadCell>Số lượng</Table.HeadCell>
                    <Table.HeadCell>Giá</Table.HeadCell>
                    <Table.HeadCell> Ngày đặt mua</Table.HeadCell>
                    <Table.HeadCell>Trạng thái</Table.HeadCell>
                    <Table.HeadCell>
                        <span className="sr-only">Edit</span>
                    </Table.HeadCell>
                </Table.Head>
                <Table.Body className="divide-y">
                    {orders.map((order, index) => {
                        const styleStatus = style(order.status);
                        const displayDetail = index === orderDetail.index;
                        const displayAcceptBtn = order.status === 0;
                        const styleDisable = "bg-gray-100";
                        return (
                            <>
                                <Table.Row className="bg-white dark:border-gray-700 dark:bg-gray-800 overflow-hidden">
                                    <Table.Cell className="text-blue-400">
                                        #{order.id}
                                    </Table.Cell>
                                    <Table.Cell className="text-blue-400 hover:text-blue-700 select-none">
                                        <button
                                            onClick={() =>
                                                setOrderDetail((current) => {
                                                    return current.index ===
                                                        index
                                                        ? {
                                                              index: -1,
                                                              id: order.id,
                                                          }
                                                        : {
                                                              index,
                                                              id: order.id,
                                                          };
                                                })
                                            }
                                        >
                                            Xem chi tiết
                                        </button>
                                    </Table.Cell>
                                    <Table.Cell>
                                        {order.totalQuantity}
                                    </Table.Cell>
                                    <Table.Cell className="text-red-400">
                                        {numberWithCommas(order.totalPrice)}₫
                                    </Table.Cell>
                                    <Table.Cell>
                                        {" "}
                                        <p className="">
                                            {order.createdAt}
                                        </p>{" "}
                                    </Table.Cell>

                                    <Table.Cell className={styleStatus}>
                                        {EOrderStatus.getNameFromIndex(order.status)}
                                    </Table.Cell>
                                    <Table.Cell>
                                        <button
                                            disabled={!displayAcceptBtn}
                                            id={order.id}
                                            onClick={() => handleAccept(order)}
                                            className={clsx(
                                                "bg-red-500 text-xl font-medium p-4 rounded-lg  text-white",
                                                !displayAcceptBtn &&
                                                    "!bg-gray-100 !text-gray-700"
                                            )}
                                        >
                                            Xác nhận
                                        </button>
                                    </Table.Cell>
                                </Table.Row>
                                {displayDetail && (
                                    <Table.Row>
                                        <Table.Cell className="" colspan="7">
                                            <OrderDetail {...order} />
                                        </Table.Cell>
                                    </Table.Row>
                                )}
                            </>
                        );
                    })}
                </Table.Body>
            </Table>
        </div>
    );
};

export default Order;
