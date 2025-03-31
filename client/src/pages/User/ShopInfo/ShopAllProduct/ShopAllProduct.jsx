import './ShopAllProduct.scss';
import { useDispatch, useSelector } from 'react-redux';
import { useEffect, useState } from 'react';
import { ESortOptions } from '../../../../utils';
import ProductFilter from '../../../../components/ProductFilter';
import { getPageProductShop } from '../../../../redux/product/productsSlice';
import { getAllProductApi } from '../../../../redux/product/productsApi';

function ShopAllProduct({}) {
    const dispatch = useDispatch();
    const shop = useSelector((state) => state.shops.oneShop.data);
    const categoriesByShop = useSelector((state) => state.categories.allCategoryByShop.data);
    const brandsByShop = useSelector((state) => state.brands.allBrandByShop.data);
    useEffect(() => {
        if (categoriesByShop) {
            setParams((prev) => ({ ...prev, categoryIds: categoriesByShop?.map((item) => item.id) }));
        }
    }, [categoriesByShop]);
    useEffect(() => {
        if (brandsByShop) {
            setParams((prev) => ({ ...prev, brandIds: brandsByShop?.map((item) => item.id) }));
        }
    }, [brandsByShop]);
    const [params, setParams] = useState({
        brandIds: brandsByShop?.map((item) => item.id),
        categoryIds: categoriesByShop?.map((item) => item.id),
        keySearch: null,
        numberPage: 1,
        sortOption: ESortOptions.POPULAR.index,
        sortPrice: null,
    });
    useEffect(() => {
        if (shop?.id) {
            if (params.categoryIds.length > 0 && params.brandIds.length > 0) {
                let parameter = {
                    brandIds: params.brandIds,
                    categoryIds: params.categoryIds,
                    keyword: params.keySearch,
                    shopId: shop?.id,
                    page: params.numberPage,
                    limit: 6,
                    sortOption: params.sortOption,
                };
                if (params.sortPrice) {
                    parameter = { ...parameter, sortField: 'price', sortDir: params.sortPrice };
                }
                getAllProductApi(dispatch, parameter);
            } else {
                dispatch(getPageProductShop({ content: [] }));
            }
        }
    }, [shop, params]);
    const { content: productList = [], ...page } = useSelector((state) => state.products.pageProductShop.data);
    const handleCheckCategory = (e, categoryId) => {
        if (e.target.checked) {
            setParams((prev) => ({ ...prev, categoryIds: [...prev.categoryIds, categoryId] }));
        } else {
            setParams((prev) => {
                let newCategoryIds = prev.categoryIds.filter((itemId) => itemId != categoryId);
                return { ...prev, categoryIds: newCategoryIds };
            });
        }
    };
    const handleCheckBrand = (e, brandId) => {
        if (e.target.checked) {
            setParams((prev) => ({ ...prev, brandIds: [...prev.brandIds, brandId] }));
        } else {
            setParams((prev) => {
                let newbrandIds = prev.brandIds.filter((itemId) => itemId != brandId);
                return { ...prev, brandIds: newbrandIds };
            });
        }
    };
    return (
        <div className="row grid grid-cols-12 pt-4">
        <div className="col-span-3">
            <form>
                <div className="well">
                    <div className="row">
                        <div className="col-span-12">
                            <div className="input-group">
                                <span className="input-group-btn">
                                    <button className="btn btn-primary" type="submit">
                                        <i className="fa fa-search"></i>
                                    </button>
                                </span>
                            </div>
                        </div>
                    </div>
                </div>
            </form>

            <form className="shop__filter mt-4">
                <h3 className="headline">
                    <span>Danh mục</span>
                </h3>
                {categoriesByShop &&
                    categoriesByShop.map((item, index) => (
                        <div className="checkbox" key={index}>
                            <input
                                type="checkbox"
                                value={item.id}
                                id={`shop-filter-checkbox-category_${item.id}`}
                                onChange={(e) => handleCheckCategory(e, item.id)}
                                defaultChecked
                            />
                            <label htmlFor={`shop-filter-checkbox-category_${item.id}`}>{item.title}</label>
                        </div>
                    ))}
                <h3 className="headline">
                    <span>Thương hiệu</span>
                </h3>
                {brandsByShop &&
                    brandsByShop.map((item, index) => (
                        <div className="checkbox" key={index}>
                            <input
                                type="checkbox"
                                value={item.id}
                                id={`shop-filter-checkbox-brand_${item.id}`}
                                onChange={(e) => handleCheckBrand(e, item.id)}
                                defaultChecked
                            />
                            <label htmlFor={`shop-filter-checkbox-brand_${item.id}`}>{item.name}</label>
                        </div>
                    ))}

                {/* <h3 className="headline">
                    <span>Material</span>
                </h3>
                <div className="radio">
                    <input
                        type="radio"
                        name="shop-filter__radio"
                        id="shop-filter-radio_1"
                        defaultValue=""
                        defaultChecked={true}
                    ></input>
                    <label htmlFor="shop-filter-radio_1">100% Cotton</label>
                </div>
                <div className="radio">
                    <input
                        type="radio"
                        name="shop-filter__radio"
                        id="shop-filter-radio_2"
                        defaultValue=""
                    ></input>
                input    <label htmlFor="shop-filter-radio_2">Bamboo</label>
                </div>
                <div className="radio">
                    <input
                        type="radio"
                        name="shop-filter__radio"
                        id="shop-filter-radio_3"
                        defaultValue=""
                    ></input>
                    <label htmlFor="shop-filter-radio_3">Leather</label>
                </div>
                <div className="radio">
                    <input
                        type="radio"
                        name="shop-filter__radio"
                        id="shop-filter-radio_4"
                        defaultValue=""
                    ></input>
                    <label htmlFor="shop-filter-radio_4">Polyester</label>
                </div>
                <div className="radio">
                    <input
                        type="radio"
                        name="shop-filter__radio"
                        id="shop-filter-radio_5"
                        defaultValue=""
                    ></input>
                    <label htmlFor="shop-filter-radio_5">Not specified</label>
                </div>

                <h3 className="headline">
                    <span>Colors</span>
                </h3>
                <div className="shop-filter__color">
                    <input type="text" id="shop-filter-color_1" data-input-color="black"></input>
                    <label htmlFor="shop-filter-color_1"></label>
                </div> */}
            </form>
        </div>

        <div className="col-span-9">
            <ProductFilter params={params} setParams={setParams} productList={productList} page={page}/>
        </div>
    </div>
    )
}

export default ShopAllProduct;