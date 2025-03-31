import { Link, Navigate, resolvePath } from 'react-router-dom';
import { authService } from '../../services/auth.service';
import { userService } from '../../services/user.service';
import { DEFAULT_STORE, isObjEmpty } from '../../utils';
import { login, logout } from './userSlice';
import firebase, { auth, db, storage } from '../../firebase';
import swal from 'sweetalert';

export const PostLogin = async (dispatch, data, Navigate) => {
    try {
        let res = await authService.postLogin(data);
        console.log(res.data);
        localStorage.setItem(DEFAULT_STORE.TOKEN, JSON.stringify(res.data.accessToken));

        const displayName = res.data?.username || res.data?.fullName;
        const email = data?.email ? data?.email : data?.phone + '@' + 'pxc.com';
        const password = email;
        const avatar = res.data?.avatar || '';
        try {
            await firebase.auth().signInWithEmailAndPassword(email, password);
        } catch (err) {
            await createUserFirebase(res.data);
            await firebase.auth().signInWithEmailAndPassword(email, password);
        }
        
        var userNow = auth.currentUser;
        if (userNow.displayName === null || userNow.photoURL === null) {
            var profile = userNow.displayName === null ? { displayName } : {};
            profile = userNow.photoURL === null ? { ...profile, photoURL: avatar } : profile;
            if (!isObjEmpty(profile)) {
                userNow.updateProfile(profile);
            }
        }

        if (res.data.role === 0) {
            Navigate('/Admin');
        } else if (res.data.role === 1) {
            Navigate('/Seller');
        } else if (res.data.role === 3) {
            Navigate('/shipment');
        } else {
            Navigate('/');
        }
        dispatch(login(res.data));
    } catch (error) {
        console.error(error);
        swal({ text: 'Tài khoản hoặc mật khẩu sai! Vui lòng nhập lại!', icon: 'warning' });
    }
};
export const PostRegister = async (dispatch, data, Navigate) => {
    try {
        let res = await authService.postRegister(data);

        createUserFirebase(res.data);

        localStorage.setItem(DEFAULT_STORE.TOKEN, JSON.stringify(res.data.accessToken));
        if (res.data.role === 2) {
            Navigate('/');
        } else if (res.data.role === 0) {
            Navigate('/Admin');
        } else {
            Navigate('/Seller');
        }
        dispatch(login(res.data));
    } catch (error) {
        console.error(error);
    }
};
export const getUserByAccess = async (dispatch) => {
    try {
        let res = await userService.getUserByAccess();
        if (res.status === 'OK') {
            dispatch(login(res.data));
        } else {
            dispatch(login(null));
            handleLogout();
        }
    } catch (error) {
        console.error(error);
        handleLogout();
    }
};
export const logUserByAccess = async (dispatch) => {
    try {
        dispatch(logout());
    } catch (error) {
        console.error(error);
    }
};

export const handleLogout = () => {
    localStorage.removeItem(DEFAULT_STORE.TOKEN);
    localStorage.removeItem(DEFAULT_STORE.USER_INFO);
};

const createUserFirebase = async (data) => {
    const _id = data?.id;
    const displayName = data?.username;
    const email = data?.email ? data?.email : data?.phone + '@' + 'pxc.com';
    const password = email;
    const avatar = data?.avatar;

    try {
        // const existingUser = await firebase.auth().fetchSignInMethodsForEmail(email).then(async(providers)=>{
        //     if (providers?.length === 0) {
        //Create user
        const res = await firebase.auth().createUserWithEmailAndPassword(email, password);
        //     }
        // })

        var userNow = auth.currentUser;
        userNow.updateProfile({
            displayName,
            photoURL: avatar,
        });

        //create user on firestore
        await db.collection('users').doc(res.user.uid).set({
            uid: res.user.uid,
            _id,
            displayName,
            email,
            photoURL: avatar,
        });

        //create empty user chats on firestore
        await db.collection('userChats').doc(res.user.uid).set({});

        await firebase.auth().signInWithEmailAndPassword(email, password);
    } catch (err) {
        console.error(err);
    }
};
