import axiosInstance from './../api';

const branch_api = "/shops";

class ShopService {
    getAllShops() {
        return axiosInstance.get(`${branch_api}`);
    }
};

export default new ShopService();
