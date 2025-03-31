import { getAllNotifications, addNotification } from './notificationsSlice.js';
import firebase, { auth, db, storage } from '../../firebase';
import { v4 as uuid } from "uuid";

export const getAllNotificationsApi = async (dispatch, uid) => {
    db.collection('notifications')
        .doc(uid)
        // .limit(10)
        .onSnapshot((doc) => {
            dispatch(getAllNotifications(Object.values(doc.data()) || []));
        });
};

export const addNotificationByIdApi = async (userId, title, content, url) => {
    console.log(userId, title, content);
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
                url,
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
                url,
                img: "",
                date: firebase.firestore.Timestamp.now(),
              },
            });
          }
          
          return;
        });
      });
  };
