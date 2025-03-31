import { getAllReviewByProductId,removeReviewByProductId } from './reviewSlice';
import { ReviewService } from '../../services/review.service';
export const getAllReviews = async (dispatch,id) => {
    let res = await ReviewService.getReviewByProductId(id);
    dispatch(getAllReviewByProductId(res.data));
    
};
export const removeReviews = async (dispatch,id,idpro) => {
    await ReviewService.removeReview(id);
    let res = await ReviewService.getReviewByProductId(idpro);
    dispatch(removeReviewByProductId(res.data));
    
};
export const repairReviews = async (dispatch,id,idpro,data) => {
    await ReviewService.repairReview(id,data);
    let res = await ReviewService.getReviewByProductId(idpro);
    dispatch(removeReviewByProductId(res.data));
    
};


