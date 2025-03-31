import { axiosClient } from '~/api';

const branch_api = "/ordershops";

export const orderShopService = {
    getAllOrdersByShopId(shopId, { page = null, limit = null, sortField = null, sortDir = null }) {
        const params = {
            page: page,
            limit: limit,
            sortField: sortField,
            sortDir: sortDir,
        };
        const query = Object.keys(params).reduce((acc, key) => {
            const value = params[key];
            return value ? acc + `${key}=${params[key]}&` : acc;
        }, '');
        return axiosClient.get(`${branch_api}/shopId/${shopId}?${query}`);
    },
    updateStatus(id, data) {
        return axiosClient.put(`${branch_api}/${id}/status`, data);
    },
    getLogs(id) {
        return axiosClient.get(`${branch_api}/${id}`);
    },
};
