import { createSlice } from '@reduxjs/toolkit';

export const shop = createSlice({
    name: 'shops',
    initialState: {
        allShops: {
            data: [],
        }
    },
    reducers: {
        getAllShops: (state, action) => {
            state.allShops.data = action.payload;
        }
    },
});
export const { getAllShops } = shop.actions;
export default shop.reducer;