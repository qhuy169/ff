import { createSlice } from '@reduxjs/toolkit';

export const categories = createSlice({
    name: 'categories',
    initialState: {
        allCategory: {
            data: [],
        },
        allCategoryByShop: {
            data: [],
        },
        oneCategory: {
            data: {},
        },
    },
    reducers: {
        getAllCategories: (state, action) => {
            state.allCategory.data = action.payload;
        },
        getAllCategoriesByShop: (state, action) => {
            state.allCategoryByShop.data = action.payload;
        },
        getOneCategory: (state, action) => {
            state.oneCategory.data = action.payload;
        },
    },
});
export const { getAllCategories, getAllCategoriesByShop, getOneCategory } = categories.actions;
export default categories.reducer;