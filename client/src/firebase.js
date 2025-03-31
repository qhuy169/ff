import firebase from 'firebase/app';
import 'firebase/auth';
import 'firebase/storage';
import 'firebase/firestore';

// const firebaseConfig = { 
//   apiKey : "AIzaSyA0hP_QjIjMATISIBSE9odT7C-otuhyVAo" , 
//   authDomain : "kl-ecom.firebaseapp.com" , 
//   projectId : "kl-ecom" , 
//   storageBucket : "kl-ecom.appspot.com" , 
//   messagingSenderId : "66067006814" , 
//   appId : "1:66067006814:web:3bb5c2039627492e3bbbcf" 
// };

const firebaseConfig = {
  apiKey: "AIzaSyDM5NJGINFEyJH2P8eLM6KuPr7Ayb7YOjA",
  authDomain: "appmusic-4ff7c.firebaseapp.com",
  databaseURL: "https://appmusic-4ff7c-default-rtdb.firebaseio.com",
  projectId: "appmusic-4ff7c",
  storageBucket: "appmusic-4ff7c.appspot.com",
  messagingSenderId: "1039680158356",
  appId: "1:1039680158356:web:8ace08319d508aff3c1281",
  measurementId: "G-PTZ25TF51M"
};
// Initialize Firebase
firebase.initializeApp(firebaseConfig);

export default firebase;
export const auth = firebase.auth();
export const storage = firebase.storage();
export const db = firebase.firestore();
