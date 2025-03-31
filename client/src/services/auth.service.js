import { axiosClient } from '~/api/';

const branch_api = "/auth";

export const authService = {

    postLogin(data) {
        return axiosClient.post(`${branch_api}/login`,data);
    },
    postRegister(data) {
        return axiosClient.post(`${branch_api}/register`,data);
    },
    checkAccessToken() {
        return axiosClient.post(`${branch_api}/check-access-token`);
    }
};
