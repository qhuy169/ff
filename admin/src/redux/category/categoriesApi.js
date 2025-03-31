import { getAllCategories, getOneCategory } from './categoriesSlice';
import { getAllProductApi} from '../product/productsApi';
import { CategoryService, BrandService } from '../../services';

export const getAllCategoriesApi = async (dispatch) => {
    let res = await CategoryService.getAllCategories();
    dispatch(getAllCategories(res.data));
};
export const getOneCategoryBySlugApi = async (dispatch, slug) => {
    let res =  await CategoryService.getCategoryBySlug(slug);
    let resBrand = await BrandService.getAllBrandsByCategoryId(res.data?.id);
    dispatch(getOneCategory({...res.data, brands: resBrand.data}));
};