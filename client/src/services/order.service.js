import { axiosClient } from '~/api';

const branch_api = "/orders";

export const orderService = {
    postOrder(data) {
        return axiosClient.post(`${branch_api}`, data);
    },
    getOderById(id){
        return axiosClient.get(`${branch_api}/${id}`);
    },
    updateOrder(data, id) {
        return axiosClient.put(`${branch_api}/${id}`, data);
    },
    updatePaymentOrder(data, id) {
        return axiosClient.put(`${branch_api}/${id}/payment`, data);
    },
    deleteOrderById(id) {
        return axiosClient.delete(`${branch_api}/${id}`);
    },
    getAllOrdersByShopId(shopId) {
        return axiosClient.get(`${branch_api}/shopId/${shopId}`);
    },
};
