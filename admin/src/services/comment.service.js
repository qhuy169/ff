import axiosInstance from './../api';

const branch_api = "\comments";

class CommentService {
  getComments(page, limit) {
    return axiosInstance.get(`${branch_api}`);
  }
  getCommentByProductId(id) {
    return axiosInstance.get(`${branch_api}?productId=${id}`);
  }
  getProductById(id) {
    return axiosInstance.get(`products?id=${id}`);
  }
  removeComment(id) {
    return axiosInstance.delete(`${branch_api}/${id}`);
  }
  repairComment(id,data) {
    return axiosInstance.patch(`${branch_api}/${id}`,data);
  }
  postComment(data){
    return axiosInstance.post(`${branch_api}/`,data);
  }
};

export default new CommentService;