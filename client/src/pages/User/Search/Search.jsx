import './search.scss';
import { useState, useEffect } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { useSearchParams } from 'react-router-dom';
import ProductCard from '../../../components/ProductCard';
import Slick from '../../../components/Slick';
import ListProduct from './../../../components/ListProduct/ListProduct';
import { GeoAlt } from 'react-bootstrap-icons';
import { getResult, removeResult } from '~/redux/search/searchApi';

function Search() {
    const dispatch = useDispatch();
    let [searchParams, setSearchParams] = useSearchParams();
    let keySearch = searchParams.get('keyword');

    const optionsFillter = [
        {
            id: 1,
            name: 'Nổi bật',
        },
        {
            id: 2,
            name: '% giảm nhiều',
        },
        {
            id: 3,
            name: 'Giá thấp đến cao',
        },
    ];
    const [fillterLocation, setFillterLocation] = useState(false);
    const [checked, setChecked] = useState(1);
    let {content: resultSearch = [], page} = useSelector((state) => state.search.pageSearch.data) || {content: []};
    let products = [...resultSearch];
    // let products = content;
    useEffect(() => {
        getResult(dispatch, {keyword: keySearch});
    }, [keySearch]);
    const handleClickFillter = (e) => {
        // var value = e.options[e.selectedIndex].value;
        // setChecked(value);
        let idSelect = e.target.options[e.target.options.selectedIndex].value;
        console.log(typeof idSelect);
        setChecked(parseInt(idSelect));
    };
    const handlelocation = () => {
        setFillterLocation(!fillterLocation);
    };
    if (checked === 3) {
        products = products.sort((a, b) => parseInt(a.price) - parseInt(b.price));
    }
    if (checked === 1) {
        products = products?.sort((a, b) => -parseInt(a.totalVote) + parseInt(b.totalVote));
    }
    if (checked === 2) {
        products = products?.sort((a, b) => -a.discount + b.discount);
    }
    return (
        <div>
            {/*        
            {resultSearch.map(item=>(
                // <ProductCard {...item}></ProductCard>
                
                
            ))} */}
            <h2 className="phone__content font-semibold text-blue-400 py-4">Tất cả kết quả tìm kiếm</h2>
            <div className="phone__content">
                <div className="flex flex-col items-center px-5 mb-6 pr-[17px]"></div>
                <div className="listcontent">
                    {products ? (
                        <div className="">
                            <select className="inline-block mb-4" onChange={(e) => handleClickFillter(e)}>
                                {optionsFillter.map((item) => (
                                    // <div>
                                    //     <input type="radio" checked={checked === item.id} onClick={() => handleClickFillter(item.id)} />
                                    //     <label className="mr-4 ml-2">{item.name}</label>
                                    // </div>

                                    <option key={item.id} value={item.id}>
                                        {item.name}{' '}
                                    </option>
                                ))}
                            </select>
                            <div className="flex items-center  cursor-pointer mb-1">
                                <GeoAlt className="text-gray-400"></GeoAlt>
                                <span onClick={handlelocation} className="text-[13px] ml-1 text-blue-400">
                                    {' '}
                                    {fillterLocation
                                        ? 'Không áp dụng vị trí cho sản phẩm'
                                        : 'Áp dụng vị trí cho sản phẩm'}
                                </span>
                            </div>

                            <ListProduct products={products} isSlide={false}></ListProduct>
                        </div>
                    ) : (
                        <h2>Không có sản phẩm này tại hệ thống chúng tôi</h2>
                    )}
                </div>
            </div>
        </div>
    );
}

export default Search;
