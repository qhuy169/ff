
import { shopService } from '../../services';
import { DEFAULT_STORE } from '../../utils';
import { login } from '../user/userSlice';
import { getOneShop, getOverviewShop, getViewShop } from './shopSlice';

export const getShopBySlugApi = async (dispatch, slug) => {
    try {
        const res = await shopService.getShopBySlug(slug)
        dispatch(getOneShop(res.data));
    } catch (err) {
        console.error(err);
    }
}

export const PostRegisterShop = async (dispatch, data, navigate) => {
    try {
        let res = await shopService.postShopApi(data);

        dispatch(login(res.data));
        navigate('/Seller')
    } catch (error) {
        console.error(error)
    }

};

export const getOverviewByShopIdApi = async (dispatch, shopId) => {
    try {
        const res = await shopService.getOverviewById(shopId)
        dispatch(getOverviewShop(res.data));
    } catch (err) {
        console.error(err);
    }
}

export const getByShopId = async (dispatch, shopId) => {
    try {
        const res = await shopService.getShopById(shopId)
        //Tồn tại ngày đăng kí
        //1 là đưa ra thông báo
        if (res.data.registerPriceAt != null) {
            const currentDate = new Date();
            const endDate = new Date(res.data.endPriceAt);

            const oneDayInMillis = 24 * 60 * 60 * 1000;

            // Calculate the difference in milliseconds between the current date and the end date
            const timeDifference = endDate.getTime() - currentDate.getTime();

            // Check if the time difference is less than or equal to one day
            if (timeDifference <= oneDayInMillis) {
                res.data.checkPackage = 1
            } else {
                res.data.checkPackage = 0
            }
        }
        //Chưa đăng kí trước đó
        else {
            res.data.checkPackage = 1
        }
        dispatch(getViewShop(res.data));
    } catch (err) {
        console.error(err);
    }
}



