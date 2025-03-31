import QuickLink from '~/components/QuickLink';
import './quicklinkphone-module.scss';
import { useSelector, useDispatch } from 'react-redux';
// const data = [
//     {
//         type: 'Apple',
//         link: '//cdn.tgdd.vn/Brand/1/iPad-(Apple)522-b_4.jpg',
//         demand: 'Hỗ trợ nghe gọi',
//     },
//     {
//         type: 'Samsung',
//         link: '//cdn.tgdd.vn/Brand/1/samsungnew-220x48-3.png',
//         demand: 'Pin Khủng',
//     },
//     {
//         type: 'Xiaomi',
//         link: '//cdn.tgdd.vn/Brand/1/Tablet-xiaomi-220x48-1.png',
//         demand: 'Chơi game',
//     },
//     {
//         type: 'Lenovo',
//         link: '//cdn.tgdd.vn/Brand/1/Lenovo522-b_6.jpg',
//         demand: 'Chơi game',
//     },
//     {
//         type: 'Masstel',
//         link: '//cdn.tgdd.vn/Brand/1/Masstel522-b_7.png',
//         demand: 'Chơi game',
//     },
//     {
//         type: 'NOKIA',
//         link: '//cdn.tgdd.vn/Brand/1/logonokia-220x48-1.jpg',
//         demand: 'Chơi game',
//     },
//     {
//         type: 'HUAWEI',
//         link: '//cdn.tgdd.vn/Brand/1/Huawei522-b_4.jpg',
//         demand: 'Chơi game',
//     },
//     {
//         type: 'Alcatel',
//         link: '//cdn.tgdd.vn/Brand/1/logoalcatel-220x48-1.png',
//         demand: 'Chơi game',
//     },
// ];
// const dataDemand = data
//     .map((item) => item.demand)
//     .reduce(function (accumulator, element) {
//         if (accumulator.indexOf(element) === -1) {
//             accumulator.push(element);
//         }
//         return accumulator;
//     }, []);

const QuickLinkCategory = (props) => {
    const {brands} = useSelector((state) => state.categories.oneCategory.data);
    return (
        <div className="container__phone">
            <div className="container__quicklink-phone">
                <div className="quicklink">
                    <div>
                        {brands && brands.map((item) => (
                            <QuickLink
                                key={item.id}
                                id={item.id}
                                type={item.name}
                                link={item.img}
                                handleSetChose={props.handleSetChose}
                            ></QuickLink>
                        ))}
                    </div>
                </div>
            </div>
            {/* <div className="container__quicklink-demand">
                <div className="container__quicklink-phone">
                    <div className="quicklink">
                        <h4 className="">Chọn điện thoại theo nhu cầu:</h4>
                        <div>
                            {dataDemand.map((item) => (
                                <QuickLink demand={item}></QuickLink>
                            ))}
                        </div>
                    </div>
                </div>
            </div> */}
        </div>
    );
};

export default QuickLinkCategory;
