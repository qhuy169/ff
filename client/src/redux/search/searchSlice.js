import { createSlice } from '@reduxjs/toolkit';

export const searchSlice = createSlice({
    name: 'search',
    initialState: {
        pageSearch: {
            data: [],
        },
    },
    reducers: {
        getResultSearch: (state, action) => {
            state.pageSearch.data = action.payload;
        },
        removeResultSearch: (state) => {
            state.pageSearch.data = '';
        },
    },
});

export const { getResultSearch, removeResultSearch } = searchSlice.actions;

export default searchSlice.reducer;
