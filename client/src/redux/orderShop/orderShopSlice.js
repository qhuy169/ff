import { createSlice } from '@reduxjs/toolkit';

export const orderShops = createSlice({
    name: 'orderShops',
    initialState: {
        pageOrder: {
            data: [],
        },
        orderDetail: {
            //data: [],
            data: [],
        },
        statisticPageOrder: {
            data: {},
        },
    },
    reducers: {
        getPageOrder: (state, action) => {
            state.pageOrder.data = action.payload;
        },
        getStatisticPageOrder: (state, action) => {
            state.statisticPageOrder.data = action.payload || {content: []};
            console.log(state.statisticPageOrder.data);
        },
        postOrder: (state, action) => {
            state.pageOrder.data = [...state.pageOrder.data, action.payload];
        },
        updateOrder: (state, action) => {
            state.pageOrder.data = Array.from(state.pageOrder.data || []).map(
                (item) => (item.id === action.payload.id ? action.payload : item)
              );
        },
        deleteOrder: (state, action) => {
            state.pageOrder.data = Array.from(state.pageOrder.data || []).filter(
                (item) => item.id !== action.payload
              );
        },
    },
});
export const { getPageOrder, getStatisticPageOrder, postOrder, updateOrder, deleteOrder } = orderShops.actions;
export default orderShops.reducer;
