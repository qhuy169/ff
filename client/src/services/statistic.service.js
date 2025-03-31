import { axiosClient } from '~/api';

const branch_api = 'shops/statistic';

export const statisticService = {
    getStatisticOrders(shopId, {startDate= null, endDate= null, timeDistance= 'DAY'}) {
        const params = {
            startDate: startDate,
            endDate: endDate,
            timeDistance: timeDistance
        };
        const query = Object.keys(params).reduce((acc, key) => {
            const value = params[key];
            return value ? acc + `${key}=${params[key]}&` : acc;
        }, '');
        return axiosClient.get(`${branch_api}/order/${shopId}?${query}`);
    },
}