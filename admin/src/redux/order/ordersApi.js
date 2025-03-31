import { getAllOrder } from './ordersSlice';
import { orderService } from '../../services/order.service';
export const getAllOrders = async (dispatch) => {
    let res = await orderService.getAllOrder();
    dispatch(getAllOrder(res.data));
    
};




