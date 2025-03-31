import { createSlice } from '@reduxjs/toolkit';

export const notifications = createSlice({
    name: 'notifications',
    initialState: {
        allNotification: {
            data: [],
        },
    },
    reducers: {
        getAllNotifications: (state, action) => {
            state.allNotification.data = action.payload;
        },
        addNotification: (state, action) => {
            const newItem = action.payload;
            const duplicate = state.allNotification.data.filter((e) => e.userInfo.uid === newItem.userInfo.uid);
            if (duplicate.length > 0) {
                state.allNotification.data = state.allNotification.data.filter(
                    (e) => e.userInfo.uid !== newItem.userInfo.uid,
                );

                state.allNotification.data = [...state.allNotification.data, newItem];
            } else {
                state.allNotification.data = [
                    ...state.allNotification.data,
                    {
                        ...action.payload,
                    },
                ];
            }
        },
    },
});
export const { getAllNotifications, addNotification } = notifications.actions;
export default notifications.reducer;
