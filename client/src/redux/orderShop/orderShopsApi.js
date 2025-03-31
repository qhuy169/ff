import { getPageOrder, getStatisticPageOrder, postOrder, updateOrder, deleteOrder } from './orderShopSlice';
import { orderShopService } from '../../services';

export const getAllOrdersByShopApi = async (dispatch, shopId, params = {}) => {
    let res = await orderShopService.getAllOrdersByShopId(shopId, params);
    if (params?.limit === 5) {
        dispatch(getStatisticPageOrder(res.data));
    } else {
        dispatch(getPageOrder(res.data));
    }
};

export const updateStatusOrderApi = async (dispatch, id, data) => {
    let res = await orderShopService.updateStatus(id, data);
    dispatch(updateOrder(res?.data));
};
