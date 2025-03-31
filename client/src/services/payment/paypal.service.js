import { axiosClient } from './../../api';

const branch_api = '/payment';

export const paypal = {
    createPayPalPayment(data) {
        return axiosClient.post(`${branch_api}/paypal/create-payment-url`, data);
    },
    // getReturnVNPay(params) {
    //     const query = params.reduce((acc, { key, value }) => {
    //         return value ? acc + `${key}=${value}&` : acc;
    //     }, '');
    //     return axiosClient.post(`${branch_api}/paypal/return?${query}`);
    // },
    postPricePackage(body){
        return axiosClient.post(`${branch_api}/paypal/create-payment-url/shop-price`,body);
    }
};
