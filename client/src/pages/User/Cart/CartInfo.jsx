import { useEffect, useRef, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { ChevronLeft, ChevronDown, TicketPerforated } from 'react-bootstrap-icons';
import ProductItem from './ProductItem';
import Input from './Input';
import { numberWithCommas } from '~/utils';
import { LocationForm } from '~/components/LocationForm';
import { clearCart } from '../../../redux/shopping-cart/cartItemsSlide';
import moment from 'moment';
import './Cart.scss';
import { useCart } from '../../../hooks';
import { useDispatch, useSelector } from 'react-redux';
import {
    DEFAULT_VARIABLE,
    EDiscountType,
    EGender,
    EPayment,
    EShippingMethod,
    REGEXP,
    toAddressSlug,
} from '../../../utils';
import { createOrder } from '../../../redux/order/orderSlice';
import { discountService, productService } from '../../../services';
import { AccordionActions, Portal } from '@material-ui/core';
import {
    addCheckoutDiscount,
    clearCheckoutDiscount,
    updateAllCheckoutDiscount,
} from '../../../redux/discount/discountsSlice';
import { getAllDiscountByShopId, getAllDiscountByUser } from '../../../redux/discount/discountsApi';
import swal from 'sweetalert';

function CartInfo() {
    const $ = document.querySelector.bind(document);
    const portalRef = useRef(null);
    const { cartItems, totalPrice, totalQuantity } = useCart();
    const [addressOption, setAddresOption] = useState();
    const [currentDiscount, setCurrentDiscount] = useState(null);
    const [totalPriceDiscount, setTotalPriceDiscount] = useState(0);
    const discounts = useSelector((state) => state.discounts.checkoutDiscounts.data);
    const [checkAvailable, setCheckAvailable] = useState(false);
    const dispatch = useDispatch();
    const navigate = useNavigate();
    const getUser = JSON.parse(localStorage.getItem('customerInfo'));
    const [orderDetail, setOrderDetail] = useState({
        gender: EGender.UNKNOWN.index,
        fullName: '',
        email: '',
        phone: '',
        address: {
            homeAdd: '',
            ward: '',
            district: '',
            city: '',
        },
        payment: EPayment.CASH.index,
        shippingMethod: EShippingMethod.GHN_EXPRESS.index,
        discountCode: '',
        paid: false,
        note: '',
        orderItems: [],
    });
    let orderItem = {
        name: '',
        productId: 0,
        quantity: 0,
        percent: 0,
        saleName: '',
        note: '',
    };
    console.log(cartItems, 'cartItems');
    const ok = async () => {};
    const handleSubmit = (e) => {
        e.preventDefault();
        // let checkAvailable = true;
        if (getUser) {
            cartItems.map(async (item) => {
                let getItem = await productService.getProductById(item.id);
                if (getItem.data.availableQuantity < item.quantity) {
                    swal({ text: `Sản phẩm trong kho không đủ cho sản phẩm ${item.title}.`, icon: 'error' });
                    setCheckAvailable(false);
                }
            });
            if (checkAvailable) {
                const fullName = document.getElementById('fullname').value;
                const phone = document.getElementById('phone').value;
                let homeAdd = document.getElementById('homeAddress').value;
                const sex = document.getElementsByName('sex');
                const discountCode = document.getElementById('ticketid').value;
                const note = document.getElementById('anotheroption').value;
                let sexValue;
                for (let i = 0, length = sex.length; i < length; i++) {
                    if (sex[i].checked) {
                        sexValue = sex[i].value;
                    }
                }
                let orderItemDetails = cartItems.map((value) => ({
                    productId: value,
                    quantity: value.quantity,
                    saleName: value.tag,
                    note: value?.note ? value.note : '',
                }));
                let data = {
                    ...orderDetail,
                    gender: sexValue === null || sexValue === undefined ? EGender.UNKNOWN : Number.parseInt(sexValue),
                    fullName: fullName,
                    phone: phone,
                    address: {
                        homeAdd: homeAdd,
                        ward: addressOption.ward,
                        district: addressOption.district,
                        city: addressOption.city,
                    },
                    discountIds: discounts,
                    totalPriceDiscount: totalPriceDiscount,
                    note: note,
                    orderItems: orderItemDetails,
                    totalPriceProduct: totalPrice,
                };
                dispatch(createOrder(data));
                dispatch(clearCart());
                navigate('/order');
            } else {
                setCheckAvailable(true);
            }
        } else {
            swal({ text: `Vui lòng đăng nhập trước khi mua hàng`, icon: 'warning' });
        }
    };
    useEffect(() => {
        if (getUser) {
            const setCustomerInfo = () => {
                let customerInfo = localStorage.getItem('customerInfo');
                if (customerInfo) {
                    customerInfo = JSON.parse(customerInfo);

                    // console.log(customerInfo);
                    // console.log(customerInfo);
                    let fullName = (document.getElementById('fullname').value =
                        customerInfo?.fullName === null ||
                        customerInfo?.fullName === undefined ||
                        customerInfo?.fullName.trim() === ''
                            ? 'Ẩn Danh'
                            : customerInfo?.fullName);
                    let phone = (document.getElementById('phone').value = customerInfo.phone);
                    let email = (document.getElementById('email').value = customerInfo.email);
                    document.getElementById('homeAddress').value = customerInfo?.address?.homeAdd || '';
                    let address = customerInfo?.address || {
                        homeAdd: '',
                        ward: '',
                        district: '',
                        city: '',
                    };
                    setOrderDetail((prev) => ({
                        ...prev,
                        fullName: fullName,
                        phone: phone,
                        email: email,
                        address: address,
                    }));
                }
            };
            setCustomerInfo();
            getAllDiscountByUser(dispatch);
        }
    }, []);
    useEffect(() => {
        if (discounts && discounts.length > 0) {
            let shopIdListFromCartItem = cartItems.reduce(
                (arr, item) => (arr.includes(item?.shop?.id) ? arr : [...arr, item?.shop?.id]),
                [],
            );
            if (shopIdListFromCartItem.length > 0) {
                let newCheckoutDiscounts = discounts.filter((item) => shopIdListFromCartItem.includes(item.shopId));
                dispatch(updateAllCheckoutDiscount(newCheckoutDiscounts));
            } else {
                dispatch(clearCheckoutDiscount());
            }
        }
    }, [cartItems]);
    const handleCheckDiscountCode = async (e, codeClick = '') => {
        let code = codeClick || $('#ticketid').value;
        if (
            code.length >= DEFAULT_VARIABLE.MIN_DISCONT_CODE_LENGTH &&
            code.length <= DEFAULT_VARIABLE.MAX_DISCONT_CODE_LENGTH
        ) {
            let res = await discountService.checkDiscountCode(code);
            let discount = res?.data;
            if (discount !== null) {
                if (!codeClick) {
                    e.preventDefault();
                }
                $('#ticketid')?.setCustomValidity('');
                if (cartItems.some((item) => item.shop?.id === discount.shopId)) {
                    if (!discounts.includes(discount)) {
                        console.log('discount', discount);
                        setCurrentDiscount(discount);
                        $('#ticketid')?.setCustomValidity('');
                    } else {
                        $('#ticketid')?.setCustomValidity('Đã có mã giảm giá này trong danh sách');
                    }
                } else {
                    $('#ticketid')?.setCustomValidity(
                        'Mã giảm giá không áp dụng cho các shop của các sản phẩm hiện tại',
                    );
                }
                return;
            }
            $('#ticketid')?.setCustomValidity('Mã giảm giá không hợp lệ');
        }
    };
    const handleAddDiscountCode = async (e, vouncher = null) => {
        let discount = vouncher || currentDiscount;
        console.log('discount: ', discount, currentDiscount);
        if (discount !== null) {
            if (cartItems.some((item) => item.shop?.id === discount.shopId)) {
                dispatch(addCheckoutDiscount(discount));
                $('#ticketid').value = '';
                let message = `Thêm mã giảm giá với code ${discount.code} ${
                    discount.type === EDiscountType.DISCOUNT_SHOP_PRICE.index
                        ? `giảm ${numberWithCommas(discount.price)}đ`
                        : `giảm ${discount.percent * 100}%${
                              discount.cappedAt !== null ? `, tối đa ${numberWithCommas(discount.cappedAt)}đ` : ''
                          }`
                } với đơn tối thiểu ${numberWithCommas(discount.minSpend) || 0}đ thành công!`;
                swal({ text: message, icon: 'warning' });
            }
            setCurrentDiscount(null);
        }
    };
    const getCartItemsGroupByShop = () => {
        return cartItems.reduce((acc, item) => {
            let shopCart = acc.find((shopCart) => shopCart?.id === item?.shop?.id) || null;
            if (shopCart === null) {
                return [...acc, { ...item?.shop, cartItems: [item] }];
            } else {
                acc = acc.filter((shop) => shop?.id !== shopCart?.id);
                return [...acc, { ...shopCart, cartItems: [...shopCart.cartItems, item] }];
            }
        }, []);
    };
    const handleClickVouncher = (vouncher, e = '') => {
        handleAddDiscountCode(e, vouncher);
    };
    const discountsUser = useSelector((state) => state.discounts.allDiscountsUser.data);
    useEffect(() => {
        let cartItemsGroupByShop = getCartItemsGroupByShop();
        let totalPriceDiscount = 0;
        for (let discount of discounts) {
            if (
                discount.type !== EDiscountType.DISCOUNT_SHOP_PRICE.index &&
                discount.type !== EDiscountType.DISCOUNT_SHOP_PERCENT.index
            ) {
                continue;
            }
            let items = cartItemsGroupByShop.find((shopCart) => shopCart?.id === discount.shopId)?.cartItems || [];
            let totalPrice = 0;
            for (let item of items) {
                totalPrice += item.price;
            }
            if (discount.type === EDiscountType.DISCOUNT_SHOP_PRICE.index) {
                if (discount.minSpend === null || totalPrice >= discount.minSpend) {
                    totalPriceDiscount += discount.price || 0;
                }
            } else {
                if (discount.minSpend === null || totalPrice >= discount.minSpend) {
                    totalPriceDiscount +=
                        discount.cappedAt === null
                            ? totalPrice * discount.percent
                            : totalPrice * discount.percent <= discount.cappedAt
                            ? totalPrice * discount.percent
                            : discount.cappedAt;
                }
            }
        }
        setTotalPriceDiscount(totalPriceDiscount);
    }, [cartItems, discounts]);
    return (
        <div className="m-auto">
            <div className="ml-8 py-4">
                <Link to="/" className="text-blue-500">
                    <i>
                        <ChevronLeft />
                    </i>
                    Mua thêm sản phẩm khác
                </Link>
            </div>
            <form
                className="bg-white rounded-xl px-14 py-8 shadow-sm grid grid-cols-2 grid-rows-2 gap-12"
                onSubmit={handleSubmit}
            >
                <div className="col-span-1 row-span-2 flex-col items-center h-full">
                    <div className="p-4 h-full mt-auto mb-auto">
                        <h2 className="text-lue-500 mb-2 text-center font-semibold text-3xl">Đơn hàng của bạn</h2>
                        {/* {cartItems.map((product, index) => (
                            <ProductItem key={index} {...product} />
                        ))} */}
                        {getCartItemsGroupByShop()?.map((item) => {
                            return (
                                <>
                                    <div className="flex">
                                        <div className="h-[23px] w-[23px]">
                                            <img className="w-full h-full rounded-full" src={item?.avatar}></img>
                                        </div>
                                        <span className="ml-3 text-red-400">{item?.name}</span>
                                        <ul className="text-xl text-black-400 flex items-center">
                                            {discountsUser.map((vouncher) => {
                                                if (vouncher.shopId == item.id) {
                                                    return (
                                                        <li
                                                            onClick={() => handleClickVouncher(vouncher)}
                                                            className="cursor-pointer hover:opacity-40 text-green-500 ml-3 border-green-500 border border-solid rounded-2xl pl-2 pr-2"
                                                        >
                                                            {vouncher.percent
                                                                ? `- ${vouncher.percent} %`
                                                                : `-${vouncher.price} đ`}
                                                        </li>
                                                    );
                                                }
                                            })}
                                        </ul>
                                    </div>

                                    {item.cartItems.map((cartItem, index) => (
                                        <ProductItem key={index} {...cartItem} />
                                    ))}
                                </>
                            );
                        })}
                        <div className="flex justify-between py-4">
                            <span>Tạm tính ({totalQuantity} sản phẩm):</span>
                            <span> {numberWithCommas(totalPrice)}₫</span>
                        </div>
                    </div>
                </div>
                <div className="col-span-1 row-span-2 border-[1px] border-solid border-blue-200 rounded-2xl p-8">
                    <div className="my-8 py-4">
                        <h4 className="text-center my-8">THÔNG TIN KHÁCH HÀNG</h4>
                        <div className="my-4">
                            <input id="male" type="radio" name="sex" value={EGender.MALE.index} defaultChecked />
                            &nbsp;
                            <label htmlFor="male">Anh</label>
                            &emsp;
                            <input id="female" type="radio" name="sex" value={EGender.FEMALE.index} />
                            &nbsp;
                            <label htmlFor="female">Chị</label>
                        </div>
                        <div className="flex gap-4 my-8">
                            <Input
                                placeholder="Họ và Tên"
                                id="fullname"
                                required={true}
                                onChange={(e) => setOrderDetail((prev) => ({ ...prev, fullName: e.target.value }))}
                            />
                            <Input
                                placeholder="Số điện thoại"
                                id="phone"
                                type="tel"
                                required={true}
                                pattern="(84|0[3|5|7|8|9])+([0-9]{8})\b"
                                onChange={(e) => setOrderDetail((prev) => ({ ...prev, phone: e.target.value }))}
                            />
                        </div>
                        <div className="flex gap-4">
                            <Input
                                placeholder="Địa chỉ email (dùng để nhận thông báo đơn hàng ( không bắt buộc)"
                                id="email"
                                type="email"
                                required={true}
                                pattern={REGEXP.EMAIL}
                                onChange={(e) => setOrderDetail((prev) => ({ ...prev, email: e.target.value }))}
                            />
                        </div>
                    </div>
                    <div className="my-8">
                        <div>
                            <div className=" rounded-xl">
                                <p>Chọn địa chỉ để biết thời gian nhận hàng và phí vận chuyển (nếu có)</p>

                                <Input
                                    placeholder="Số nhà, tên đường"
                                    id="homeAddress"
                                    required={true}
                                    onChange={(e) =>
                                        setOrderDetail((prev) => ({
                                            ...prev,
                                            address: { ...prev.address, homeAdd: e.target.value },
                                        }))
                                    }
                                />
                                <LocationForm onChange={setAddresOption} address={toAddressSlug(orderDetail.address)} />
                            </div>
                        </div>
                    </div>
                    <div>
                        <Input
                            placeholder="Ghi chú cho người giao hàng (không bắt buộc)"
                            id="anotheroption"
                            onChange={(e) => setOrderDetail((prev) => ({ ...prev, note: e.target.value }))}
                        />
                    </div>

                    <div>
                        <div className="py-8 border-b ">
                            <button type="button" className="p-4 border rounded-lg">
                                <i>
                                    <TicketPerforated />
                                </i>
                                &nbsp;
                                <span>Sử dụng mã giảm giá</span>&nbsp;
                                <i>
                                    <ChevronDown />
                                </i>
                            </button>
                            <div ref={portalRef}></div>
                        </div>

                        <div className="flex justify-between my-4">
                            <strong>Giảm giá:</strong>
                            <strong className="text-black-600">{numberWithCommas(totalPriceDiscount)} ₫</strong>
                        </div>
                        <div className="flex justify-between my-4">
                            <strong>Tạm tính tổng tiền:</strong>
                            <strong className="text-red-600">
                                {numberWithCommas(totalPrice + totalPriceDiscount)} ₫
                            </strong>
                        </div>
                        <button
                            type="submit"
                            className="h-20 my-8 border-green-400 border border-solid rounded-lg w-full text-green-400 font-bold"
                        >
                            ĐẶT HÀNG
                        </button>
                        <small className="block text-center">
                            Bạn có thể chọn hình thức thanh toán sau khi đặt hàng
                        </small>
                    </div>
                </div>
                <Portal container={portalRef.current}>
                    <form className="flex gap-8 border p-4 rounded-xl" onSubmit={(e) => handleAddDiscountCode(e)}>
                        <Input
                            placeholder="Nhập mã giảm giá/ Phiếu mua hàng"
                            id="ticketid"
                            onBlur={handleCheckDiscountCode}
                            onChange={(e) => {
                                e.target.setCustomValidity(''),
                                    setOrderDetail((prev) => ({ ...prev, discountCode: e.target.value }));
                            }}
                        />
                        <button
                            type="submit"
                            className="py-4 px-10 border bg-blue-500 rounded-lg text-white"
                            // onClick={handleCheckDiscountCode}
                        >
                            Áp dụng
                        </button>
                    </form>
                </Portal>
            </form>
        </div>
    );
}

export default CartInfo;
