import { axiosClient } from '~/api';

const branch_api = "/feedbacks";

export const ratingService = {
    getAllRatingByShop(shopId, { page = null, limit = null, sortField = null, sortDir = null }) {
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
    getRating(productId) {
        return axiosClient.get(`${branch_api}/productId/${productId}`);
    },
    postRating(data) {
        return axiosClient.post(`${branch_api}`, data);
    },
    patchRating(id, data) {
        return axiosClient.patch(`${branch_api}/${id}`, data);
    },
};
