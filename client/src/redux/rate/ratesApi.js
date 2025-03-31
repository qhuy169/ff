import { getRate, postRate } from './ratesSlice';
import { ratingService } from '../../services';

export const getAllRatesByShopApi = async (dispatch, shopId, params = {}) => {
    let res = await ratingService.getAllRatingByShop(shopId, params);
    dispatch(getRate(res.data?.content || []));
};

export const getRates = async (dispatch, id) => {
    let res = await ratingService.getRates(id);
    dispatch(getRate(res.data));
};
export const postRates = async (dispatch, data) => {
    let res = await ratingService.postRate(data);
    dispatch(postRate(res.data));
};
