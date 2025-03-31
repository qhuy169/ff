import { createSlice } from '@reduxjs/toolkit';

export const chatContacts = createSlice({
    name: 'chatContacts',
    initialState: {
        allChatContact: {
            data: [],
        },
    },
    reducers: {
        getAllChatContacts: (state, action) => {
            state.allChatContact.data = action.payload;
        },
        addChatContact: (state, action) => {
            const newItem = action.payload;
            const duplicate = state.allChatContact.data.filter((e) => e.userInfo.uid === newItem.userInfo.uid);
            if (duplicate.length > 0) {
                state.allChatContact.data = state.allChatContact.data.filter(
                    (e) => e.userInfo.uid !== newItem.userInfo.uid,
                );

                state.allChatContact.data = [...state.allChatContact.data, newItem];
            } else {
                state.allChatContact.data = [
                    ...state.allChatContact.data,
                    {
                        ...action.payload,
                    },
                ];
            }
        },
    },
});
export const { getAllChatContacts, addChatContact } = chatContacts.actions;
export default chatContacts.reducer;
