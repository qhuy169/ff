import { getAllComment,getAllCommentByProductId,removeCommentByProductId,repllyCommentByProductId } from './commentSlice';
import { CommentService } from '../../services';
export const getAllComments = async (dispatch,id) => {
    let res = await CommentService.getCommentByProductId(id);
    dispatch(getAllCommentByProductId(res.data));
    
};
export const getAllCommentsfull = async (dispatch) => {
    let res = await CommentService.getComments();
    dispatch(getAllComment(res.data));
    
};
export const removeComments = async (dispatch,id,idpro) => {
    await CommentService.removeComment(id);
    let res = await CommentService.getCommentByProductId(idpro);
    dispatch(removeCommentByProductId(res.data));
    
};
export const repplyComments = async (dispatch,idpro,data) => {
    await CommentService.postComment(data);
    let res = await CommentService.getCommentByProductId(idpro);
    dispatch(repllyCommentByProductId(res.data));
    
};


