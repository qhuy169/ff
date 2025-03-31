import { useRef, useState } from 'react';
import { Facebook, Google } from 'react-bootstrap-icons';
import { useDispatch, useSelector } from 'react-redux';
import { Link, useNavigate } from 'react-router-dom';
import { PostRegister } from '../../redux/user/userApi';
import { ERole, FACEBOOK_AUTH_URL, GOOGLE_AUTH_URL } from '../../utils';
import { LocationForm } from './../LocationForm';
import './SignUpForm.scss';

function SingUpForm() {
    const user = useSelector((state) => state.user?.user);
    const [addressOption, setAddressOption] = useState();
    const navigate = useNavigate();
    const ref = useRef();
    const dispatch = useDispatch();
    const [data, setData] = useState({
        firstName: '',
        lastName: '',
        phone: '',
        email: '',
        password: '',
        pwdR: '',
        role: ERole.CUSTOMER.name,
        address: {
            homeAdd: '',
            ward: '',
            district: '',
            city: '',
        },
    });
    const [check, setCheck] = useState(true);
    const handleSubmit = (e) => {
        e.preventDefault();
        let newData = {...data, address: {...data.address, ...addressOption}};
        if (newData.password !== newData.pwdR) {
            ref.current.focus();
            setCheck(false);
        } else {
            // PostLogin(dispatch,data)
            // console.log(user)
            PostRegister(dispatch, newData, navigate);
            console.log(newData);
        }
    };
    const handleChangeOption = (e) => {
        setData((prev) => ({ ...prev, role: e.target.value }));
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
                                <p className="text-center font-semibold mx-4 mb-0">Đăng ký</p>
                            </div>
                            <div className="mb-6">
                                <input
                                    type="text"
                                    className="rounded-xl form-control block w-full px-4 py-2 text-xl font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-300 rounded transition ease-in-out m-0 focus:text-gray-700 focus:bg-white focus:border-blue-600 focus:outline-none"
                                    id="exampleFormControlInput2"
                                    placeholder="Họ"
                                    onChange={(e) => setData({ ...data, lastName: e.target.value })}
                                    required
                                />
                            </div>
                            <div className="mb-6">
                                <input
                                    type="text"
                                    className="rounded-xl form-control block w-full px-4 py-2 text-xl font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-300 rounded transition ease-in-out m-0 focus:text-gray-700 focus:bg-white focus:border-blue-600 focus:outline-none"
                                    id="exampleFormControlInput2"
                                    placeholder="Tên"
                                    onChange={(e) => setData({ ...data, firstName: e.target.value })}
                                    required
                                />
                            </div>
                            <div className="mb-6">
                                <input
                                    type="text"
                                    className="rounded-xl form-control block w-full px-4 py-2 text-xl font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-300 rounded transition ease-in-out m-0 focus:text-gray-700 focus:bg-white focus:border-blue-600 focus:outline-none"
                                    id="exampleFormControlInput2"
                                    placeholder="Số điện thoại"
                                    onChange={(e) => setData({ ...data, phone: e.target.value })}
                                    required
                                />
                            </div>
                            <div className="mb-6">
                                <input
                                    type="text"
                                    className="rounded-xl form-control block w-full px-4 py-2 text-xl font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-300 rounded transition ease-in-out m-0 focus:text-gray-700 focus:bg-white focus:border-blue-600 focus:outline-none"
                                    id="exampleFormControlInput2"
                                    placeholder="Địa chỉ email"
                                    onChange={(e) => setData({ ...data, email: e.target.value })}
                                    required
                                />
                            </div>

                            <div className="mb-6">
                                <input
                                    type="password"
                                    className="rounded-xl form-control block w-full px-4 py-2 text-xl font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-300 rounded transition ease-in-out m-0 focus:text-gray-700 focus:bg-white focus:border-blue-600 focus:outline-none"
                                    id="exampleFormControlInput2"
                                    placeholder="Mật khẩu"
                                    onChange={(e) => setData({ ...data, password: e.target.value })}
                                    required
                                />
                            </div>

                            <div className="mb-6">
                                <input
                                    ref={ref}
                                    type="password"
                                    className="rounded-xl form-control block w-full px-4 py-2 text-xl font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-300 rounded transition ease-in-out m-0 focus:text-gray-700 focus:bg-white focus:border-blue-600 focus:outline-none"
                                    id="exampleFormControlInput2"
                                    placeholder="Nhập lại mật khẩu"
                                    required
                                    onChange={(e) => {
                                        setCheck(true);
                                        setData({ ...data, pwdR: e.target.value });
                                    }}
                                />
                            </div>

                            <input
                                placeholder="Số nhà, tên đường"
                                id="homeAddress"
                                className="rounded-xl form-control block w-full px-4 py-2 text-xl font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-300 rounded transition ease-in-out m-0 focus:text-gray-700 focus:bg-white focus:border-blue-600 focus:outline-none"
                                required
                                onChange={(e) =>
                                    setData((prev) => ({
                                        ...prev,
                                        address: { ...prev.address, homeAdd: e.target.value },
                                    }))
                                }
                            />
                            <LocationForm onChange={setAddressOption} className={'mr-4'}></LocationForm>

                            <div className="my-4">
                                <label className="mr-8">Vai trò</label>
                                <input
                                    id="customer"
                                    type="radio"
                                    name="role"
                                    value={ERole.CUSTOMER.name}
                                    defaultChecked
                                    onClick={(e) => handleChangeOption(e)}
                                />
                                &nbsp;
                                <label htmlFor="customer">{ERole.CUSTOMER.vi}</label>
                                &emsp;
                                <input
                                    id="shipper"
                                    type="radio"
                                    name="role"
                                    value={ERole.SHIPPER.name}
                                    onClick={(e) => handleChangeOption(e)}
                                />
                                &nbsp;
                                <label htmlFor="shipper">{ERole.SHIPPER.vi}</label>
                            </div>
                            {!check && (
                                <div className="flex justify-between items-center mb-6">
                                    <h3 className="text-red-500 text-[14px]"> Mật khẩu lập lại sai </h3>
                                </div>
                            )}

                            <div className="text-center lg:text-left">
                                <button
                                    type="submit"
                                    className="inline-block px-7 py-3 bg-blue-600 text-white font-medium text-sm leading-snug uppercase rounded shadow-md hover:bg-blue-700 hover:shadow-lg focus:bg-blue-700 focus:shadow-lg focus:outline-none focus:ring-0 active:bg-blue-800 active:shadow-lg transition duration-150 ease-in-out"
                                >
                                    Register
                                </button>
                                <p className="text-sm font-semibold mt-2 pt-1 mb-0">
                                    I have an account?
                                    <Link
                                        to="/SignIn"
                                        className="text-red-600 hover:text-red-700 focus:text-red-700 transition duration-200 ease-in-out"
                                    >
                                        Login
                                    </Link>
                                </p>
                            </div>
                            <hr className="my-4" />
                            <div className="flex justify-between">
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
export default SingUpForm;
