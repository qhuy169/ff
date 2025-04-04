import { useState } from 'react';
import { PostLogin } from '../../redux/user/userApi';
import { useSelector, useDispatch } from 'react-redux';
import { useNavigate } from 'react-router-dom';
import { FACEBOOK_AUTH_URL, GOOGLE_AUTH_URL } from '../../utils';
import { Facebook, Google } from 'react-bootstrap-icons';
import { Link } from 'react-router-dom'
import './SignInForm.scss';

function SingInForm() {
    const navigate = useNavigate();
    const dispatch = useDispatch();
    const [data, setData] = useState({
        phone: '',
        email: '',
        password: '',
        otp: false,
    });

    const handleSubmit = (e) => {
        e.preventDefault();
        PostLogin(dispatch, data, navigate);
    };
    return (
        <section className="h-screen">
            <div className="px-6 h-full text-gray-800">
                <div className="flex xl:justify-center lg:justify-between justify-center items-center flex-wrap h-full g-6">
                    <div className="grow-0 shrink-1 md:shrink-0 basis-auto xl:w-6/12 lg:w-6/12 md:w-9/12 mb-12 md:mb-0">
                        <img
                            src="https://mdbcdn.b-cdn.net/img/Photos/new-templates/bootstrap-login-form/draw2.webp"
                            className="w-full"
                            alt="Sample image"
                        />
                    </div>
                    <div className="xl:ml-20 xl:w-5/12 lg:w-5/12 md:w-8/12 mb-12 md:mb-0">
                        <form onSubmit={handleSubmit}>
                        

                            <div className="flex items-center my-4 before:flex-1 before:border-t before:border-gray-300 before:mt-0.5 after:flex-1 after:border-t after:border-gray-300 after:mt-0.5">
                                <p className="text-center font-semibold mx-4 mb-0">Đăng nhập</p>
                            </div>

                            <div className="mb-6">
                                <input
                                    type="text"
                                    className="form-control block w-full px-4 py-2 text-xl font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-300 rounded-xl transition ease-in-out m-0 focus:text-gray-700 focus:bg-white focus:border-blue-600 focus:outline-none"
                                    id="exampleFormControlInput2"
                                    placeholder="Số điện thoại"
                                    onChange={(e) => setData({ ...data, phone: e.target.value })}
                                />
                            </div>
                            <div className="mb-6">
                                <input
                                    type="email"
                                    className="rounded-xl form-control block w-full px-4 py-2 text-xl font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-300 rounded-xltransition ease-in-out m-0 focus:text-gray-700 focus:bg-white focus:border-blue-600 focus:outline-none"
                                    id="exampleFormControlInput2"
                                    placeholder="Địa chỉ email"
                                    onChange={(e) => setData({ ...data, email: e.target.value })}
                                />
                            </div>

                            <div className="mb-6">
                                <input
                                    type="password"
                                    className="form-control block w-full px-4 py-2 text-xl font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-300 rounded-xl transition ease-in-out m-0 focus:text-gray-700 focus:bg-white focus:border-blue-600 focus:outline-none"
                                    id="exampleFormControlInput2"
                                    placeholder="Mật khẩu"
                                    onChange={(e) => setData({ ...data, password: e.target.value })}
                                />
                            </div>

                            <div className="flex justify-between items-center mb-6">
                                <div className="form-group form-check">
                                    <input
                                        type="checkbox"
                                        className="form-check-input appearance-none h-4 w-4 border border-gray-300 rounded-sm bg-white checked:bg-blue-600 checked:border-blue-600 focus:outline-none transition duration-200 mt-1 align-top bg-no-repeat bg-center bg-contain float-left mr-2 cursor-pointer"
                                        id="exampleCheck2"
                                    />
                                    <label
                                        className="form-check-label inline-block text-gray-800"
                                        htmlFor="exampleCheck2"
                                    >
                                        Remember me
                                    </label>
                                </div>
                                <a href="#!" className="text-gray-800">
                                    Forgot password?
                                </a>
                            </div>

                            <div className="text-center lg:text-left">
                                <button
                                    type="submit"
                                    className="inline-block px-7 py-3 bg-blue-600 text-white font-medium text-sm leading-snug uppercase rounded shadow-md hover:bg-blue-700 hover:shadow-lg focus:bg-blue-700 focus:shadow-lg focus:outline-none focus:ring-0 active:bg-blue-800 active:shadow-lg transition duration-150 ease-in-out"
                                >
                                    Login
                                </button>
                                <p className="text-sm font-semibold mt-2 pt-1 mb-0">
                                    Don't have an account?
                                    <Link
                                        to="/SignUp"
                                        className="text-red-600 hover:text-red-700 focus:text-red-700 transition duration-200 ease-in-out"
                                    >
                                        Register
                                    </Link>
                                </p>
                            </div>
                            <hr className="my-4" />
                            <div className="flex justify-start">
                                <a
                                    href={GOOGLE_AUTH_URL}
                                    className="btn btn-google text-uppercase rounded-lg mr-12 min-w-[250px]"
                                    type="submit"
                                >
                                    <Google className="mr-2" /> Đăng nhập bằng Google
                                </a>
                                <a
                                    href={FACEBOOK_AUTH_URL}
                                    className="btn btn-facebook text-uppercase rounded-lg ml-12 min-w-[250px]"
                                    type="submit"
                                >
                                    <Facebook className="mr-2" /> Đăng nhập bằng Facebook
                                </a>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </section>
    );
}

export default SingInForm;
