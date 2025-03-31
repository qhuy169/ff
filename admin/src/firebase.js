import firebase from 'firebase/app';
import 'firebase/auth';
import 'firebase/storage';
import 'firebase/firestore';



const firebaseConfig = {
  apiKey: "AIzaSyBcGhkz6MUDuGHk5lvOXpmjxYD1DCseY_Q",
  authDomain: "kl-ecom-7309a.firebaseapp.com",
  projectId: "kl-ecom-7309a",
  storageBucket: "kl-ecom-7309a.appspot.com",
  messagingSenderId: "944748962118",
  appId: "1:944748962118:web:2aaf74238993bc85028547",
  measurementId: "G-G739GR1XNQ"
};

// Initialize Firebase
firebase.initializeApp(firebaseConfig);

export default firebase;
export const auth = firebase.auth();
export const storage = firebase.storage();
export const db = firebase.firestore();
