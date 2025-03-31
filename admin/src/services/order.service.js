import axiosInstance from './../api';

const branch_api = "/orders";

export const orderService = {
    getAllOrder() {
        return axiosInstance.get(`${branch_api}`);
    },


    updateHistoryOrder(id, data) {
        return axiosInstance.patch(`${branch_api}/${id}`, data);
    },

};
