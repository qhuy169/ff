import { useState, Fragment } from 'react';
import { numberWithCommas } from '~/utils';
import { Table } from 'flowbite-react';
import './table.scss';
import OrderDetail from './OrderDetail';
import clsx from 'clsx';
import { historyService } from '~/services';
import swal from 'sweetalert';

const OrderTable = (props) => {
    const style = (text) => {
        switch (text) {
            case 'Đã đặt hàng':
            case 'Đặt hàng':
                return 'text-green-400';
            case 'Đang giao hàng':
                return 'text-blue-400';
            case 'Đã hủy':
                return 'text-red-400';
            case 'Đã nhận':
                return 'text-gray-400';
        }
    };
    const resStatus = {
        status: 'đặt hàng',
    }
    const [orderDetail, setOrderDetail] = useState({ index: -1, id: null });
    const [status, setStatus] = useState(() => {
        return props.data.map((order) => ({ id: order.id, status: order.status })) || [];
    });
    const handleCancel = async (e) => {
        if (confirm('Bạn có muốn hủy đơn hàng không?')) {
            const id = e.target.id;
            const data = JSON.stringify({ status: 'Đã hủy' });
            const res = await historyService.updateHistoryOrder(id, data);
            if (res) {
                swal({text: 'Hủy đơn hàng thành công', icon: 'success',});
                setStatus((e) => {
                    const data = e.map((order) => {
                        return order.id == id
                            ? { id: order.id, status: 'Đã hủy' }
                            : { id: order.id, status: order.status };
                    });
                    return data;
                });
            }
        }
    };
    return (
        <Table hoverable={true} className="">
            <caption className="text-left p-4 font-semibold text-2xl">Đơn hàng đã mua gần đây</caption>
            <Table.Head className="">
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
            <Table.Body className="divide-y overflow-hidden">
                {props.data.map((order, index) => {
                    const styleStatus = style(order.status);
                    const displayDetail = index === orderDetail.index;
                    const displayCancelBtn = resStatus.status != 'Đặt hàng';
                    const styleDisable = 'bg-gray-100';
                    return (
                        <Fragment key={index}>
                            <Table.Row className="bg-white dark:border-gray-700 dark:bg-gray-800 overflow-hidden">
                                <Table.Cell className="text-blue-400">#{order.id}</Table.Cell>
                                <Table.Cell className="text-blue-400 hover:text-blue-700 select-none">
                                    <button
                                        onClick={() =>
                                            setOrderDetail((current) => {
                                                return current.index === index
                                                    ? { index: -1, id: order.id }
                                                    : { index, id: order.id };
                                            })
                                        }
                                    >
                                        Xem chi tiết
                                    </button>
                                </Table.Cell>
                                <Table.Cell>{order.totalQuantity}</Table.Cell>
                                <Table.Cell className="text-red-400">{numberWithCommas(order.totalPrice)}₫</Table.Cell>
                                <Table.Cell>
                                    <p className="">{order.createdAt}</p>
                                </Table.Cell>
                                <Table.Cell className={styleStatus}>
                                    <span className="mr-4">{resStatus.status}</span>
                                    {order?.payAt ? (
                                        <span className="text-white text-xl bg-green-500 p-2 rounded-lg">
                                            Đã thanh toán
                                        </span>
                                    ) : (
                                        <span className="text-white text-xl bg-gray-500 p-2 rounded-lg">
                                            Chưa thanh toán
                                        </span>
                                    )}
                                </Table.Cell>
                                <Table.Cell>
                                    <button
                                        disabled={displayCancelBtn}
                                        id={order.id}
                                        onClick={handleCancel}
                                        className={clsx(
                                            'bg-red-500 text-2xl font-medium p-4 rounded-lg  text-white',
                                            displayCancelBtn && '!bg-gray-100 !text-gray-700',
                                        )}
                                    >
                                        Hủy
                                    </button>
                                </Table.Cell>
                            </Table.Row>
                            {displayDetail && (
                                <Table.Row>
                                    <Table.Cell className="" colSpan="7">
                                        <OrderDetail {...order} />
                                    </Table.Cell>
                                </Table.Row>
                            )}
                        </Fragment>
                    );
                })}
            </Table.Body>
        </Table>
    );
};

export default OrderTable;
