import { createSlice } from '@reduxjs/toolkit';
export const comments = createSlice({
    name: 'comments',
    initialState: {
        comment: {
            data: [],
            datafull:[]
        },
    },
    reducers: {
        getAllCommentByProductId: (state, action) => {
            state.comment.data = action.payload;
        },
        getAllComment: (state, action) => {
            state.comment.datafull = action.payload;
        },
        removeCommentByProductId: (state, action) => {
            state.comment.data = action.payload;
        },
        repllyCommentByProductId: (state, action) => {
            state.comment.data = action.payload;
        },
    },
});
export const { getAllComment,getAllCommentByProductId,removeCommentByProductId,repllyCommentByProductId } = comments.actions;
export default comments.reducer;
