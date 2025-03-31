import { createSlice } from '@reduxjs/toolkit';

export const historyOrders = createSlice({
    name: 'historyOrders',
    initialState: {
        historyOrder: {
            data: [],
        },
    },

    reducers: {
        getHistoryOrder: (state, action) => {
            state.historyOrder.data = action.payload;
        },
    },
});
export const { getHistoryOrder } = historyOrders.actions;
export default historyOrders.reducer;
