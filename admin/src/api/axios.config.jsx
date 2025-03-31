import axios from "axios";

const baseURL = "http://localhost:8080/api/v1";

const axiosInstance = axios.create({
  baseURL,
  withCredentials: false,
  headers: {
    "Content-Type": "application/json",
  },
});

axiosInstance.interceptors.request.use(
  function (req) {
    const token = JSON.parse(localStorage.getItem("token"));
    if (token) req.headers.Authorization = `Bearer ${token}`;
    return req;
  },
  function (error) {
    return Promise.reject(error);
  }
);

axiosInstance.interceptors.response.use(
  function (res) {
    return res.data;
  },

  function (error) {
    const { response } = error;
    const { data } = error.response;
    if (response.status === 401) {
      alert("Vui lòng đăng nhập để thực hiện chức năng này");
    }
    return Promise.reject(error);
  }
);

export default axiosInstance;
