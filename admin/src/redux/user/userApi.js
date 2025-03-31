import { Link, Navigate } from 'react-router-dom';
import { getUserById,removeUserById } from './userSlice';
import { UserService } from '../../services/user.service';
export const getUsers = async (dispatch) => {
    let res = await UserService.getUsers();
    dispatch(getUsers(res.data));
};
export const getUser = async (dispatch,id) => {
    let res = await UserService.getUser(id);
    dispatch(getUserById(res.data));
}
export const removeUser = async (dispatch,id) => {
    await UserService.removeUser(id);
    let res = await UserService.getUser(id);
    dispatch(removeUserById(res.data));
    
};
export const editUser = async (dispatch,id,data) => {
    await UserService.editUser(id,data);
    let res = await UserService.getUser(id);
    dispatch( editUserById(res.data));
    
};


