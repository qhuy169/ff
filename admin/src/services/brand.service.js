import axiosInstance from './../api';

const branch_api = "/brands";

class BrandService {
    getAllBrands() {
        return axiosInstance.get(`${branch_api}`);
    }
    getAllBrandsByCategoryId(categoryId) {
        return axiosInstance.get(`${branch_api}/categoryId/${categoryId}`);
    }
};

export default new BrandService();
