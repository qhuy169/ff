import { useEffect, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { Link, useNavigate } from 'react-router-dom';
import { productService } from '~/services';
import PieChartComponent from '../../../components/Chat/PieChartComponent';
import { DEFAULT_STORE } from '../../../utils';
import ProductRating from './../../../components/Rating/index';
import swal from 'sweetalert';

function ProductBody() {
    const navigate = useNavigate();
    const initProductDetail = useSelector((state) => state.products.productDetail.data);
    const { id, price, discount, tag, title, slug, img, colors, brand, category, parameter, info } = initProductDetail;

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
    const handleClickPay = () => {
        addToCart({ ...initProductDetail, quantity: 1 });
    };
    const handleClickChat = (e) => {
        e.preventDefault();
        const user = localStorage.getItem(DEFAULT_STORE.TOKEN);
        if (user) {
            navigate(`/chat/${initProductDetail.shop?.user?.id}?product=${initProductDetail.id}`)
        } else {
            swal({title: 'Hãy đăng nhập trước khi thực hiện tính năng này!', icon: 'warning'})
        }
    }

    const [products, setProducts] = useState([]);
    useEffect(() => {
        async function getProducts() {
            const res = await productService.queryProduct(
                ['brand', brand],
                ['category', 'dienthoai'],
                ['_start', '1'],
                ['_limit', '10'],
            );
            setProducts(res.data?.content ? res.data?.content : []);
        }
        getProducts();
    }, [brand]);

    const Art = () => {
        return <div dangerouslySetInnerHTML={{ __html: info }} />;
    };
    const test = {
        productId: 2,
        avgScore: 2.0,
        date: null,
        sentiment: "Positive",
        totalSentiment: 4,
        sentimentDetails: [
            {
                score: 0,
                total: 0,
                percent: 10,
                sentiment: "Nagetive"
            },
            {
                score: 1,
                total: 0,
                percent: 20,
                sentiment: "Neutral"
            },
            {
                score: 2,
                total: 4,
                percent: 70,
                sentiment: "Positive"
            }
        ]
    }
    const tags = ['iPhone', '6 GB', '128 GB', 'Chụp ảnh, quay phim', 'iPhone 13 (Mini, Pro, Pro Max)'];
    return (
        <section className="product__body">
            <div className="grid wide product__body-container">
                <div className="product__body-left-wrap">
                    <div className="product__body-left">
                        <div className="product__details-title-wrap">
                            <h1 className="product__details-title">Thông tin Shop</h1>
                        </div>
                        <div className="product__details-list">

                            <Link to={`/ShopInfo/${initProductDetail?.shop?.slug}`} className="w-[100px] h-[100px]">
                                <img
                                    src={initProductDetail?.shop?.avatar}
                                    alt=""
                                    className="home-product-item__img rounded-full w-[100px] h-[100px]"
                            
                                ></img>
                            </Link>
                            <h5 className="home-product-item__name">{initProductDetail?.shop?.name}</h5>
                            <div className="home-product-item__price">
                                <div className="flex flex-col gap-8 home-product-item__price-new">
                                    <span>Email: {initProductDetail?.shop?.email}</span>
                                    <span className="home-product-item__price-new-prices">
                                        Location: {initProductDetail?.shop?.address?.city}
                                    </span>
                                </div>
                            </div>

                            <Link
                                to={{
                                    pathname: `/chat/${initProductDetail.shop?.user?.id}`,
                                    search: `?product=${initProductDetail.id}`,
                                }}
                                className="select-none text-[23px] px-6 py-2 font-semibold rounded shadow-lg shadow-purple-600/50"
                                onClick={handleClickChat}
                            >
                                Chat
                            </Link>
                        </div>

                        <div className="product__desc">
                            <div className="product__details-title-wrap">
                                <h1 className="product__details-title">MÔ TẢ SẢN PHẨM</h1>
                            </div>
                            
                            <div className="product__desc-para-wrap">
                                {info ? (
                                    <Art />
                                ) : (
                                    <p className="product__desc-para">
                                        ⚠️ CAM KẾT 1 ĐỔI 1 TRONG 7 NGÀY NẾU CÓ LỖI CỦA SHOP ⚠️ Giao hàng tận nơi trên
                                        toàn quốc, Nhận hàng nhanh chóng tại nhà. PHỤ KIỆN XANH 247 xin giới thiệu sản
                                        phẩm: Tai nghe mèo bluetooth không dây đèn led, headphone chụp tai chống ồn CAO
                                        CẤP gấp gọn, âm bass mạnh mẽ, thời lượng PIN KHỦNG 400mAh, có micro chìm - CAM
                                        KẾT bảo hành 6 tháng NHẬP MÃ PHUKIEN50 GIẢM 25K TRỰC TIẾP ĐƠN HÀNG *THÔNG TIN
                                        SẢN PHẨM: Tai nghe mèo, Headphone chụp tai bluetooth không dây - Sản phẩm bao
                                        gồm: 1 Tai nghe và 1 dây sạc đi kèm - Tai nghe bluetooth tai mèo không chỉ là 1
                                        phụ kiện điện thoại thông thường nó là 1 phụ kiện thời trang vô cùng đẹp với 5
                                        tông màu đa dạng phù hợp với nhiều cá tính. Phù hợp với tất cả các nam thanh nữ
                                        tú khi ra đường cũng như học trực tuyến, dùng để nghe nhạc, chơi game, xem
                                        phim.... 👉GIAO TẬN TAY KHÁCH HÀNG MỚI THU TIỀN 100% Luôn cho khách KIỂM TRA HÀNG trước khi thanh toán 100% MIỄN
                                        PHÍ đổi trả nếu hàng lỗi, không đúng sản phẩm như đã đặt 100% Giao hàng TOÀN
                                        QUỐC, chỉ 3-5 ngày nhận được hàng #tai #nghe #bluetooth #khong #day #headphone
                                        #chup #tai #meo #tainghe #khongday #tainghemeo #tainghekhongday
                                        #tainghebluetooth #dethuong #mini #tainghegiare #tainghedep #cute{' '}
                                    </p>
                                )}
                            </div>
                        </div>
                    </div>

                    <ProductRating></ProductRating>
                    {/* <Comment></Comment> */}
                </div>

                <div className="product__body-right">
                   <PieChartComponent sentiment={initProductDetail?.sentiment}></PieChartComponent>

                    <h2 className="product__best-seller-title">Mô tả sản phẩm</h2>

                    <div className="product__best-seller-list">
                        <a className="home-product-wrap">
                            <div className="home-product-item">
                                {parameter ? (
                                    <div className="product__details-list">
                                        {Object.entries(parameter).map((param, index) => {
                                            if (index != 0) {
                                                return (
                                                    // <tr className={clsx(index % 2 === 0 && 'bg-gray-100')} key={index}>
                                                    //     <td colSpan="4">{param[0]}</td>
                                                    //     <td colSpan="6">{param[1]}</td>
                                                    // </tr>
                                                    <div className="product__details-item flex">
                                                        <span className="product__details-item-title">{param[0]}</span>
                                                        <span className="product__details-item-desc">{param[1]}</span>
                                                    </div>
                                                );
                                            }
                                        })}
                                    </div>
                                ) : (
                                    <div className="product__details-list">
                                        <div className="product__details-item flex items-center h-full">
                                            <span className="product__details-item-title">Danh Mục</span>
                                            <span className="product__details-item-desc">Tên nhãn hiệu</span>
                                        </div>
                                        <div className="product__details-item flex items-center">
                                            <span className="product__details-item-title">Kiểu kết nối</span>
                                            <span className="product__details-item-desc">Không dây</span>
                                        </div>
                                        <div className="product__details-item flex">
                                            <span className="product__details-item-title">Kho hàng</span>
                                            <span className="product__details-item-desc">498</span>
                                        </div>
                                        <div className="product__details-item flex">
                                            <span className="product__details-item-title">Gửi từ</span>
                                            <span className="product__details-item-desc">Quận Cầu Giấy, Hà Nội</span>
                                        </div>
                                    </div>
                                )}
                            </div>
                        </a>
                    </div>
                </div>
            </div>
        </section>
    );
}

export default ProductBody;
