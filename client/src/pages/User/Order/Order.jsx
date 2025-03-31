import React from 'react';
import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import './Order.scss';
import { useDispatch, useSelector } from 'react-redux';
import { numberWithCommas } from '~/utils';
import { Navigate, useNavigate } from 'react-router-dom';
import { orderService } from '~/services';
import { paypal, vnpay } from '../../../services/payment';
import { EGender, EPayment, EShippingMethod, MESSAGE } from './../../../utils';
import { deleteOrdersByIdApi } from '../../../redux/order/ordersApi';
import { ghn } from '../../../services/shipping';
import { clearCheckoutDiscount } from '../../../redux/discount/discountsSlice';
import { deleteOrder } from '../../../redux/order/orderSlice';
import swal from 'sweetalert';

const Order = ({ title }) => {
    const dispatch = useDispatch();
    const navigate = useNavigate();
    const [ship, setShip] = useState({ expectedDeliveryTime: null, transportFee: 0 });
    const order = localStorage.getItem('order') ? JSON.parse(localStorage.getItem('order')) : null;
    if (!order) {
        navigate('/');
    }
    const hasOrder = order && Object.keys(order).length > 0 && order.constructor === Object;
    const handleConfirm = async () => {
        const payment = getPayment();
        let orderItems = [...order.orderItems].map((value) => ({ ...value, productId: value.productId?.id }));
        let orderShops = ship.shipGroupByShop.map(item => ({
            ...item, shippingMethod: EShippingMethod.GHN_EXPRESS.index
        }))
        let discountIds = order.discountIds.map(item => item.id);
        const data = { ...order, ...payment, discountIds, orderItems, orderShops };
        try {
            const res = await orderService.postOrder(data);
            console.log(res)
            if (res?.status === 'CREATED') {
                if (data.payment === EPayment.VNPAY.index) {
                    const dataVNPay = {
                        orderId: res?.data?.id,
                        // orderInfo: `${order.fullName} thanh toán đơn hàng ${order.id} với MoMo`,
                        fullName: order.fullName,
                        redirectUrl: window.location.origin + '/#/',
                        totalPrice: order?.totalPriceProduct || 1000 + order?.transportFee || 0,
                        // extraData: '',
                    };
                    const resVNPay = await vnpay.createVNPayPayment(dataVNPay);
                    window.location = resVNPay.data.payUrl || window.location.origin + '/#/';
                } else if (data.payment === EPayment.PAYPAL.index) {
                    const dataPayPal = {
                        orderId: res?.data?.id,
                        // orderInfo: `${order.fullName} thanh toán đơn hàng ${order.id} với MoMo`,
                        fullName: order.fullName,
                        redirectUrl: window.location.origin + '/#/',
                        totalPrice: order?.totalPriceProduct || 1000 + order?.transportFee || 0,
                        // extraData: '',
                    };
                    const resPayPal = await paypal.createPayPalPayment(dataPayPal);
                    window.location = resPayPal.data.payUrl || window.location.origin + '/#/';
                } else {
                    swal({text: 'Tạo đơn hàng thành công', icon: 'success',});
                }
            } else {
                swal({text: MESSAGE.ERROR_ACTION, icon: 'error',});
            }
        } catch (err) {
            swal({text: MESSAGE.ERROR_ACTION, icon: 'error',});
        }
        navigate('/');
        localStorage.removeItem('order');
    };
    const getPayment = () => {
        const radios = document.querySelectorAll('input[name="payment"]');
        let paymentIndex = -1;
        for (let i = 0, length = radios.length; i < length; i++) {
            if (radios[i].checked) {
                paymentIndex = i;
                break;
            }
        }
        return paymentIndex != -1
            ? { payment: Number.parseInt(radios[paymentIndex].value), paid: false }
            : { payment: EPayment.CASH.index, paid: false };
    };
    const handleCancel = async () => {
        dispatch(deleteOrder());
        navigate('/');
    };
    useEffect(() => {
        if (order && ship.expectedDeliveryTime === null) {
            const getShip = async () => {
                let shipGroupByShop = await ghn.getPreviewOrderGHN(order);
                let totalFee = shipGroupByShop.reduce((acc, item) => acc + item.totalFee, 0);
                setShip({ totalFee: totalFee, shipGroupByShop: shipGroupByShop });
                // let date = new Date(Date.parse(res?.data?.data?.expected_delivery_time));
                // setShip({
                //     expectedDeliveryTime: date,
                //     transportFee: res?.data?.data?.total_fee,
                // });
            };
            getShip();
        }
    }, [order]);
    useEffect(() => {
        document.title = title;
        dispatch(clearCheckoutDiscount());
    }, []);
    return (
        <>
            {hasOrder ? (
                <div className="order">
                    <div className="alertsuccess-new">
                        <i className="new-cartnew-success"></i>
                        <strong>Đặt hàng thành công</strong>
                    </div>
                    <div className="ordercontent">
                        <div>
                            <p>
                                Cảm ơn {EGender.getNameFromIndex(order.gender)} <b>{order.fullName}</b> vì đã có cơ hội được phục vụ.
                            </p>
                        </div>
                        <div>
                            <div className="info-order">
                                <div className="info-order-header">
                                    <h4>
                                        Đơn hàng: <span className="text-blue-400 font-bold">#{order.id}</span>
                                    </h4>
                                    <div className="header-right">
                                        <Link to="/history">Quản lý đơn hàng</Link>
                                        <div className="cancel-order-new">
                                            <div>
                                                <div className="cancel-order-new">
                                                    <span>.</span>
                                                    <label
                                                        onClick={handleCancel}
                                                        style={{ color: 'blue', cursor: 'pointer', padding: '0 0' }}
                                                    >
                                                        Hủy
                                                    </label>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <label htmlFor="">
                                    <span>
                                        <i className="info-order__dot-icon"></i>
                                        <span>
                                            <strong>Người nhận hàng:</strong>
                                            <h4 id="userName" className="mb-[8px]">
                                                {EGender.getNameFromIndex(order.gender)} {order.fullName}
                                            </h4>
                                            <strong>Số điện thoại:</strong>
                                            <h5 id="customerPhone" className="mb-[8px]">
                                                {order.phone}
                                            </h5>
                                            <strong>Email:</strong>
                                            <h5 id="customerPhone" className="mb-[8px]">
                                                {order.email}
                                            </h5>
                                        </span>
                                    </span>
                                </label>
                                <label htmlFor="">
                                    <span>
                                        <i className="info-order__dot-icon"></i>
                                        <span>
                                            <strong>Giao đến: </strong>
                                            {order.address?.homeAdd}, {order.address?.ward},{order.address?.district},
                                            Thành phố {order.address?.city} (nhân viên sẽ gọi xác nhận trước khi giao).
                                        </span>
                                    </span>
                                </label>
                                <label htmlFor="">
                                    <span>
                                        <i className="info-order__dot-icon"></i>
                                        <span>
                                            <strong>
                                                Tổng tiền hàng: {numberWithCommas(Number(order.totalPriceProduct)) || 0}
                                                {' đ'}
                                            </strong>
                                        </span>
                                    </span>
                                </label>
                                <label htmlFor="">
                                    <span>
                                        <i className="info-order__dot-icon"></i>
                                        <span>
                                            <strong>
                                                Phí vận chuyển: {numberWithCommas(Number(ship.totalFee)) || 0}
                                                {' đ'}
                                            </strong>
                                        </span>
                                    </span>
                                </label>
                                <label htmlFor="">
                                    <span>
                                        <i className="info-order__dot-icon"></i>
                                        <span>
                                            <strong>
                                                Giảm giá: {numberWithCommas(129000) || 0}
                                                {' đ'}
                                            </strong>
                                        </span>
                                    </span>
                                </label>
                                <label htmlFor="">
                                    <span>
                                        <i className="info-order__dot-icon"></i>
                                        <span>
                                            <strong>Tổng thanh toán: {'   '}</strong>
                                            <strong className="text-red-500 text-5xl">
                                                {numberWithCommas(
                                                    Number(ship.totalFee) + Number(order.totalPriceProduct) - Number(order.totalPriceDiscount) || 0
                                                ) || 0}{' '}
                                                {' đ'}
                                            </strong>
                                        </span>
                                    </span>
                                </label>
                            </div>
                        </div>

                        <div>
                            <h4 className="order-infor-alert">Đơn hàng chưa được thanh toán</h4>
                        </div>

                        <div className="payment-method-new">
                            <div>
                                <h3>Chọn hình thức thanh toán:</h3>
                                <ul className="formality-pay-new">
                                    <li className="normal-payment">
                                        <div className="text-payment">
                                            <span>
                                                <input
                                                    type="radio"
                                                    id="cash"
                                                    name="payment"
                                                    value={EPayment.CASH.index}
                                                    defaultChecked
                                                />
                                                <label htmlFor="cash">Thanh toán tiền mặt khi nhận hàng</label>
                                            </span>
                                        </div>
                                    </li>

                                    

                                    <li className="normal-payment">
                                        <div className="text-payment">
                                            <span>
                                                <input
                                                    type="radio"
                                                    id="vnpay"
                                                    name="payment"
                                                    value={EPayment.VNPAY.index}
                                                />
                                                <label htmlFor="vnpay">Thanh toán qua VNPay</label>
                                            </span>
                                        </div>
                                    </li>
                                    <li className="normal-payment">
                                        <div className="text-payment">
                                            <span>
                                                <input
                                                    type="radio"
                                                    id="paypal"
                                                    name="payment"
                                                    value={EPayment.PAYPAL.index}
                                                />
                                                <label htmlFor="paypal">Thanh toán qua Paypal</label>
                                            </span>
                                        </div>
                                    </li>

                                    {/**<li className="normal-payment">
                                        <div className="text-payment">
                                            <span>
                                                <input type="radio" id="ck" name="payment" value="banking" />
                                                <label htmlFor="ck">Chuyển khoản ngân hàng</label>
                                            </span>
                                        </div>
                                    </li>

                                    <li className="normal-payment">
                                        <div className="text-payment">
                                            <span>
                                                <input type="radio" id="atm" name="payment" value="atm" />
                                                <label htmlFor="atm">Qua thẻ ATM (có Internet Banking)</label>
                                            </span>
                                        </div>
                                    </li>

                                    <li className="normal-payment">
                                        <div className="text-payment">
                                            <span>
                                                <input type="radio" id="qr" name="payment" value="qrcode" />
                                                <label htmlFor="qr">Qua QR Code</label>
                                            </span>
                                        </div>
                                    </li>

                                    <li className="normal-payment">
                                        <div className="text-payment">
                                            <span>
                                                <input type="radio" id="visa" name="payment" value="visa" />
                                                <label htmlFor="visa">Qua thẻ quốc tế Visa, Master, JCB</label>
                                            </span>
                                        </div>
                                    </li>

                                    <li className="normal-payment">
                                        <div className="text-payment">
                                            <span>
                                                <input type="radio" id="moca" name="payment" value="moca" />
                                                <label htmlFor="moca">Qua Ví điện tử Moca trên Grab</label>
                                            </span>
                                        </div>
                                    </li>
                                    <li className="normal-payment">
                                        <div className="text-payment">
                                            <span>
                                                <input type="radio" id="machine" name="payment" value="marchinecard" />
                                                <label htmlFor="machine">Nhân viên mang máy cà thẻ khi nhận hàng</label>
                                            </span>
                                        </div>
                                    </li> */}
                                </ul>

                                <button onClick={handleConfirm} className="confirm-payment-button">
                                    Xác nhận
                                </button>
                            </div>
                            <div className="refund-popup">
                                <a href="">Xem chính sách hoàn tiền online</a>
                            </div>
                            <hr />

                            <div className="buyanotherNew">
                                <Link to="/"> Mua thêm sản phẩm khác </Link>
                            </div>
                            <span className="customer-rating">
                                <div className="customer-rating__top">
                                    <div className="customer-rating__top__desc">
                                        {EGender.getNameFromIndex(order.gender)} <strong>{order.fullName}</strong> có
                                        hài lòng về trải nghiệm mua hàng?
                                    </div>
                                    <div className="customer-rating__top__rating-buttons">
                                        <button className="customer-rating__top__rating-buttons__good">
                                            <p>Hài lòng</p>
                                            <i className="iconrating-good"></i>
                                        </button>
                                        <button className="customer-rating__top__rating-buttons__bad">
                                            <p>Không hài lòng</p>
                                            <i className="iconrating-bad"></i>
                                        </button>
                                    </div>
                                </div>
                            </span>
                        </div>
                    </div>
                </div>
            ) : (
                <Navigate to="/cart" />
            )}
        </>
    );
};

export default Order;
