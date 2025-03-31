import { useParams } from 'react-router-dom';
import { useState, useEffect } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import BigBannerCategory from './BigBannerCategory';
import ListProductCategory from './ListProductCategory';
import QuickLinkCategory from './QuickLinkCategory';
import FilterCategory from './FilterCategory';
import { getOneCategoryBySlugApi } from '../../../redux/category/categoriesApi';
const Category = () => {
    const { categorySlug } = useParams();
    const dispatch = useDispatch();
    useEffect(() => {
        getOneCategoryBySlugApi(dispatch, categorySlug);
    }, []);
    const [chose, setChose] = useState(undefined);
    const [isOpen, setisOpen] = useState(false);
    const handle = (bolen) => {
        setisOpen(bolen);
    };
    const handleSetChose = (brandId) => {
        setChose(brandId);
    };
    return (
        <div style={{ backgroundColor: '#fff' }}>
            <BigBannerCategory></BigBannerCategory>
            <FilterCategory handle={handle}></FilterCategory>
            {/* <QuickLinkCategory handleSetChose={handleSetChose}></QuickLinkCategory> */}
            <ListProductCategory chose={chose} isOpen={isOpen}></ListProductCategory>
        </div>
    );
};
export default Category;
