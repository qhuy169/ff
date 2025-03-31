import { axiosClient } from '~/api/';

export const userService = {
    getUserById(id) {
        return axiosClient.get(`/users?id=${id}`);
    },
    getUserByAccess() {
        return axiosClient.get(`/users/access-token`);
    },
    editUser(phone, data) {
        return axiosClient.patch(`/users/${phone}`, data);
    }
};
