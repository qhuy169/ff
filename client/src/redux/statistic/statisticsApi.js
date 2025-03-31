import { getStatisticOrder } from './statisticsSlice';
import { statisticService } from '../../services';
import swal from 'sweetalert';

export const getStatisticOrdersApi = async (dispatch, shopId, param) => {
    let res = await statisticService.getStatisticOrders(shopId, param);
    dispatch(getStatisticOrder(res?.data))
};