import { getAllCategories, getAllCategoriesByShop, getOneCategory } from './categoriesSlice';
import { getAllProductApi} from '../product/productsApi';
import { categoryService } from '~/services';
import { brandService } from '~/services';

export const getAllCategoriesApi = async (dispatch) => {
    let res = await categoryService.getAllCategories();
    dispatch(getAllCategories(res.data));
};
export const getAllCategoriesByShopIdApi = async (dispatch, shopId) => {
    let res = await categoryService.getAllCategoriesByShopId(shopId);
    console.log('res', res);
    dispatch(getAllCategoriesByShop(res.data));
};
export const getOneCategoryBySlugApi = async (dispatch, slug) => {
    let res =  await categoryService.getCategoryBySlug(slug);
    let resBrand = await brandService.getAllBrandsByCategoryId(res.data?.id);
    dispatch(getOneCategory({...res.data, brands: resBrand.data}));
};