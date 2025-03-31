import { axiosClient } from '../api';

const branch_api = "/discounts";

export const discountService = {
    getAllDiscounts() {
        return axiosClient.get(`${branch_api}`);
    },
    getDiscountById(id) {
        return axiosClient.get(`${branch_api}/${id}`);
    },
    getDiscountByUser() {
        return axiosClient.get(`${branch_api}/user`);
    },
    getDiscountByShopId(id) {
        return axiosClient.get(`${branch_api}/search?shopId=${id}`);
    },
    checkDiscountCode(code) {
        return axiosClient.get(`${branch_api}/check-code/${code}`);
    },
    postDiscount(data) {
        return axiosClient.post(`${branch_api}`, data, {
          headers: {
            "Content-Type": "multipart/form-data",  // Ensure the correct header
          },
        });
    },    
    saveDiscount(code) {
        return axiosClient.put(`${branch_api}/user/add-all`,code);
    },
    putDiscount(id, data) {
        return axiosClient.put(`${branch_api}/${id}`, data, {
            headers: { "Content-Type": "multipart/form-data" },
        });
    },    
    deleteDiscount(id) {
        return axiosClient.delete(`${branch_api}/${id}`);
    },
};
