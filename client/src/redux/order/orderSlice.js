import { createSlice } from '@reduxjs/toolkit';

const order = localStorage.getItem('order') !== null ? JSON.parse(localStorage.getItem('order')) : {};
export const orders = createSlice({
    name: 'orders',
    initialState: {
        pageOrder: {
            data: [],
        },
        order: {
            //data: [],
            data: order,
        },
        orderDetail: {
            //data: [],
            data: [],
        },
    },
    reducers: {
        getPageOrder: (state, action) => {
            state.pageOrder.data = action.payload;
        },
        getOrderDetail: (state, action) => {
            state.orderDetail.data = action.payload;
        },
        postOrder: (state, action) => {
            state.order.data = action.payload;
        },
        createOrder: (state, action) => {
            state.order.data = action.payload;
            const orderData = JSON.stringify(action.payload);
            localStorage.setItem('order', orderData);
        },
        deleteOrder: (state, action) => {
            state.order.data = null;
            localStorage.removeItem('order');
        },
    },
});
export const { getPageOrder, postOrder, createOrder, deleteOrder,getOrderDetail } = orders.actions;
export default orders.reducer;
