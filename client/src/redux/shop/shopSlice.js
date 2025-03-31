import { createSlice } from '@reduxjs/toolkit';

export const shops = createSlice({
    name: 'shops',
    initialState: {
        oneShop: {
            data: '',
        },
        overviewShop: {
            data: '',
        },
        viewShop: null
    },
    reducers: {
        getOneShop: (state, action) => {
            state.oneShop.data = action.payload;
        },
        getOverviewShop: (state, action) => {
            state.overviewShop.data = action.payload;
        },
        getViewShop: (state, action) => {
            state.viewShop = action.payload;
        },
    },
});
export const {
    getOneShop,
    getOverviewShop,
    getViewShop
} = shops.actions;

export default shops.reducer;