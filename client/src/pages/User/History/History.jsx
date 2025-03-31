import { useEffect } from 'react';
import PurchaseHistory from './PurchaseHistory';
import DangNhap from './DangNhap';
import { useDispatch } from 'react-redux';
import { getHistoryOrderbyUserIds } from '~/redux/history/historyOrdersApi';
import Info from './Info';
const History = () => {
    let customer = JSON.parse(localStorage.getItem('customerInfo'));

   
    const dispatch = useDispatch();
    useEffect(() => {
        if(customer){
            let userId = customer.id;
            getHistoryOrderbyUserIds(dispatch, userId);
        }   
    }, []);

    return <div>{customer ? <Info /> : <DangNhap />}</div>;
};
export default History;
