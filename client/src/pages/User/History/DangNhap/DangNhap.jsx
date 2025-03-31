import clsx from 'clsx';
import { React, useState, useEffect } from 'react';
import firebase from '~/firebase';
import styles from './dangnhap.module.scss';
import { Navigate, useNavigate } from 'react-router-dom';
import { customerService } from '~/services';
import { PostLogin } from '../../../../redux/user/userApi';
import { useDispatch } from 'react-redux';
import {PhoneLandscape} from 'react-bootstrap-icons';
import swal from 'sweetalert';

const DangNhap = () => {
    const [txtPhoneNumber, setTxtPhoneNumber] = useState('');
    const [otp, setOtp] = useState('');
    const [flag, setFlag] = useState(false);
    const [error, setError] = useState('');
    const dispatch = useDispatch()
    const navigate = useNavigate();
    const configureCaptcha = () => {
        window.recaptchaVerifier = new firebase.auth.RecaptchaVerifier('sign-in-button', {
            size: 'invisible',
            callback: (response) => {
                // reCAPTCHA solved, allow signInWithPhoneNumber.
                handleSubmit();
                console.log('Recaptca varified');
            },
            defaultCountry: 'VN',
        });
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        let numberPhone=txtPhoneNumber.split('')
        if(txtPhoneNumber.startsWith('0')){
            numberPhone.shift()
        }else if(txtPhoneNumber.startsWith('84')){
            numberPhone.splice(0,2)
        }
        const phoneNumber = '+84' + numberPhone.join('');
        if (phoneNumber.length < 10 || phoneNumber === '' || phoneNumber === undefined) {
            swal({text: 'Số điện thoại không hợp lệ', icon: 'error',});
        } else {
            setFlag(true);
            configureCaptcha();
            const appVerifier = window.recaptchaVerifier;
            console.log(phoneNumber);

            firebase
                .auth()
                .signInWithPhoneNumber(phoneNumber, appVerifier)
                .then((confirmationResult) => {
                    // SMS sent. Prompt user to type the code from the message, then sign the
                    // user in with confirmationResult.confirm(code).
                    window.confirmationResult = confirmationResult;
                    console.log('OTP has been sent');
                })
                .catch((error) => {
                    console.log('SMS not sent');

                    console.log(error);
                    appVerifier.clear();
                });
        }
    };

    const submitOTP = (e) => {
        e.preventDefault();

        const code = otp;

        window.confirmationResult
            .confirm(code)
            .then((result) => {
                //User signed in successfully.
                PostLogin(dispatch,{phone:txtPhoneNumber,otp:true}, navigate)
                window.location.reload();
                //User signed in successfully.
                // customerService.getCustomerByPhone(txtPhoneNumber).then((res) => {
                //     if (res.length > 0) {
                //         localStorage.setItem('customerInfo', JSON.stringify(res[0]));
                       
                //     }
                // });
                // JSON.parse(localStorage.getItem('customerInfo'))
            })
            .catch((error) => {
                //User couldn't sign in (bad verification code?)
            });
    };

    const changeNum = (e) => {
        e.preventDefault();
        setFlag(false);
    };
    return (
        <section className={styles.login}>
            <div className={clsx('d1', 'step1')} style={{ display: !flag ? 'block' : 'none' }}>
                <div className='h-[200px] w-[200px] rounded-xl bg-333' style={{padding: 0}}><img className='w-full h-full ' src="https://png.pngtree.com/element_our/20190528/ourlarge/pngtree-dial-phone-flat-rectangle-blue-business-card-small-icon-decorative-pattern-image_1185686.jpg"></img></div>
                <span>TRA CỨU THÔNG TIN ĐƠN HÀNG</span>
                <form id="frmGetVerifyCode" onSubmit={(e) => handleSubmit(e)}>
                <div id="sign-in-button" style={{ display: 'none' }}></div>
<h1 className='relative'>
<PhoneLandscape className='absolute top-6 left-[23%]'></PhoneLandscape>
                        <input
                            type="tel"
                            value={txtPhoneNumber}
                            placeholder="Nhập số điện thoại tra cứu"
                            autoComplete="off"
                            maxLength="11"
                            onChange={(e) => setTxtPhoneNumber(e.target.value)}
                            
                        ></input>
</h1>
                    <button type="submit" className={styles.btn} id="submitPhone">
                        Tiếp tục
                    </button>
                </form>
            </div>

            <div className={clsx('d2', 'step2')} style={{ display: flag ? 'block' : 'none' }}>
                <span className="s1">
                    Mã xác nhận đã được gửi đến số điện thoại <b>{txtPhoneNumber}</b>
                </span>
                <form id="frmSubmitVerifyCode" onSubmit={(e) => submitOTP(e)}>
                    <input
                        type="number"
                        maxLength="6"
                        value={otp}
                        placeholder="Nhập mã xác nhận"
                        onChange={(e) => setOtp(e.target.value)}
                    ></input>
                    <button className={styles.btn} id="submitOTP">
                        Tiếp tục
                    </button>
                </form>
                {/* <a className="resend-sms" href="javascript:GetVerifyCode(1)">
                    Tôi không nhận được mã, vui lòng gửi lại
                </a> */}

                <a className={styles.btnChangeNum} onClick={(e) => changeNum(e)}>
                    Thay đổi số điện thoại
                </a>
            </div>
        </section>
    );
};
export default DangNhap;