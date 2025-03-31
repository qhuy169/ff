import { createSlice } from '@reduxjs/toolkit';

export const brands = createSlice({
    name: 'brands',
    initialState: {
        allBrand: {
            data: [],
        },
        allBrandByShop: {
            data: [],
        },
    },
    reducers: {
        getAllBrands: (state, action) => {
            state.allBrand.data = action.payload;
        },
        getAllBrandsByShop: (state, action) => {
            state.allBrandByShop.data = action.payload;
        },
    },
});
export const { getAllBrands, getAllBrandsByShop } = brands.actions;
export default brands.reducer;