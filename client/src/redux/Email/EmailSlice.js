import { createSlice } from '@reduxjs/toolkit';

export const emails = createSlice({
    name: 'emails',
    initialState: {
        allEmail: {
            data: [],
        },
        detailEmail: {
            data: {},
        },
    },
    reducers: {
        AllEmails: (state, action) => {
            state.allEmail.data = action.payload?.content?.sort((a, b)=> - a.id + b.id) || [];
        },
       
        EmailDetail: (state, action) => {
            state.detailEmail.data = action.payload;
        },
        // updateDiscussRating: (state, action) => {
        //     const rating = state.productDetail.data.rating.find((rating) => rating.id === action.payload.idRating);
        //     if (rating) {
        //         const res = rating.discuss.push(action.payload);
        //     }
        // },
    },
});
export const {
   EmailDetail,
    AllEmails
} = emails.actions;

export default emails.reducer;
