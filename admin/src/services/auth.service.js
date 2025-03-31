import axiosInstance from './../api';

const branch_api = "/auth";

class AuthService {
  async login(email, password) {
    const { data } = await axiosInstance.post(`${branch_api}/login`, {
      email,
      password,
    });
    return data;
  }

  logout() {
    localStorage.removeItem("user");
    localStorage.removeItem('token')
    localStorage.removeItem('expiresAt')
  }

  forgotPassword(email) {
    return axiosInstance.post(`${branch_api}/forgot-password`, {
      email,
    });
  }

  checkToken(token, email) {
    return axiosInstance.post(`${branch_api}/check-token`, {
      token,
      email,
    });
  }

  resetPassword(token, email, password, password2) {
    return axiosInstance.post(`${branch_api}/reset-password`, {
      token,
      email,
      password,
      password2,
    });
  }

  register(username, email, password) {
    return axiosInstance.post(`${branch_api}/signup`, {
      username,
      email,
      password,
    });
  }

  getCurrentUser() {
    return axiosInstance.get(`/users/profile`);
  }
}

export default new AuthService();
