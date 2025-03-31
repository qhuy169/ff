import { useState } from 'react';
import OrderTable from './OrderTable';
import Welcome from './Welcome';
import { Link } from 'react-router-dom';
import { useSelector } from 'react-redux';
import clsx from 'clsx';
import { CartXFill } from 'react-bootstrap-icons';

const EmptyOrder = () => {
    return (
        <div className="w-full flex flex-col items-center justify-center my-16">
            <CartXFill className='text-9xl text-red-600'></CartXFill>

            <p className='my-8'>Bạn chưa có đơn hàng nào</p>
       
        </div>
    );
};
const PurchaseHistory = () => {
    const loadHistoryOrder = useSelector((state) => state.historyOrders.historyOrder.data);
    const check = loadHistoryOrder.length === 0;
    return <div>{check ? <EmptyOrder /> : <OrderTable data={loadHistoryOrder} />}</div>;
};
export default PurchaseHistory;
