import { useState } from 'react';
import { numberWithCommas } from '~/utils';
import { CheckCircleFill } from 'react-bootstrap-icons';
import { Modal, Button, Alert } from 'flowbite-react';
import { Link } from 'react-router-dom';
import { CounterQuantity, SelectColor } from '~/components/Selector';
import { discountInfo, discountMore } from './DiscountContent';
import { useDispatch, useSelector } from 'react-redux';
import { addItem } from '~/redux/shopping-cart/cartItemsSlide';

function AlertMess({ type, mess }) {
    return (
        <div>
            {type && (
                <Alert
                    color={type}
                    onDismiss={function onDismiss() {
                        return;
                    }}
                >
                    <span>{mess}</span>
                </Alert>
            )}
        </div>
    );
}
function PayInfo(props) {
    const initProductDetail = useSelector((state) => state.products.productDetail.data);
    const { price, discount, tag, title, slug, img, colors, brand, category,originPrice,shop } = initProductDetail;

    const pays = [{ bank: 'vnpay' }, { bank: 'tpbank' }, { bank: 'eximbank' }];
    const [modalShow, setModalShow] = useState(false);

    const [alertMess, setAlertMess] = useState({});

    const onClick = () => {
        setModalShow(!modalShow);
    };

    const dispatch = useDispatch();
    const addToCart = (data) => {
        if (dispatch(addItem(data))) {
            setAlertMess({ type: 'success', mess: 'Thêm thành công' });
        } else {
            setAlertMess({ type: 'failure', mess: 'Change a few things up and try submitting again.' });
        }
        setTimeout(() => setAlertMess({}), 1500);
    };

    const color = colors ? colors[0] : '';
    let productForCart = { price, discount, tag, title, slug, img, quantity: 1, color, brand, category,originPrice,shop };
    const handleClickPay = () => {
        addToCart(productForCart);
    };
    return (
        <div className="">
            <div>
                <p>Giá tại </p>
                <div>
                    <strong>{numberWithCommas(price * (1 - discount))}₫ *</strong>
                    <span className="line-through">{numberWithCommas(price)}₫</span>
                    <span>(-{discount * 100}%)</span>
                    <span>Trả góp 0%</span>
                </div>
            </div>
            <div className="border border-gray-400 text-2xl">
                <div className="bg-gray-100 p-4 border-b border-gray-400">
                    <strong>Khuyến mãi</strong>
                    <p>Giá và khuyến mãi dự kiến áp dụng đến 23:00 31/07</p>
                </div>
                <ul className="p-4">
                    {discountInfo.map((item, index) => {
                        return (
                            <li className="my-6" key={index}>
                                <span className="bg-blue-300 rounded-full h-8 w-8 inline-block text-center text-white text-lg">
                                    {index + 1}
                                </span>
                                &emsp;
                                <span>{item}</span>
                            </li>
                        );
                    })}
                </ul>
                <p className="p-2">(*) Giá hoặc khuyến mãi không áp dụng trả góp lãi suất đặc biệt (0%, 0.5%, 1%)</p>
            </div>
            <div>
                <p>Giao tới Thị trấn Cái Dầu, Huyện Châu Phú, An Giang Đổi</p>
                <p>Giao hàng Từ 10h - 12h hôm nay (21/07)</p>
                <p>Phí giao hàng: 45.000₫</p>
            </div>
            <div className="m-4">
                <strong>Ưu đãi khi thanh toán</strong>
                <div className="overflow-scroll no-scrollbar ">
                    <div className="flex gap-4 w-fit">
                        {pays.map((item) => {
                            return (
                                <div
                                    className="rounded-lg text-xl border rounded border-gray-300 p-4 w-96"
                                    key={item.bank}
                                >
                                    <input type="radio" name="pay" />
                                    &nbsp;
                                    <img
                                        className="inline-block w-16 object-cover"
                                        src="https://cdn.tgdd.vn/mwgcart/mwgcore/ContentMwg/images/logo/vnpay.png"
                                        alt=""
                                    />
                                    <p>
                                        Giảm <b>500.000₫</b>
                                    </p>
                                    <p>Sản phẩm iPhone</p>
                                </div>
                            );
                        })}
                    </div>
                </div>
            </div>
            <div>
                <button
                    className="bg-yellow-300  w-full mb-4 block p-6 rounded-lg text-white font-bold"
                    onClick={() => setModalShow(true)}
                >
                    MUA NGAY
                </button>
                <Modal show={modalShow} onClose={() => setModalShow(false)} size="4xl">
                    <Modal.Header>
                        <div className="p-4 text-2xl">
                            <p className="">{title}</p>
                            <strong className="text-red-400">{numberWithCommas(price * (1 - discount))}₫</strong>
                            <span className="line-through">{numberWithCommas(price)}₫</span>
                            <div className="flex-1 w-60 mt-2">
                                <img src={img} alt="" />
                            </div>
                        </div>
                    </Modal.Header>
                    <Modal.Body>
                        <AlertMess {...alertMess} />
                        <div className="space-y-6">
                            <p>Chọn màu:</p>
                            <SelectColor
                                colors={colors}
                                onChange={(color) => {
                                    productForCart = { ...productForCart, color: color };
                                }}
                            />
                            <p>Chọn số lượng:</p>
                            <CounterQuantity
                                value={1}
                                onChange={(quantity) => {
                                    productForCart = { ...productForCart, quantity: quantity };
                                }}
                            />
                        </div>
                    </Modal.Body>
                    <Modal.Footer>
                        <div className="w-full">
                            <button
                                className="bg-yellow-300  w-full mb-4 block p-6 rounded-lg text-white font-bold"
                                onClick={handleClickPay}
                            >
                                THÊM VÀO GIỎ HÀNG
                            </button>
                            <Link to="/cart" className="text-blue-500 block w-full text-center">
                                Xem giỏ hàng
                            </Link>
                        </div>
                    </Modal.Footer>
                </Modal>

                <div className="flex gap-2 text-xl">
                    <button className="bg-blue-500 p-4 w-1/2 rounded-lg text-white">
                        <p>MUA TRẢ GÓP 0%</p>
                        <p>Duyệt hồ sơ trong 5 phút</p>
                    </button>
                    <button className="bg-blue-500 p-4 w-1/2 rounded-lg text-white">
                        <p>TRẢ GÓP QUA THẺ</p>
                        <p>Visa, Mastercard, JCB, Amex</p>
                    </button>
                </div>
            </div>
            <p className="text-center">Gọi đặt mua 1800.1060 (7:30 - 22:00)</p>
            <div className="border border-gray-400 text-2xl">
                <div className="bg-gray-100 p-4 border-b border-gray-400">
                    <p>
                        <strong>4 ưu đãi thêm</strong> Dự kiến áp dụng đến 23:00 31/07
                    </p>
                </div>
                <ul className="p-4">
                    {discountMore.map((item, index) => {
                        return (
                            <li className="my-6" key={index}>
                                <i className="text-blue-400">
                                    <CheckCircleFill />
                                </i>
                                &emsp;
                                <span>{item}</span>
                            </li>
                        );
                    })}
                </ul>
            </div>
        </div>
    );
}

export default PayInfo;
