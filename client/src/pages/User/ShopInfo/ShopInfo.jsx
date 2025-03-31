import './shopinfo.scss';

import { useDispatch, useSelector } from 'react-redux';
import { useParams } from 'react-router-dom';
import { useEffect, useState } from 'react';
import { getOverviewByShopIdApi, getShopBySlugApi } from '../../../redux/shop/shopApi';
import { doc } from 'prettier';
import { getAllCategoriesByShopIdApi } from '../../../redux/category/categoriesApi';
import { getAllBrandByShopIdApi } from '../../../redux/brand/brandApi';

import ShopAllProduct from './ShopAllProduct';
import ShopInfoVouncher from './ShopInfoVouncher';

function ShopInfo() {
    const { slug } = useParams();
    const dispatch = useDispatch();
    useEffect(() => {
        getShopBySlugApi(dispatch, slug);
    }, []);
    const shop = useSelector((state) => state.shops.oneShop.data);
    useEffect(() => {
        if (shop?.id) {
            getAllCategoriesByShopIdApi(dispatch, shop?.id);
            getAllBrandByShopIdApi(dispatch, shop?.id);
            getOverviewByShopIdApi(dispatch, shop?.id);
        }
    }, [shop]);
    const categoriesByShop = useSelector((state) => state.categories.allCategoryByShop.data);
    const brandsByShop = useSelector((state) => state.brands.allBrandByShop.data);
    const overviewShop = useSelector((state) => state.shops.overviewShop.data);
    const [panel, setPanel] = useState(0);
    return (
        <div
            className="container mt-8"
            style={{
                width: '86%',
                maxWidth: '1240px',
                margin: '50px auto',
            }}
        >
            <div className="row grid grid-cols-12 p-4 mb-12">
                <div className="col-span-4 relative">
                    <div className="bg-image" style={{ backgroundImage: `url(${shop?.background})` }}></div>
                    <div className="flex items-center my-4 bg-text">
                        <div className="flex gap-8">
                            <div className="list-group-item">
                            </div>
                            <img
                                    className="h-full w-full rounded-full"
                                    style={{width: '120px', height: '120px'}}
                                    src={
                                        shop?.avatar ||
                                        'data:image/png;base64,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'
                                    }
                                ></img>
                            <div className="flex flex-col gap-3 flex-1">
                                <div href="#" className="mb-2 w-full list-group-item font-semibold">
                                    Thông tin Shop
                                </div>
                                <span className="list-group-item">{shop.name}</span>
                                <span className="list-group-item">{shop.phone}</span>
                                <span className="list-group-item">{shop.email}</span>
                            </div>
                        </div>
                    </div>
                </div>
                <div className="col-span-8">
                    <div className="seller-info-list h-[170px] p-[50px]:important">
                        <div className="seller-info-item seller-info-item---clickable">
                            <div className="seller-info-item-icon-wrapper">
                                <svg
                                    enableBackground="new 0 0 15 15"
                                    viewBox="0 0 15 15"
                                    x="0"
                                    y="0"
                                    strokeWidth="0"
                                    className="shopee-svg-icon"
                                >
                                    <path d="m13 1.9c-.2-.5-.8-1-1.4-1h-8.4c-.6.1-1.2.5-1.4 1l-1.4 4.3c0 .8.3 1.6.9 2.1v4.8c0 .6.5 1 1.1 1h10.2c.6 0 1.1-.5 1.1-1v-4.6c.6-.4.9-1.2.9-2.3zm-11.4 3.4 1-3c .1-.2.4-.4.6-.4h8.3c.3 0 .5.2.6.4l1 3zm .6 3.5h.4c.7 0 1.4-.3 1.8-.8.4.5.9.8 1.5.8.7 0 1.3-.5 1.5-.8.2.3.8.8 1.5.8.6 0 1.1-.3 1.5-.8.4.5 1.1.8 1.7.8h.4v3.9c0 .1 0 .2-.1.3s-.2.1-.3.1h-9.5c-.1 0-.2 0-.3-.1s-.1-.2-.1-.3zm8.8-1.7h-1v .1s0 .3-.2.6c-.2.1-.5.2-.9.2-.3 0-.6-.1-.8-.3-.2-.3-.2-.6-.2-.6v-.1h-1v .1s0 .3-.2.5c-.2.3-.5.4-.8.4-1 0-1-.8-1-.8h-1c0 .8-.7.8-1.3.8s-1.1-1-1.2-1.7h12.1c0 .2-.1.9-.5 1.4-.2.2-.5.3-.8.3-1.2 0-1.2-.8-1.2-.9z"></path>
                                </svg>
                            </div>
                            <div className="seller-info-item-text">
                                <div className="seller-info-item-text-name">Sản phẩm:&nbsp;</div>
                                <div className="seller-info-item-text-value">{overviewShop?.totalProduct || 0}</div>
                            </div>
                        </div>
                        {/* <div className="seller-info-item">
                            <div className="seller-info-item-icon-wrapper">
                                <svg
                                    enableBackground="new 0 0 15 15"
                                    viewBox="0 0 15 15"
                                    x="0"
                                    y="0"
                                    className="shopee-svg-icon"
                                >
                                    <g>
                                        <circle cx="7" cy="4.5" fill="none" r="3.8" strokeMiterlimit="10"></circle>
                                        <line
                                            fill="none"
                                            strokeLinecap="round"
                                            strokeMiterlimit="10"
                                            x1="12"
                                            x2="12"
                                            y1="11.2"
                                            y2="14.2"
                                        ></line>
                                        <line
                                            fill="none"
                                            strokeLinecap="round"
                                            strokeMiterlimit="10"
                                            x1="10.5"
                                            x2="13.5"
                                            y1="12.8"
                                            y2="12.8"
                                        ></line>
                                        <path
                                            d="m1.5 13.8c0-3 2.5-5.5 5.5-5.5 1.5 0 2.9.6 3.9 1.6"
                                            fill="none"
                                            strokeLinecap="round"
                                            strokeMiterlimit="10"
                                        ></path>
                                    </g>
                                </svg>
                            </div>
                            <div className="seller-info-item-text">
                                <div className="seller-info-item-text-name">Đang Theo dõi:&nbsp;</div>
                                <div className="seller-info-item-text-value">17</div>
                            </div>
                        </div> */}
                        {/* <div className="seller-info-item">
                            <div className="seller-info-item-icon-wrapper">
                                <svg
                                    enableBackground="new 0 0 15 15"
                                    viewBox="0 0 15 15"
                                    x="0"
                                    y="0"
                                    className="shopee-svg-icon"
                                >
                                    <g>
                                        <polygon
                                            fill="none"
                                            points="14 10.8 7 10.8 3 13.8 3 10.8 1 10.8 1 1.2 14 1.2"
                                            strokeLinejoin="round"
                                            strokeMiterlimit="10"
                                        ></polygon>
                                        <circle cx="4" cy="5.8" r="1" stroke="none"></circle>
                                        <circle cx="7.5" cy="5.8" r="1" stroke="none"></circle>
                                        <circle cx="11" cy="5.8" r="1" stroke="none"></circle>
                                    </g>
                                </svg>
                            </div>
                            <div className="seller-info-item-text">
                                <div className="seller-info-item-text-name">Tỉ lệ phản hồi Chat:&nbsp;</div>
                                <div className="seller-info-item-text-value">
                                    96% (trong vài giờ)
                                    <div className="section-seller-overview__inline-icon section-seller-overview__inline-icon--help">
                                        <svg width="10" height="10">
                                            <g
                                                fill="currentColor"
                                                fillRule="nonzero"
                                                color="currentColor"
                                                strokeWidth="0"
                                            >
                                                <path d="M5 10A5 5 0 1 1 5 0a5 5 0 0 1 0 10zM5 .675a4.325 4.325 0 1 0 0 8.65 4.325 4.325 0 0 0 0-8.65z"></path>
                                                <path d="M6.235 5.073c.334-.335.519-.79.514-1.264a1.715 1.715 0 0 0-.14-.684 1.814 1.814 0 0 0-.933-.951A1.623 1.623 0 0 0 5 2.03a1.66 1.66 0 0 0-.676.14 1.772 1.772 0 0 0-.934.948c-.093.219-.14.454-.138.691a.381.381 0 0 0 .106.276c.07.073.168.113.27.11a.37.37 0 0 0 .348-.235c.02-.047.031-.099.03-.15a1.006 1.006 0 0 1 .607-.933.954.954 0 0 1 .772.002 1.032 1.032 0 0 1 .61.93c.003.267-.1.525-.288.716l-.567.537c-.343.35-.514.746-.514 1.187a.37.37 0 0 0 .379.382c.1.002.195-.037.265-.108a.375.375 0 0 0 .106-.274c0-.232.097-.446.29-.642l.568-.534zM5 6.927a.491.491 0 0 0-.363.152.53.53 0 0 0 0 .74.508.508 0 0 0 .726 0 .53.53 0 0 0 0-.74A.491.491 0 0 0 5 6.927z"></path>
                                            </g>
                                        </svg>
                                    </div>
                                </div>
                            </div>
                        </div> */}
                        {/* <div className="seller-info-item">
                            <div className="seller-info-item-icon-wrapper">
                                <svg
                                    enableBackground="new 0 0 15 15"
                                    viewBox="0 0 15 15"
                                    x="0"
                                    y="0"
                                    className="shopee-svg-icon"
                                >
                                    <g>
                                        <circle cx="5.5" cy="5" fill="none" r="4" strokeMiterlimit="10"></circle>
                                        <path
                                            d="m8.4 7.5c.7 0 1.1.7 1.1 1.6v4.9h-8v-4.9c0-.9.4-1.6 1.1-1.6"
                                            fill="none"
                                            strokeLinejoin="round"
                                            strokeMiterlimit="10"
                                        ></path>
                                        <path
                                            d="m12.6 6.9c.7 0 .9.6.9 1.2v5.7h-2"
                                            fill="none"
                                            strokeLinecap="round"
                                            strokeLinejoin="round"
                                            strokeMiterlimit="10"
                                        ></path>
                                        <path
                                            d="m9.5 1.2c1.9 0 3.5 1.6 3.5 3.5 0 1.4-.9 2.7-2.1 3.2"
                                            fill="none"
                                            strokeLinecap="round"
                                            strokeMiterlimit="10"
                                        ></path>
                                    </g>
                                </svg>
                            </div>
                            <div className="seller-info-item-text">
                                <div className="seller-info-item-text-name">Người theo dõi:&nbsp;</div>
                                <div className="seller-info-item-text-value">59,9k</div>
                            </div>
                        </div> */}
                        <div className="seller-info-item seller-info-item---clickable">
                            <div className="seller-info-item-icon-wrapper">
                                <svg
                                    enableBackground="new 0 0 15 15"
                                    viewBox="0 0 15 15"
                                    x="0"
                                    y="0"
                                    className="shopee-svg-icon icon-rating"
                                >
                                    <polygon
                                        fill="none"
                                        points="7.5 .8 9.7 5.4 14.5 5.9 10.7 9.1 11.8 14.2 7.5 11.6 3.2 14.2 4.3 9.1 .5 5.9 5.3 5.4"
                                        strokeLinecap="round"
                                        strokeLinejoin="round"
                                        strokeMiterlimit="10"
                                    ></polygon>
                                </svg>
                            </div>
                            <div className="seller-info-item-text">
                                <div className="seller-info-item-text-name">đánh giá:&nbsp;</div>
                                <div className="seller-info-item-text-value">{overviewShop?.totalVote === 0 ? 'chưa có đánh giá' : `${overviewShop?.avgStar || 0} (${overviewShop?.totalVote || 0} đánh giá)`}</div>
                            </div>
                        </div>
                        <div className="seller-info-item seller-info-item---clickable">
                            <div className="seller-info-item-icon-wrapper">
                            <svg width="13" height="14"><g fill="currentColor" fill-rule="nonzero" stroke-width="0.4"><path d="M9.49.903h.453c.498 0 .903.404.903.903v4.993a.452.452 0 1 0 .904 0V1.806C11.75.808 10.94 0 9.944 0H9.49a.452.452 0 1 0 0 .903zM5.879 12.645H1.813a.903.903 0 0 1-.903-.902V1.806c0-.499.405-.903.903-.903h.452a.451.451 0 0 0 0-.903h-.452C.816 0 .007.808.007 1.806v9.936c0 .998.809 1.806 1.806 1.806h4.065a.452.452 0 0 0 0-.903z"></path><path d="M2.265 0H9.49a.451.451 0 1 1 0 .903H2.265a.452.452 0 0 1 0-.903zm.904 3.613H9.04a.452.452 0 1 1 0 .903H3.169a.452.452 0 1 1 0-.903zm0 2.71h3.613a.452.452 0 1 1 0 .904H3.169a.452.452 0 0 1 0-.904zm0 2.709h1.806a.452.452 0 1 1 0 .905H3.169a.452.452 0 0 1 0-.905zm6.322 4.065a2.258 2.258 0 1 0 0-4.515 2.258 2.258 0 0 0 0 4.515zm0 .903a3.161 3.161 0 1 1 0-6.323 3.161 3.161 0 0 1 0 6.323z"></path><path d="M7.575 12.117l3.193-3.194a.451.451 0 1 1 .638.639l-3.192 3.192a.451.451 0 0 1-.639-.637z"></path></g></svg>
                            </div>
                            <div className="seller-info-item-text">
                                <div className="seller-info-item-text-name">Tỉ lệ hủy đơn:&nbsp;</div>
                                <div className="seller-info-item-text-value">0 %</div>
                            </div>
                        </div>
                        <div className="seller-info-item">
                            <div className="seller-info-item-icon-wrapper">
                                <svg
                                    enableBackground="new 0 0 15 15"
                                    viewBox="0 0 15 15"
                                    x="0"
                                    y="0"
                                    className="shopee-svg-icon"
                                >
                                    <g>
                                        <circle cx="6.8" cy="4.2" fill="none" r="3.8" strokeMiterlimit="10"></circle>
                                        <polyline
                                            fill="none"
                                            points="9.2 12.5 11.2 14.5 14.2 11"
                                            strokeLinecap="round"
                                            strokeLinejoin="round"
                                            strokeMiterlimit="10"
                                        ></polyline>
                                        <path
                                            d="m .8 14c0-3.3 2.7-6 6-6 2.1 0 3.9 1 5 2.6"
                                            fill="none"
                                            strokeLinecap="round"
                                            strokeMiterlimit="10"
                                        ></path>
                                    </g>
                                </svg>
                            </div>
                            <div className="seller-info-item-text">
                                <div className="seller-info-item-text-name">tham gia:&nbsp;</div>
                                <div className="seller-info-item-text-value">{overviewShop?.timeDistanceFromCreateAt || 'Vừa mới đây'}</div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div className="row">
                <ul className="h-32 bg-white flex justify-center shop__sorting">
                    <li className={panel == 0 ? 'active' : ''} onClick={(e) => setPanel(0)}>
                        <a href="#">Tất cả sản phẩm</a>
                    </li>
                    <li className={panel == 1 ? 'active' : ''} onClick={(e) => setPanel(1)}>
                        <a href="#">Mã voucher</a>
                    </li>
                </ul>
            </div>
            {panel === 0 ? (
                <ShopAllProduct shop={shop} categoriesByShop={categoriesByShop} brandsByShop={brandsByShop} />
            ) : panel === 1 ? (
                <ShopInfoVouncher />
            ) : (
                <></>
            )}
        </div>
    );
}

export default ShopInfo;
