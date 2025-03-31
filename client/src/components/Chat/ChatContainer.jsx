import React, { useState, useEffect, useRef, useContext } from 'react';
import styled from 'styled-components';
import ChatInput from './ChatInput';
import Logout from './Logout';
import { v4 as uuid } from 'uuid';
import Logo from '../../assets/logowb.png';
import axios from 'axios';
import firebase, { auth, db, storage } from '../../firebase';
import { ChatContext } from '../../context/ChatContext';
import { addChatContactApi } from '../../redux/chat/contact/contactsApi';
import { useDispatch } from 'react-redux';
import { isObjEmpty } from '../../utils';
import { postEmail } from '../../redux/Email/EmailApi';

// import { sendMessageRoute, recieveMessageRoute } from "../utils/APIRoutes";

export default function ChatContainer({ currentChat, socket }) {
    const dispatch = useDispatch();

    const { currentUser } = useContext(ChatContext);
    const { data } = useContext(ChatContext);

    const [messages, setMessages] = useState([]);
    const scrollRef = useRef();
    const [arrivalMessage, setArrivalMessage] = useState(null);

    useEffect(() => {
        if (!isObjEmpty(currentUser)) {
            addChatContactApi(dispatch, currentUser, currentChat);
        }
    }, [currentUser, currentChat]);
    console.log(currentUser?.email);

    const handleSendMsg = async (msg) => {
        let message = {
            id: uuid(),
            msg,
            senderId: currentUser.uid,
            date: firebase.firestore.Timestamp.now(),
        };
        db.collection('chats')
            .doc(data.chatId)
            .update({
                messages: firebase.firestore.FieldValue.arrayUnion(message),
            });
        setMessages((prev) => [...prev, message]);

        db.collection('userChats')
            .doc(currentUser.uid)
            .update({
                [data.chatId + '.lastMessage']: {
                    msg,
                },
                [data.chatId + '.date']: firebase.firestore.Timestamp.now(),
            });

        db.collection('userChats')
            .doc(data.user.uid)
            .update({
                [data.chatId + '.lastMessage']: {
                    msg,
                },
                [data.chatId + '.date']: firebase.firestore.Timestamp.now(),
            });
        postEmail(dispatch, currentUser?.email)
    };

    useEffect(() => {
        const unsubscribe = db
            .collection('chats')
            .doc(data.chatId)
            // .limit(50)
            .onSnapshot((doc) => {
                doc.exists && setMessages(doc.data().messages);
            });

        return () => {
            unsubscribe();
        };
    }, [data.chatId]);

    useEffect(() => {
        // if (socket.current) {
        //   socket.current.on("msg-recieve", (msg) => {
        //     setArrivalMessage({ fromSelf: false, message: msg });
        //   });
        // }
    }, []);

    useEffect(() => {
        arrivalMessage && setMessages((prev) => [...prev, arrivalMessage]);
    }, [arrivalMessage]);

    useEffect(() => {
        scrollRef.current?.scrollIntoView({ behavior: 'smooth' });
    }, [messages]);

    return (
        <Container>
            <div className="chat-header">
                <div className="user-details">
                    <div className="avatar">
                        <img
                            // src={`data:image/svg+xml;base64,${currentChat.avatarImage}`}
                            src={currentChat.photoURL || Logo}
                            alt=""
                        />
                    </div>
                    <div className="username">
                        <h3>{currentChat.displayName}</h3>
                    </div>
                </div>
                <Logout />
            </div>
            <div className="chat-messages">
                {messages.map((message) => {
                    return (
                        <div ref={scrollRef} key={uuid()}>
                            <div className={`message ${message?.senderId === currentUser.uid ? 'sended' : 'recieved'}`}>
                                <div className="content ">
                                    <p>{message.msg}</p>
                                </div>
                            </div>
                        </div>
                    );
                })}
            </div>
            <ChatInput handleSendMsg={handleSendMsg} />
        </Container>
    );
}

const Container = styled.div`
    display: grid;
    grid-template-rows: 10% 80% 10%;
    gap: 0.1rem;
    overflow: hidden;
    @media screen and (min-width: 720px) and (max-width: 1080px) {
        grid-template-rows: 15% 70% 15%;
    }
    .chat-header {
        background-color: #080420;
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding: 0 2rem;
        .user-details {
            display: flex;
            align-items: center;
            gap: 1rem;
            .avatar {
                img {
                    height: 3rem;
                }
            }
            .username {
                h3 {
                    color: white;
                }
            }
        }
    }
    .chat-messages {
        background-color: #080420;
        padding: 1rem 2rem;
        display: flex;
        flex-direction: column;
        gap: 1rem;
        overflow: auto;
        &::-webkit-scrollbar {
            width: 0.2rem;
            &-thumb {
                background-color: #ffffff39;
                width: 0.1rem;
                border-radius: 1rem;
            }
        }
        .message {
            display: flex;
            align-items: center;
            .content {
                max-width: 40%;
                overflow-wrap: break-word;
                padding: 1rem;
                font-size: 1.1rem;
                border-radius: 1rem;
                color: #d1d1d1;
                @media screen and (min-width: 720px) and (max-width: 1080px) {
                    max-width: 70%;
                }
            }
        }
        .sended {
            justify-content: flex-end;
            .content {
                background-color: #4f04ff21;
            }
        }
        .recieved {
            justify-content: flex-start;
            .content {
                background-color: #9900ff20;
            }
        }
    }
`;