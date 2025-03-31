import React, { useContext, useEffect, useRef, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { useNavigate, useParams, useSearchParams } from 'react-router-dom';
import styled from 'styled-components';
import ChatContainer from '../../../components/Chat/ChatContainer';
import Contacts from '../../../components/Chat/Contact';
import Welcome from '../../../components/Chat/Welcome';
import { ChatContext } from '../../../context/ChatContext';
import { db } from '../../../firebase';
import { addChatContactApi, getAllChatContactsApi } from '../../../redux/chat/contact/contactsApi';
import { isObjEmpty } from '../../../utils/utils';
import './Chat.scss';

export default function Chat({ title }) {
    const navigate = useNavigate();
    const dispatch = useDispatch();
    const socket = useRef();
    const contacts = useSelector((state) => state.chatContacts.allChatContact.data);
    const [currentChat, setCurrentChat] = useState(undefined);
    const { currentUser } = useContext(ChatContext);
    const [err, setErr] = useState(false);

    const { userId } = useParams();
    let [searchParams, setSearchParams] = useSearchParams();
    let keySearch = searchParams.get('product');

    useEffect(() => {
        if (currentUser?.uid) {
            const unsubscribe = getAllChatContactsApi(dispatch, currentUser?.uid);
            return () => unsubscribe;
        }
    }, [currentUser]);

    useEffect(() => {
        if (userId && !isObjEmpty(currentUser)) {
            console.log('user', userId)
            db.collection('users')
                .where('_id', '==', Number(userId))
                .get()
                .then((querySnapshot) => {
                    querySnapshot.forEach((doc) => {
                        let user = doc.data();
                        addChatContactApi(dispatch, currentUser, user);
                        if (currentChat?.uid !== user.uid) {
                            setCurrentChat(user);
                        }
                        // return;
                    });
                })
                .catch((err) => {
                    console.log(err);
                    setErr(true);
                });
                searchParams.delete('product');
                setSearchParams(searchParams);
        }
    }, [userId, currentUser]);

    const handleChatChange = (chat) => {
        setCurrentChat(chat);
    };
    return (
        <>
            <Container>
                <div className="container">
                    <Contacts contacts={contacts} changeChat={handleChatChange} />
                    {currentChat === undefined ? (
                        <Welcome />
                    ) : (
                        <ChatContainer currentChat={currentChat} socket={socket} />
                    )}
                </div>
            </Container>
        </>
    );
}

const Container = styled.div`
    height: 100vh;
    width: 100vw;
    display: flex;
    flex-direction: column;
    justify-content: center;
    gap: 1rem;
    align-items: center;
    background-color: #ffff;
    .container {
        height: 85vh;
        width: 85vw;
        border-radius: 2%;
        background-color: #00000076;
        display: grid;
        grid-template-columns: 25% 75%;
        @media screen and (min-width: 720px) and (max-width: 1080px) {
            grid-template-columns: 35% 65%;
        }
    }
`;
