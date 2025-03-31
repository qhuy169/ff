import { axiosClient } from '~/api';

const branch_api = "/categories";

export const categoryService = {
    getAllCategories() {
        return axiosClient.get(`${branch_api}`);
    },
    getAllCategoriesByShopId(shopId) {
        return axiosClient.get(`${branch_api}/shopId/${shopId}`);
    },
    getCategoryBySlug(slug) {
        return axiosClient.get(`${branch_api}/slug/${slug}`);
    },
};
