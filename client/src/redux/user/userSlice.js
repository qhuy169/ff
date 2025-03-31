import { createSlice } from '@reduxjs/toolkit';
import { DEFAULT_STORE } from '../../utils';

export const userSlice = createSlice({
    name: 'user',
    initialState: {
        user: null,
    },
    reducers: {
        login: (state, action) => {
            state.user = action.payload;
            localStorage.setItem(DEFAULT_STORE.USER_INFO, JSON.stringify(action.payload));
        },
        logout: (state) => {
            state.user = null;
            localStorage.removeItem(DEFAULT_STORE.TOKEN);
            localStorage.removeItem(DEFAULT_STORE.USER_INFO);
        },
    },
});
export const { login, logout } = userSlice.actions;

export const selectUser = (state) => state.user.user;

export default userSlice.reducer;
