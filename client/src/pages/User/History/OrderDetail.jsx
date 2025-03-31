import { numberWithCommas } from '~/utils';
import { CheckCircleFill } from 'react-bootstrap-icons';
import { useDispatch, useSelector } from 'react-redux';
import { postOrder } from '~/redux/order/orderSlice';
import { Link, useNavigate } from 'react-router-dom';
import { useEffect } from 'react';
import { getOrdersById } from '../../../redux/order/ordersApi';
import { EGender } from '../../../utils';

function OrderDetail(props) {
    const dispatch = useDispatch();
    const navigate = useNavigate();
    useEffect(() => {
        getOrdersById(dispatch, props.id);
    }, [props.id]);
    const orderDetail = useSelector((state) => state.orders.orderDetail.data);
    const customer = props;
    const orderItems = orderDetail?.orderItems || []; //props.order_items.data;
    const deliveryTime = props?.deliveryTime || '3/8/2022';
    const amountPaid = props.totalPrice;
    const surcharge = props?.surcharge || 20000;
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
    const handlePayment = () => {
        dispatch(postOrder(props));
        navigate('/order');
    };
    const checkPayment = props.status != 'Đã hủy' && props.payment.paid == false;
    return (
        <div>
            <div className="p-8 border-b">
                <div className="flex justify-between">
                    <h2 className="font-bold text-3xl">Chi tiết đơn hàng: #{props.id}</h2>
                    <p className="text-2xl">
                        Trạng thái: <span className={style(props.status)}>{props.status}</span>
                        {checkPayment && (
                            <p className="cursor-pointer text-blue-400" onClick={handlePayment}>
                                Tiến hành thanh toán
                            </p>
                        )}
                    </p>
                </div>
                <p className="text-3xl">Mua tại Thegioididong.com</p>
            </div>
            {orderItems.map((item, index) => {
                return (
                    <div className="flex justify-between py-4 border-b" key={index}>
                        <div className="flex">
                            <div className="w-56 h-56">
                                <img className="object-cover" src={item.product.img} alt={item.product.title} />
                            </div>
                            <div>
                                <p className="font-semibold text-3xl">{item.product.title}</p>
                                {/* <p>Màu: {item.color}</p> */}
                                <p>Số lượng: {item.quantity}</p>
                            </div>
                        </div>
                        {item.sale ? (
                            <div>
                                <p className="text-red-400">{numberWithCommas(item.price * (1 - item?.sale))}</p>
                                <p className="line-through">{numberWithCommas(item.totalPrice)}₫</p>
                            </div>
                        ) : (
                            <div>
                                <p className="font-bold">{numberWithCommas(item.totalPrice)}₫</p>
                            </div>
                        )}
                    </div>
                );
            })}
            <div className="border-b p-4 text-3xl leading-[40px]">
                <p>Giá tạm tính: {numberWithCommas(props.totalPrice)}₫</p>
                <p>
                    <span className="">Phụ phí: </span> <span>+{numberWithCommas(surcharge)}₫</span>
                </p>
                <p>
                    <span className="font-bold">Tổng tiền: </span>
                    <span className="text-red-500">{numberWithCommas(amountPaid + surcharge)}₫</span>
                </p>
                <p>
                    <CheckCircleFill className="text-blue-500" />
                    <span className="font-bold"> Số tiền đã thanh toán: </span>
                    {props.payment.paid && (
                        <span className="text-red-400">{numberWithCommas(amountPaid + surcharge)}₫</span>
                    )}
                    {checkPayment && (
                        <>
                            <span>Chưa thanh toán</span> <a className="text-blue-400">Tiến hành thanh toán</a>
                        </>
                    )}
                </p>
            </div>
            <div className="border-b p-4 text-3xl leading-[40px]">
                <p className="font-bold text-3xl">Địa chỉ và thông tin người nhận hàng</p>
                <ul>
                    <li>
                        {EGender.getNameFromIndex(customer.gender)} {customer.username} - {customer.phone}
                    </li>
                    <li>
                        Địa chỉ nhận hàng {customer.address.ward} {customer.address.district} {customer.address.city}
                    </li>
                    <li>Thời gian nhận hàng: {deliveryTime}</li>
                </ul>
            </div>
            <div className="hidden flex justify-center py-4">
                <button className="bg-blue-400 rounded-xl p-4">Quay lại danh sách đơn hàng</button>
            </div>
        </div>
    );
}

export default OrderDetail;
