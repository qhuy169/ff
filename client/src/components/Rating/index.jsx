import clsx from 'clsx';
import { Modal } from 'flowbite-react';
import { useState } from 'react';
import { ChatFill, HeartFill, StarFill, ThreeDots } from 'react-bootstrap-icons';
import { useDispatch, useSelector } from 'react-redux';
import swal from 'sweetalert';
import { v4 as uuidv4 } from 'uuid';
import { updateDiscussRating } from '~/redux/product/productsSlice';
import { ratingService } from '~/services';
import { sentimentService } from '../../services';
import { getSentimentByResponse } from '../../utils';
import PopupInfo from './PopupInfo';

const Rating = ({ onClick }) => {
    const [numberStar, setNumberStar] = useState(5);

    const active = 'text-yellow-300';

    return (
        <div className="cursor-pointer flex gap-6">
            {[...Array(5)].map((e, i) => (
                <i
                    key={i}
                    onClick={() => {
                        setNumberStar(i + 1);
                        onClick(i + 1);
                    }}
                >
                    <StarFill className={clsx('text-5xl', i + 1 <= numberStar && active)} />
                </i>
            ))}
        </div>
    );
};
const Star = ({ star }) => {
    return [...Array(star || '')].map((e, i) => (
        <i key={i}>
            <StarFill />
        </i>
    ));
};

function ProductRating() {
    const initProductDetail = useSelector((state) => state.products.productDetail.data);

    const { title = '', star = 2.5454545454545454, totalVote = 11, img, rating, id, vote } = initProductDetail;

    const dispatch = useDispatch();
    const [productRating, setProductRating] = useState(() => {
        return rating?.content ? rating?.content.slice(rating?.content.length > 4 ? 4 : rating?.content.length) : [];
    });

    // console.log('rating: ', rating);

    const [showModal, setShowModal] = useState(false);
    const [showPopupInfo, setShowPopupInfo] = useState(false);
    const [isReview, setReview] = useState(false);
    // const [star, setStar] = useState(0);
    const [discuss, setDiscuss] = useState({ id, status: false, data: [] });
    const [ratingId, setRatingId] = useState({ index: -1, id });

    let infoRating = {
        // id: uuidv4(),
        productId: id,
        // user: {},
        content: '',
        replyForFeedbackId: null,
        // discuss: [],
        star: 5,
        images: null,
        // createdAt: moment().format('HH:MM MM/DD, YYYY'),
    };
    const images = [
        'https://images.fpt.shop/unsafe/filters:quality(5)/fptshop.com.vn/uploads/images/tin-tuc/149387/Originals/dien-thoai-tam-trung-chup-anh-dep-2022-hinh1.png',
        'https://vcdn1-sohoa.vnecdn.net/2021/08/02/iPhone-12-Pro-Max-1627882467.jpg?w=1200&h=0&q=100&dpr=1&fit=crop&s=JvvZkSOc839aGDhKueX-Tw',
        'https://cdn-www.vinid.net/2020/09/e9e5897c-i%E1%BB%87n-tho%E1%BA%A1i-ch%E1%BB%A5p-%E1%BA%A3nh-%C4%91%E1%BA%B9p.jpg',
        'https://didongviet.vn/dchannel/wp-content/uploads/2022/05/2-dien-thoai-chup-anh-dep-didongviet.jpg',
    ];

    const handleDiscuss = (comment) => {
        setDiscuss((state) => {
            const discussData = comment.childFeedbacks ? comment.childFeedbacks : [];
            const stateHide = {
                ...state,
                id: comment.id,
                data: [],
                status: false,
            };
            const stateShow = {
                ...state,
                id: comment.id,
                data: discussData,
                status: true,
            };
            return state.status ? stateHide : stateShow;
        });
    };
    const handleSubmitInfo = async (info) => {
        const content = document.getElementById(`inputDiscuss${ratingId.index}`).value;
        const newDiscuss = {
            id: uuidv4(),
            user: {
                id: uuidv4(),
                ...info,
            },
            content,
        };
        const oldDiscuss = rating?.content[ratingId.index]?.childFeedbacks || [];
        const discussData = [...oldDiscuss, newDiscuss];
        const data = JSON.stringify({ discuss: discussData });
        const res = await ratingService.patchRating(ratingId.id, data);
        if (true) {
            productRating;
            dispatch(updateDiscussRating({ idRating: ratingId.id, ...newDiscuss }));
            document.getElementById(`inputDiscuss${ratingId.index}`).value = '';
            setDiscuss((state) => ({
                ...state,
                data: [newDiscuss, ...state.data],
            }));
            swal({ text: 'Thành công!', icon: 'success' });
            setShowPopupInfo(false);
        } else {
            swal({ text: 'Thất bại', icon: 'error' });
        }
    };
    const handleSubmit = async () => {
        const formData = new FormData();
        formData.append(
            'data',
            new Blob(
                [
                    JSON.stringify({
                        productId: infoRating.productId,
                        content: infoRating.content,
                        replyForFeedbackId: infoRating.replyForFeedbackId,
                        star: isReview ? infoRating.star : -1,
                    }),
                ],
                { type: 'application/json' },
            ),
        );

        if (infoRating.images?.length > 0) {
            infoRating.images.forEach((image) => {
                formData.append('images', image);
            });
        }

        try {
            setShowModal(false);
            const res = await ratingService.postRating(formData);
            console.log(res);

            if (res.status === 'CREATED') {
                if (!infoRating.replyForFeedbackId) {
                    // Đảm bảo mảng không bị null/undefined
                    setProductRating((old = []) => [res.data, ...old]);
                } else {
                    setDiscuss((state) => ({
                        ...state,
                        data: state?.data ? [res.data, ...state.data] : [res.data], // Nếu state.data chưa tồn tại, tạo mới
                    }));
                }
                swal({ text: 'Tạo nhận xét thành công!', icon: 'success' });
            } else {
                swal({ text: res.message, icon: 'error' });
            }
        } catch (err) {
            if (err?.status === 'OK' && err?.message.startsWith('Feedback is existed')) {
                swal({ text: 'Bạn đã nhận xét đánh giá này. Không thể đánh giá thêm!', icon: 'error' });
            } else {
                swal({ text: err.message || 'Có lỗi xảy ra', icon: 'error' });
            }
        }

        infoRating.replyForFeedbackId = null;
    };

    const handleComment = (e) => {
        setReview(!isReview);
    };
    const handleShowModel = (e) => {
        if (initProductDetail.enabled) {
            setShowModal(true);
        } else {
            swal({ title: "Sản phẩm đã bị cấm. Không thể thực hiện hành động này!", icon: "warning" });
        }
    }
    return (
        <div className=" p-4 w-full bg-while ">
            <p className="text-3xl font-bold">Đánh giá {title}</p>
            <div className="flex items-center border-b py-4">
                <div className="rating w-96">
                    <div className="my-6">
                        {star && <span>{star.toFixed(1)}</span>}
                        <span className="text-yellow-300">
                            <Star star={Math.floor(star)} />
                        </span>
                        &nbsp;
                        <span>{totalVote} đánh giá</span>
                    </div>
                    {vote?.map((item) => {
                        const style = { width: `${item.percent}%` };
                        if (item.star > 0) {
                            return (
                                <div className="flex items-center text-2xl" key={item.star}>
                                    <span className="flex">
                                        {item.star}&nbsp;
                                        <i>
                                            <StarFill />
                                        </i>
                                    </span>
                                    &nbsp;
                                    <div className="container bg-gray-200 h-1.5">
                                        <div className="bg-yellow-400 h-full" style={style}></div>
                                    </div>
                                    &nbsp;
                                    <span className="text-blue-500 font-bold">{item.percent}%</span>
                                </div>
                            );
                        }
                    })}
                </div>
                <div className="flex flex-wrap gap-4 ml-8">
                    {images.map((image, index) => {
                        return (
                            <div className="h-24 w-24 rounded-xl overflow-hidden" key={index}>
                                <img src={image} alt="" className="h-full w-full object-cover" />
                            </div>
                        );
                    })}
                </div>
            </div>
            {showPopupInfo && (
                <PopupInfo onSubmit={(e) => handleSubmitInfo(e)} onClose={() => setShowPopupInfo(false)} />
            )}
            {productRating?.length > 0 &&
                productRating?.map((comment, index) => {
                    let status;
                    if (discuss.id === comment.id) {
                        status = discuss.status;
                    }
                    return (
                        <div className="py-8 border-b m-4" key={index}>
                            <p>{comment.author.username}</p>
                            {comment.star > 0 && (
                                <span className="text-yellow-300">
                                    <Star star={comment.star} />
                                </span>
                            )}
                            &nbsp;
                            <i>
                                <HeartFill className="text-red-600" />
                            </i>
                            &nbsp;
                            <span className="text-2xl">Sẽ giới thiệu cho bạn bè, người thân</span>
                            <p className="text-2xl">{comment.content}</p>
                            <div>
                                <span className="text-blue-500 cursor-pointer select-none">Hữu ích</span>&emsp;
                                <span
                                    className="text-blue-500 cursor-pointer select-none"
                                    onClick={() => {
                                        handleDiscuss(comment);
                                    }}
                                >
                                    <i>
                                        <ChatFill />
                                    </i>
                                    &nbsp; {comment.childFeedbacks?.length || 0} thảo luận
                                </span>
                                &emsp;
                                <i>
                                    <ThreeDots />
                                </i>
                                <div className={clsx(!status && 'hidden')}>
                                    <input
                                        type="text"
                                        className="rounded-lg p-4 border mr-8 w-3/4 text-2xl"
                                        placeholder="Nhập thảo luận của bạn"
                                        id={`inputDiscuss${index}`}
                                        onChange={() => setRatingId({ index, id: comment.id })}
                                    />
                                    <button
                                        onClick={() => {
                                            let content = document.getElementById(`inputDiscuss${index}`).value;
                                            if (content === '') {
                                                swal({ text: 'Vui long nhap noi dung', icon: 'warning' });
                                            }
                                            infoRating.content = content;
                                            infoRating.replyForFeedbackId = comment.id;
                                            handleSubmit();
                                        }}
                                        className="bg-blue-500 px-6 py-4 rounded-lg text-white"
                                    >
                                        GỬI
                                    </button>
                                    {discuss.data.map((item, index) => {
                                        return (
                                            <div className="p-4 border-b my-2 text-2xl" key={index}>
                                                <p>{item.author.username}</p>
                                                <p>{item.content}</p>
                                                <span className="text-blue-500">Hữu ích</span>&emsp;
                                            </div>
                                        );
                                    })}
                                </div>
                            </div>
                        </div>
                    );
                })}
            <div className="m-auto flex gap-4 w-full">
                <button className="bg-orange-300 p-4 rounded text-white w-1/2" onClick={handleShowModel}>
                    <i>
                        <StarFill />
                    </i>
                    &nbsp; Bình luận
                </button>
                <Modal show={showModal} onClose={() => setShowModal(false)} size="5xl">
                    <Modal.Header>
                        <p className="text-2xl font-bold">Đánh giá</p>
                    </Modal.Header>
                    <Modal.Body>
                        <div className="p-8 text-center">
                            <div className="font-bold p-4 text-2xl flex items-center justify-center">
                                <div className="w-56 mt-2">
                                    <img src={img} alt="" />
                                </div>
                                <p>{title}</p>
                            </div>
                            <div className="flex">
                                <button
                                    className="p-4 m-4 bg-blue-500 rounded-xl text-white mr-8"
                                    onClick={handleComment}
                                >
                                    Đánh giá
                                </button>
                                {isReview && (
                                    <div className="flex justify-center my-4">
                                        <Rating
                                            onClick={(e) => {
                                                infoRating = { ...infoRating, star: e };
                                            }}
                                        />
                                    </div>
                                )}
                            </div>

                            <form
                                action=""
                                className="flex flex-col items-center gap-4"
                                onSubmit={(e) => {
                                    e.preventDefault();
                                    handleSubmit();
                                }}
                            >
                                <textarea
                                    className="w-full rounded-xl p-4"
                                    id=""
                                    name=""
                                    cols="30"
                                    rows="10"
                                    onChange={(e) => {
                                        infoRating = { ...infoRating, content: e.target.value };
                                    }}
                                    placeholder="Mời bạn chia sẻ thêm một số cảm nhận về sản phẩm ..."
                                ></textarea>
                                <button type="submit" className="p-4 bg-blue-500 rounded-xl text-white">
                                    Gửi đánh giá ngay
                                </button>
                            </form>
                            <small>Để đánh giá được duyệt, quý khách vui lòng tham khảo Quy định duyệt đánh giá</small>
                        </div>
                    </Modal.Body>
                </Modal>
                <button
                    onClick={() => setProductRating(rating?.content)}
                    className="border border-black-500 p-4 rounded text-blue-500 w-1/2"
                >
                    Xem {rating?.content?.length} đánh giá
                </button>
            </div>
        </div>
    );
}

export default ProductRating;