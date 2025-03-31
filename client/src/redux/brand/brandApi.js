import { getAllBrands, getAllBrandsByShop } from './brandSlice';
import {  brandService } from '../../services';

export const getAllBrandApi = async (dispatch) => {
    let res = await brandService.getAllBrands()
    dispatch(getAllBrands(res.data));
};

export const getAllBrandByShopIdApi = async (dispatch, shopId) => {
    let res = await brandService.getAllBrandsByShopId(shopId)
    dispatch(getAllBrandsByShop(res.data));
};
