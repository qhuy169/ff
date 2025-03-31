import { axiosClient } from '~/api';

export const emailService = {
   
    getAllEmailsByShopId(id) {
        return axiosClient.get(`shops/messages/${id}`);
    },
    getEmail(id) {
        return axiosClient.get(`/Mail/${id}`);
    },
    postEmail(email) {
        return axiosClient.post(`/Mail/RegularEmail`,email);
    },
    postEmailSchedule(email) {
        return axiosClient.post(`/Mail/Email`,email);
    },
    deleteEmail(id) {
        return axiosClient.delete(`/Mail/${id}`);
    },
};
