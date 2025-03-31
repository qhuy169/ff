import axiosInstance from './../api';

const branch_api = "/categories";

class CategoryService {
    getAllCategories() {
        return axiosInstance.get(`${branch_api}`);
    }
    
    getCategoryBySlug(slug) {
        return axiosInstance.get(`${branch_api}/slug/${slug}`);
    }
    
    createCategory(data) {
        return axiosInstance.post(`${branch_api}`, data, {
            headers: { "Content-Type": "multipart/form-data" },
        });
    }
    
    deleteCategory(id) {
        return axiosInstance.delete(`${branch_api}/${id}`);
    }
    
    updateCategory(id, data) {
        return axiosInstance.put(`${branch_api}/${id}`, data, {
            headers: { "Content-Type": "multipart/form-data" },
        });
    }
}

export default new CategoryService();
