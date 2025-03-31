import { Link } from 'react-router-dom';
import SearchInput from './SearchInput';
import CartButton from './CartButton';
import FilterButton from './FilterButton';
import styles from './header.module.scss';
import './header.module.scss';
import { DEFAULT_STORE } from '../../utils';
import { useDispatch, useSelector } from 'react-redux';
import { useState } from 'react';
function Header() {
    const user = useSelector(state=>state.user?.user)
    const getAccess = JSON.parse(localStorage.getItem(DEFAULT_STORE.TOKEN))
    const [singIn,setSingIn]= useState(false)
    const dispatch = useDispatch();
    return (
        <header className={styles.heading}>
            <div className={styles.top}>
                <div className={styles.wrap}>
                    {/* <Link to="/">
                    <div className='select-none text-[13px] w-[60px] px-6 py-2 font-semibold text-green-400 border-r-4 border-green-200 rounded shadow-lg text-center'>Phúc Xi Cúc</div>
                    </Link> */}
                    <FilterButton></FilterButton>
                    <SearchInput />
                    {!getAccess &&(<Link to="/SignIn" className="w-32 text-center" onClick={()=>setSingIn(!singIn)}>
                        Đăng nhập/Đăng kí
                    </Link>)}
                
                     {/* <Link to="/history">
                     Đăng nhập/Đăng kí
                    </Link> */}
                    <Link to="/cart">
                        <CartButton />
                    </Link>
                </div>
            </div>
      
        </header>
    );
}

export default Header;
