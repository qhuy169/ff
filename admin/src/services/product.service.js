import axiosInstance from "./../api";

const branch_api = "products";

class ProductService {
  getProducts({
    categoryId = null,
    brandId = null,
    shopId = null,
    page = null,
    limit = null,
    location = null,
  }) {
    const params = {
      categoryId: categoryId,
      brandId: brandId,
      shopId: shopId,
      page: page,
      limit: limit,
      location: location,
    };
    const query = Object.keys(params).reduce((acc, key) => {
      const value = params[key];
      return value ? acc + `${key}=${params[key]}&` : acc;
    }, "");
    return axiosInstance.get(`${branch_api}?${query}`);
  }
  getProductById(id) {
    return axiosInstance.get(`${branch_api}/${id}`);
  }
  getProductByName(name) {
    return axiosInstance.get(`${branch_api}/${name}`);
  }
  getProductBySlug(slug) {
    return axiosInstance.get(`${branch_api}/slug/${slug}`);
  }
  createProduct(data) {
    return axiosInstance.post(`${branch_api}`, data);
  }
  updateProductById(id, formData) {
    return axiosInstance.put(`${branch_api}/${id}`, formData);
  }
  deleteProductById(id) {
    return axiosInstance.delete(`${branch_api}/${id}`);
  }
}

export default new ProductService();
