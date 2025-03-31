import axiosInstance from './../api';

const branch_api = "feedbacks";

export const ReviewService = {
  getReviews() {
    return axiosInstance.get(`${branch_api}`);
  },
  getReviewByProductId(id) {
    return axiosInstance.get(`${branch_api}?product=${id}`);
  },
  getProductById(id) {
    return axiosInstance.get(`productId/${id}`);
  },
  removeReview(id) {
    return axiosInstance.delete(`${branch_api}/${id}`);
  }
};

