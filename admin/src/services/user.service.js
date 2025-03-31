import axiosInstance from './../api';

const branch_api = "/users";

export const UserService = {
  getUsers() {
    return axiosInstance.get(`${branch_api}`);
  },
  getUser(id) {
    return axiosInstance.get(`${branch_api}/${id}`);
  },
  removeUser(id) {
    return axiosInstance.delete(`${branch_api}/${id}`);
  },
  editUser(id, data) {
    return axiosInstance.patch(`${branch_api}/${id}`, data);
  },
  addUser(data) {
    return axiosInstance.post(`${branch_api}`, data);
  }
};

