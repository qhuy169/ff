import { getAllBrands } from './brandSlice';
import {  BrandService } from './../../services';

export const getAllBrandsApi = async (dispatch) => {
    let res = await BrandService.getAllBrands()
    dispatch(getAllBrands(res.data));
};
