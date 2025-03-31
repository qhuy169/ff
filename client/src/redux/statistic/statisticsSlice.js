import { createSlice } from '@reduxjs/toolkit';

// const order = localStorage.getItem('order') !== null ? JSON.parse(localStorage.getItem('order')) : {};
export const statistics = createSlice({
    name: 'statistics',
    initialState: {
        statisticOrder: {
            data: [],
        },
    },
    reducers: {
        getStatisticOrder: (state, action) => {
            state.statisticOrder.data = action.payload;
        },
    },
});
export const { getStatisticOrder } = statistics.actions;
export default statistics.reducer;
