import { Link } from 'react-router-dom';
import { ChevronRight, StarFill, Plus, HandThumbsUpFill } from 'react-bootstrap-icons';
import { useSelector } from 'react-redux';
function Head() {
    const initProductDetail = useSelector((state) => state.products?.productDetail?.data) || {};
    const { title='', slug='', category='', categorySlug='', star=2.5454545454545454, totalVote=11 } = initProductDetail;

    const data = {
        like: '910',
        breadcrumb: [
            { name: category, path: `/${categorySlug}` },
            { name: title, path: `/${categorySlug}/${slug}` },
        ],
    };

    // const sum = function (items = [], prop) {
    //     return items.reduce(function (a, b) {
    //         const star = b[prop] ? b[prop] : 0;
    //         return a + star;
    //     }, 0);
    // };

    // let avgStar = sum(rating, 'star') / rating?.length;
    // avgStar = Number.isNaN(avgStar) ? 0 : avgStar;

    const numberStar = Math.floor(star);//Math.floor(avgStar) || 0;
    const Star = () => {
        return [ ...Array(numberStar || [])].map((e, i) => (
            <i key={i}>
                <StarFill />
            </i>
        ));
    };
    return (
        <div className="p-4">
            <ul className="breadcrumb flex text-blue-600 text-2xl list-none">
                {data.breadcrumb.map((item, index) => {
                    return (
                        <li key={index}>
                            <Link to={item.path || '/'}>{item.name}</Link>
                            <i>
                                <ChevronRight />
                            </i>
                        </li>
                    );
                })}
            </ul>
            <div className="flex items-center">
                <h1 className="text-4xl font-bold">{title}</h1>
                &emsp;
                <span className="text-yellow-300">
                    <Star />
                </span>
                &nbsp;
                <span className="text-blue-400">{totalVote} đánh giá</span>&emsp;
                <span className="text-blue-400">
                    <i>
                        <Plus />
                    </i>
                    So sánh
                </span>
                <div className="ml-auto text-xl">
                    <button className="bg-blue-500 text-white p-4 rounded">
                        <i>
                            <HandThumbsUpFill />
                        </i>
                        &nbsp; Thích&nbsp;<span>{data.like}</span>
                    </button>
                    &emsp;
                    <button className="bg-blue-500 text-white p-4 rounded">Chia sẻ</button>
                </div>
            </div>
        </div>
    );
}

export default Head;
