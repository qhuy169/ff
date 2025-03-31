import { createSlice } from '@reduxjs/toolkit';
export const reviews = createSlice({
    name: 'reviews',
    initialState: {
        review: {
            data: []
        },
    },
    reducers: {
        getAllReviewByProductId: (state, action) => {
            state.review.data = action.payload;
        },
        removeReviewByProductId: (state, action) => {
            state.review.data = action.payload;
        },
        repairReviewByProductId: (state, action) => {
            state.review.data = action.payload;
        },
    },
});
export const { getAllReviewByProductId,removeReviewByProductId,repairReviewByProductId } = reviews.actions;
export default reviews.reducer;
