import { axiosClient } from '~/api';

export const searchService = {
    getResultSearchApi({keyword = '', page = null, limit = null, location = null}) {
        const params= {
            'keyword': keyword,
            'page': page,
            'limit': limit,
            'location': location,
        }
        const query = Object.keys(params).reduce((acc, key) => {
            const value = params[key];
            return value ? acc + `${key}=${params[key]}&` : acc;
        }, '')
        return axiosClient.get(`/products/search?${query}`);
    },
};
