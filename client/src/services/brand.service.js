import { axiosClient } from '~/api';

const branch_api = "/brands";

export const brandService = {
    getAllBrands() {
        return axiosClient.get(`${branch_api}`);
    },
    getAllBrandsByCategoryId(categoryId) {
        return axiosClient.get(`${branch_api}/categoryId/${categoryId}`);
    },
    getAllBrandsByShopId(shopId) {
        return axiosClient.get(`${branch_api}/shopId/${shopId}`);
    },
};
