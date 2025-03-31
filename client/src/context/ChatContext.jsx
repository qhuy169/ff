import { createContext, useContext, useReducer, useState } from 'react';
import { auth } from '../firebase';
import { useEffect } from 'react';

export const ChatContext = createContext({});

export const ChatContextProvider = ({ children }) => {
    const [currentUser, setCurrentUser] = useState({});

    useEffect(() => {
        const unsub = auth.onAuthStateChanged((user) => {
            setCurrentUser(user);
            console.log(user);
        });
        return () => {
            unsub();
        };
    }, []);
    const INITIAL_STATE = {
        chatId: 'null',
        user: {},
    };

    const chatReducer = (state, action) => {
        switch (action.type) {
            case 'CHANGE_USER':
                return {
                    user: action.payload,
                    chatId:
                        currentUser.uid > action.payload.uid
                            ? currentUser.uid + action.payload.uid
                            : action.payload.uid + currentUser.uid,
                };

            default:
                return state;
        }
    };

    const [state, dispatch] = useReducer(chatReducer, INITIAL_STATE);

    const useChat = () => {
        const context = useContext(ChatContext);
    
        if (context === undefined) {
            throw new Error('useChat must be used within ChatContextProvider');
        }
    
        return context;
    };

    return <ChatContext.Provider value={{ data: state, dispatch, currentUser }}>{children}</ChatContext.Provider>;
};
