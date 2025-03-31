import { useState, useEffect } from 'react';
import styles from './home.module.scss';

import Ticket from './Ticket';


import ProductDeal from './ProductDeal';

import ProductSuggest from './ProductSuggest';
import ProductCategory from './ProductCategory';
import { Sticky } from 'react-bootstrap-icons';
import { useDispatch } from 'react-redux';
import { useNavigate, useSearchParams } from 'react-router-dom';
import { DEFAULT_STORE, parseQueryString } from '../../../utils';
import { getUserByAccess } from '../../../redux/user/userApi';
import swal from 'sweetalert';
import { vnpay } from '../../../services/payment';

function Home({ title = '' }) {
    const [displayTicket, setDisplayTicket] = useState(false);
    const dispatch = useDispatch()
    const [searchParams, setSearchParams] = useSearchParams();
    const navigate = useNavigate();
    // useEffect(() => {

    // }, []);
    useEffect(() => {
        const clearParamByPayment = async () => {
            let status = searchParams.get(DEFAULT_STORE.TRANSACTION_STATUS) || param[DEFAULT_STORE.TRANSACTION_STATUS];
            if (status?.toLowerCase() === 'true') {
                swal({
                    title: 'Thành công',
                    text: 'Giao dịch thành công!',
                    icon: 'success',
                });
            } else {
                swal({
                    title: 'Thất bại',
                    text: 'Giao dịch thất bại, vui lòng kiểm tra lại!',
                    icon: 'error',
                });
            }
        }
        const clearParamByGoogle = async () => {
            let token = searchParams.get(DEFAULT_STORE.TOKEN) || param[DEFAULT_STORE.TOKEN];
            localStorage.setItem(DEFAULT_STORE.TOKEN, JSON.stringify(token));
            getUserByAccess(dispatch, token)
        }

        let param = parseQueryString(window.location.search);
        if (searchParams || param) {
            if (searchParams.get(DEFAULT_STORE.TRANSACTION_STATUS) || param[DEFAULT_STORE.TRANSACTION_STATUS]) {
                clearParamByPayment();
                // window.location.search = '';
            } else if (searchParams.get(DEFAULT_STORE.TOKEN) || param[DEFAULT_STORE.TOKEN]) {
                clearParamByGoogle();
                swal({ text: 'Đăng nhập thành công!', icon: 'success' });
            } else if (searchParams.get('error') || param['error']) {
                swal({text: decodeURI(searchParams.get('error') || param['error']), icon: 'error',})
            }
            history.replaceState({}, document.title, '/');
        }
        document.title = title;
        const handleScroll = (event) => {
            setDisplayTicket(window.scrollY > 500);
        };
        window.addEventListener('scroll', handleScroll);
        return () => {
            window.removeEventListener('scroll', handleScroll);
        };

    }, []);

    return (
        <>
            <div>
                <main className={styles.main}>
                    <ProductCategory />
                    <Ticket show={displayTicket} />
                    <ProductSuggest />
                    <ProductDeal />
                </main>
            </div>
        </>
    );
}
export default Home;
