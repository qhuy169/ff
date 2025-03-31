import './ProductFilter.scss';
import ProductCard from '../ProductCard';
import Paging from '../Paging';

import { SearchHeartFill } from 'react-bootstrap-icons';
import { ESortOptions } from '../../utils';

function ProductFilter({ params, setParams, productList, page = {totalElements: 0, number: 0, totalPages: 0} }) {
    return (
        <>
            <ul className="shop__sorting bg-white px-12">
                <span>Sắp xếp theo:</span>
                <li className={ESortOptions.POPULAR.index === params.sortOption ? 'active' : ''}>
                    <a
                        onClick={(e) =>
                            setParams((prev) => ({
                                ...prev,
                                sortOption: ESortOptions.POPULAR.index,
                                numberPage: 1,
                            }))
                        }
                    >
                        Phổ biến
                    </a>
                </li>
                <li className={ESortOptions.LATEST.index === params.sortOption ? 'active' : ''}>
                    <a
                        onClick={(e) =>
                            setParams((prev) => ({
                                ...prev,
                                sortOption: ESortOptions.LATEST.index,
                                numberPage: 1,
                            }))
                        }
                    >
                        Mới nhất
                    </a>
                </li>
                <li className={ESortOptions.TOP_SALES.index === params.sortOption ? 'active' : ''}>
                    <a
                        onClick={(e) =>
                            setParams((prev) => ({
                                ...prev,
                                sortOption: ESortOptions.TOP_SALES.index,
                                numberPage: 1,
                            }))
                        }
                    >
                        Bán chạy
                    </a>
                </li>
                <li>
                    <select
                        name="sortPrice"
                        id="sortPrice"
                        className="w-[200px]"
                        placeholder="Giá"
                        onChange={(e) => setParams((prev) => ({ ...prev, sortPrice: e.target.value }))}
                        defaultValue={'none'}
                    >
                        <option disabled value="none">
                            Giá
                        </option>
                        <option value="asc">Giá thấp đến cao</option>
                        <option value="dec">Giá cao đến thấp</option>
                    </select>
                </li>
            </ul>
            <div>
                <input
                    type="text"
                    className="mb-[15px] w-[220px] pl-6 form-control border-none rounded-2xl p-4"
                    placeholder="Search products..."
                    id="search"
                ></input>
                <SearchHeartFill
                    className=" ml-4 font-light text-[16px] cursor-pointer text-gray-300"
                    onClick={() =>
                        setParams((prev) => ({ ...prev, keySearch: document.getElementById('search').value }))
                    }
                ></SearchHeartFill>
                <span className="ml-12">Tất cả sản phẩm: {page?.totalElements || 0} kết quả</span>
            </div>

            <div className="row flex flex-wrap gap-12">
                {productList.map((item, index) => (
                    <ProductCard key={index} {...item}></ProductCard>
                ))}
            </div>
            <div className="row">
                <Paging
                    currentPage={page.number}
                    totalPages={page.totalPages}
                    onClick={(e) => setParams((prev) => ({ ...prev, numberPage: Number.parseInt(e.target.innerText) }))}
                />
                <div className="col-span-12">
                    <ul className="pagination pull-right flex gap-4 absolute bottom-0 right-12">
                        <li className="disabled">
                            <a href="#">«</a>
                        </li>
                    </ul>
                </div>
            </div>
        </>
    );
}

export default ProductFilter;
