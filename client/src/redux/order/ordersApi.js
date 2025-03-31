import { getPageOrder, postOrder, createOrder, deleteOrder,getOrderDetail } from './orderSlice';
import { orderService } from '~/services/order.service';
import { MESSAGE } from '../../utils';
import swal from 'sweetalert';

export const getAllOrdersByShopApi = async (dispatch, shopId) => {
    let res = await orderService.getAllOrdersByShopId(shopId);
    console.log(res);
    dispatch(getPageOrder(res.data));
};

export const getOrdersById = async (dispatch, idOder) => {
    let res = await orderService.getOderById(idOder);
    console.log(res);
    dispatch(getOrderDetail(res.data));
};

export const postOrdersApi = async (dispatch, data, navigate) => {
    try {
        let res = await orderService.postOrder(data);
        dispatch(postOrder(res?.data));
        navigate('/order');
    } catch (err) {
        swal({text: MESSAGE.ERROR_ACTION, icon: 'error',});
        navigate('/');
    }
};

export const updateOrdersApi = async (dispatch, data, id) => {
    let res = await orderService.updateOrder(data, id);
    dispatch(postOrder(res?.data));
};

export const updatePaymentOrdersApi = async (dispatch, data, id) => {
    let res = await orderService.updatePaymentOrder(data, id);
    dispatch(postOrder(res?.data));
};

export const deleteOrdersByIdApi = async (dispatch, id, navigate=null) => {
    try {
        await orderService.deleteOrderById(id);
        dispatch(deleteOrder());
        swal({text: 'Hủy đơn hàng thành công!', icon: 'success',});
    } catch (err) {
        swal({text: MESSAGE.ERROR_ACTION, icon: 'error',});
    } 
    if (navigate) {
        navigate('/');
    }
};
