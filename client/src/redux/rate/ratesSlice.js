import { createSlice } from '@reduxjs/toolkit';

export const rates = createSlice({
    name: 'rates',
    initialState: {
        rate: {
            data: {},
        },
    },
    reducers: {
        getRate: (state, action) => {
            state.rate.data = action.payload;
            console.log(state.rate.data);
        },
        postRate: (state, action) => {
            state.rate.data = action.payload;
        },
    },
});
// [...state.rate.data, action.payload];
export const { getRate, postRate } = rates.actions;
export default rates.reducer;
