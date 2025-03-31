import { getAllNotifications, addNotification } from "./notificationsSlice.js";
import firebase, { auth, db, storage } from "../../firebase";
import { v4 as uuid } from "uuid";

export const getAllNotificationsApi = async (dispatch, uid) => {
  db.collection("notifications")
    .doc(uid)
    // .limit(10)
    .onSnapshot((doc) => {
      dispatch(getAllNotifications(Object.values(doc.data()) || []));
    });
};

export const addNotificationByIdApi = async (userId, title, content) => {
  console.log(userId);
  db.collection("users")
    .where("_id", "==", userId)
    .get()
    .then((querySnapshot) => {
      querySnapshot.forEach(async (doc) => {
        let user = doc.data();
        let docNotify = await db.collection("notifications").doc(user.uid).get();
        if (docNotify && docNotify.exists) {
          docNotify
          .ref
          .update({
            [uuid()]: {
              title,
              content,
              url: "history",
              img: "",
              date: firebase.firestore.Timestamp.now(),
            },
          });
        } else {
          docNotify
          .ref
          .set({
            [uuid()]: {
              title,
              content,
              url: "history",
              img: "",
              date: firebase.firestore.Timestamp.now(),
            },
          });
        }
        
        return;
      });
    });
};

export const addNotificationApi = async (
  dispatch,
  currentUser,
  currentChat
) => {
  const combinedId =
    currentUser.uid > currentChat.uid
      ? currentUser.uid + currentChat.uid
      : currentChat.uid + currentUser.uid;
  try {
    const res = db
      .collection("chats")
      .doc(combinedId)
      .get()
      .then((doc) => {
        if (!doc.exists) {
          console.log(currentUser, currentChat);
          db.collection("chats").doc(combinedId).set({ messages: [] });
          db.collection("userChats")
            .doc(currentUser.uid)
            .update({
              [combinedId + ".userInfo"]: {
                uid: currentChat.uid,
                displayName: currentChat.displayName,
                photoURL: currentChat.photoURL,
              },
              [combinedId + ".date"]: firebase.firestore.Timestamp.now(),
            });
          db.collection("userChats")
            .doc(currentChat.uid)
            .update({
              [combinedId + ".userInfo"]: {
                uid: currentUser.uid,
                displayName: currentUser.displayName,
                photoURL: currentUser.photoURL,
              },
              [combinedId + ".date"]: firebase.firestore.Timestamp.now(),
            });
          dispatch(addNotification({ userInfo: currentChat }));
        }
      });
  } catch (err) {}
};
