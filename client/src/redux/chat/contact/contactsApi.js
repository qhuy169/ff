import { getAllChatContacts, addChatContact } from './contactsSlice.js';
import firebase, { auth, db, storage } from '../../../firebase';

export const getAllChatContactsApi = async (dispatch, uid) => {
    db.collection('userChats')
        .doc(uid)
        .onSnapshot((doc) => {
            dispatch(getAllChatContacts(Object.values(doc.data()) || []));
        });
};

export const addChatContactApi = async (dispatch, currentUser, currentChat) => {
    const combinedId =
            currentUser.uid > currentChat.uid ? currentUser.uid + currentChat.uid : currentChat.uid + currentUser.uid;
        try {
            const res = db
                .collection('chats')
                .doc(combinedId)
                .get()
                .then((doc) => {
                    if (!doc.exists) {
                        console.log(currentUser, currentChat);
                        db.collection('chats').doc(combinedId).set({ messages: [] });
                        db.collection('userChats')
                            .doc(currentUser.uid)
                            .update({
                                [combinedId + '.userInfo']: {
                                    uid: currentChat.uid,
                                    displayName: currentChat.displayName,
                                    photoURL: currentChat.photoURL,
                                },
                                [combinedId + '.date']: firebase.firestore.Timestamp.now(),
                            });
                        db.collection('userChats')
                            .doc(currentChat.uid)
                            .update({
                                [combinedId + '.userInfo']: {
                                    uid: currentUser.uid,
                                    displayName: currentUser.displayName,
                                    photoURL: currentUser.photoURL,
                                },
                                [combinedId + '.date']: firebase.firestore.Timestamp.now(),
                            });
                        dispatch(addChatContact({userInfo: currentChat}));
                    }
                });
        } catch (err) {}
}
