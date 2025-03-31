import { useState } from "react";
import clsx from "clsx";
import styles from "./login.module.scss";
import authService from "../../services/auth.service";
import { useUser } from "../../context/UserContext";
import { Navigate } from "react-router-dom";
import Alert from "@mui/material/Alert";
import { async } from "@firebase/util";
const Login = () => {
    const [login, setLogin] = useState(true);
    const { isLoggedIn, setUserState } = useUser();
    const [email, setEmail] = useState("");
    const [redirectToReferrer, setRedirectToReferrer] = useState(false);
    const [password, setPassword] = useState("");
    const handleEmailChange = async (e) => {
        setEmail(e.target.value);
    };
    const handlePasswordChange =  async (e) => {
        setPassword(e.target.value);
    };
    const [alert, setAlert] = useState(false);
    const handleLoginSubmit = async (e) => {
        e.preventDefault();
        try {
            const res = await authService.login(email, password);
            console.log(res);
            if (res) {
                setTimeout(() => {
                    console.log(res);
                    setUserState(res);
                }, 1500);
            }
            else {
                setAlert(true)
            }
        } catch (error) {}
    };
    const handleSignupSubmit = () => {};

    if (redirectToReferrer) {
        return <Navigate to="/" />;
    }
    if (isLoggedIn) {
        return <Navigate to="/" />;
    }
    return (
                
            
            <div className={styles.body}>
                <div className={styles.container}>
                    <div className={styles.backbox}>
                        <div
                            className={clsx(
                                styles.loginMsg,
                                !login && styles.hide
                            )}
                        >
                            {" "}
                            <div className={styles.textcontent}>
                                <p className={styles.title}>
                                    Don't have an account?
                                </p>
                                <p>Sign up to save all your graph.</p>
                                <button
                                    id="switch1"
                                    onClick={() => setLogin(false)}
                                >
                                    Sign Up
                                </button>
                            </div>
                        </div>
                        <div
                            className={clsx(
                                styles.signupMsg,
                                login && styles.hide
                            )}
                        >
                            <div className={styles.textcontent}>
                                <p className={styles.title}>Have an account?</p>
                                <p>Log in to see all your collection.</p>
                                <button
                                    id="switch2"
                                    onClick={() => setLogin(true)}
                                >
                                    LOG IN
                                </button>
                            </div>
                        </div>
                    </div>

                    <div className={styles.frontbox}>
                        {alert && <p className={styles.alert}>Đăng nhập thất bại</p>}
                        <form
                            onSubmit={handleLoginSubmit}
                            className={clsx(
                                styles.login,
                                !login && styles.hide
                            )}
                        >
                            <h2>LOG IN</h2>
                            <div className={styles.inputbox}>
                                <input
                                    type="text"
                                    name="email"
                                    value={email}
                                    onChange={handleEmailChange}
                                    placeholder="email"
                                    tabIndex={1}
                                />
                                <input
                                    type="password"
                                    name="password"
                                    value={password}
                                    onChange={handlePasswordChange}
                                    placeholder="password"
                                    tabIndex={2}
                                />
                            </div>
                            <p>FORGET PASSWORD?</p>
                            <button type="submit">LOG IN</button>
                        </form>

                        <form
                            onSubmit={handleSignupSubmit}
                            className={clsx(
                                styles.signup,
                                login && styles.hide
                            )}
                        >
                            <h2>SIGN UP</h2>
                            <div className={styles.inputbox}>
                                <input
                                    type="text"
                                    name="fullname"
                                    placeholder="fullname"
                                    tabIndex={1}
                                />
                                <input
                                    type="text"
                                    name="email"
                                    placeholder="email"
                                    tabIndex={2}
                                />
                                <input
                                    type="password"
                                    name="password"
                                    placeholder="password"
                                    tabIndex={3}
                                />
                            </div>
                            <button type="submit">SIGN UP</button>
                        </form>
                    </div>
                </div>
            </div>
    );
};

export default Login;
