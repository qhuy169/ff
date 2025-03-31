import { useState, useEffect } from 'react';
import firebase from '../../../firebase';
import { userService } from '~/services';
import { useNavigate } from 'react-router-dom';
const Welcome = () => {
    const user = JSON.parse(localStorage.getItem('customerInfo'));
    const navigate = useNavigate();

    const handleLogOut = () => {
        if (confirm('Bạn có muốn thoát không?')) {
            try {
                firebase
                    .auth()
                    .signOut()
                    .then(() => {
                        console.log('Log out success');
                    })
                    .catch((error) => {
                        console.log(error);
                    });
                localStorage.removeItem('access');
                localStorage.removeItem('customerInfo');
                location.reload();
            } catch (error) {
                console.log(error);
            }
        }
    };

    return (
        <div className="">
            <div className='flex flex-col justify-center items-center
            '>
                <div>
                    <img src={user?.avatar || "https://cdn-icons-png.flaticon.com/512/8345/8345328.png"} alt=""  className='w-24 object-fit-cover'/>
                </div>

                <b id="profileName" className="font-light ">
                    {user?.sex} {user?.username} &nbsp;
                </b>
              
            </div>

        </div>
    );
};
export default Welcome;
