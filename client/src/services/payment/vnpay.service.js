import { axiosClient } from './../../api';

const branch_api = '/payment';

export const vnpay = {
    createVNPayPayment(data) {
        return axiosClient.post(`${branch_api}/vnpay/create-payment-url`, data);
    },
    getReturnVNPay(params) {
        const query = params.reduce((acc, { key, value }) => {
            return value ? acc + `${key}=${value}&` : acc;
        }, '');
        console.log(query);
        return axiosClient.post(`${branch_api}/vnpay/return?${query}`);
    },
    postPricePackage(body){
        return axiosClient.post(`${branch_api}/vnpay/create-payment-url/shop-price`,body);
    }
};
