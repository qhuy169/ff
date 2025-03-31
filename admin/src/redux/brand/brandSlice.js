import { createSlice } from '@reduxjs/toolkit';

export const brands = createSlice({
    name: 'brands',
    initialState: {
        allBrands: {
            data: [],
        }
    },
    reducers: {
        getAllBrands: (state, action) => {
            state.allBrands.data = action.payload;
        }
    },
});
export const { getAllBrands } = brands.actions;
export default brands.reducer;