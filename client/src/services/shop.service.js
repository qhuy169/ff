import { axiosClient } from '~/api';

const branch_api = "/shops"

export const shopService = {
    getShopBySlug(slug) {
        return axiosClient.get(`${branch_api}/slug/${slug}`)
    },
    postShopApi(data) {
        return axiosClient.post(`${branch_api}`,data);
    },
    getOverviewById(id) {
        return axiosClient.get(`${branch_api}/overview/${id}`)
    },
    getPricesPackage() {
        return axiosClient.get(`${branch_api}/prices`)
    },
    getShopById(id) {
        return axiosClient.get(`${branch_api}/${id}`)
    }
};
