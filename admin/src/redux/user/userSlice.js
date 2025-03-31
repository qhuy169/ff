import { createSlice } from '@reduxjs/toolkit';
export const users = createSlice({
    name: 'users',
    initialState: {
        user: {
            data: []
        },
    },
    reducers: {
        getUserById: (state, action) => {
            state.user.data = action.payload;
        },
        removeUserById: (state, action) => {
            state.user.data = action.payload;
        },
        editUserById: (state, action) => {
            state.user.data = action.payload;
        },
    },
});
export const { getUserById, removeUserById, editUserById } = users.actions;
export default users.reducer;
