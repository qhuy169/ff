import { createSlice } from '@reduxjs/toolkit';

export const categories = createSlice({
    name: 'categories',
    initialState: {
        allCategories: {
            data: [],
        },
        oneCategory: {
            data: {},
        },
    },
    reducers: {
        getAllCategories: (state, action) => {
            state.allCategories.data = action.payload;
        },
        getOneCategory: (state, action) => {
            state.oneCategories.data = action.payload;
        },
    },
});
export const { getAllCategories, getOneCategory } = categories.actions;
export default categories.reducer;