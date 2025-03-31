import { getAllShops } from './shopSlice';
import {  ShopService } from './../../services';

export const getAllShopsApi = async (dispatch) => {
    let res = await ShopService.getAllShops()
    dispatch(getAllShops(res.data));
};
