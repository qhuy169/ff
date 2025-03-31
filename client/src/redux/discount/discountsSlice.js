import { createSlice } from '@reduxjs/toolkit';

export const discounts = createSlice({
    name: 'discounts',
    initialState: {
        allDiscounts: {
            data: [],
        },
        checkoutDiscounts: {
            data: [],
        },
        allDiscountsUser:{
            data: [],
        },
        allDiscountsUserForShop:{
            data: [],
        }
    },
    reducers: {
        getAllDiscounts: (state, action) => {
            state.allDiscounts.data = action.payload;
        },
        getAllDiscountsUser: (state, action) => {
            state.allDiscountsUser.data = action.payload;
        },
        getAllDiscountsUserForShop: (state, action) => {
            state.allDiscountsUserForShop.data = action.payload;
        },
        savetDiscountsUser: (state, action) => {
            state.allDiscountsUserForShop.data = [...state.allDiscountsUserForShop.data, ...action.payload]
        },
        updateAllCheckoutDiscount: (state, action) => {
            state.checkoutDiscounts.data = action.payload || [];
        },
        reduceDiscountsShop: (state, action) => {
            const id = action.payload;
            const getItemReduce =  state.allDiscounts.data.map(
                (item) => 
                {
                    if(item.id === id){
                        item.quantity = item.quantity - 1
                    }
                    return item        
                }
            );
            state.allDiscounts.data =[...getItemReduce]
        },
        addCheckoutDiscount: (state, action) => {
            const newItem = action.payload;
            const duplicate = state.checkoutDiscounts.data.filter(
                (e) => e.id === newItem.id || e.shopId=== newItem.shopId
            );
            if (duplicate.length > 0) {
                state.checkoutDiscounts.data = state.checkoutDiscounts.data.filter(
                    (e) => e.id !== newItem.id && e.shopId !== newItem.shopId
                );

                state.checkoutDiscounts.data = [
                    ...state.checkoutDiscounts.data,
                    newItem
                ];
            } else {
                state.checkoutDiscounts.data = [
                    ...state.checkoutDiscounts.data,
                    {
                        ...action.payload,
                    },
                ];
                state.allDiscounts.data = [...state.allDiscounts.data, action.payload];
            }
        },
        deleteCheckoutDiscount: (state, action) => {
            const item = action.payload;
            state.checkoutDiscounts.data = state.checkoutDiscounts.data.filter(
                (e) => e.id !== item.id,
            );
        },
        clearCheckoutDiscount: (state, action) => {
            state.checkoutDiscounts.data = [];
        },
    },
});
export const {
    getAllDiscounts,
    updateAllCheckoutDiscount,
    addCheckoutDiscount,
    clearCheckoutDiscount,
    savetDiscountsUser,
    getAllDiscountsUser,
    getAllDiscountsUserForShop,
    reduceDiscountsShop
} = discounts.actions;

export default discounts.reducer;
