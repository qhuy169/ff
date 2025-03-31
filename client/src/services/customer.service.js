import { axiosClient } from '~/api';

export const customerService = {
    getCustomerByPhone(number) {
        return axiosClient.get(`/customers?phone=${number}`);
    },
    postCustomer(data) {
        return axiosClient.post(`/customers`, data);
    },
};
