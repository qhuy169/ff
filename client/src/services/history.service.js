import { axiosClient } from '~/api';

export const historyService = {
    getHistoryOrderByUserId(userId) {
        return axiosClient.get(`/orders/userId/${userId}`);
    },
    updateHistoryOrder(id, data) {
        return axiosClient.patch(`/orders/${id}`, data);
    },
};
