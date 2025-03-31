import './home.scss';
import Widget from '../../../components/seller/widget/Widget';
import Chart from '../../../components/seller/chart/Chart';
import Featured from '../../../components/seller/featured/Featured';
import Table from '../../../components/seller/table/Table';
import swal from 'sweetalert';
import { useEffect, useState } from 'react';
import { Navigate, useNavigate } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux';
import { getAllOrdersByShopApi } from '../../../redux/orderShop/orderShopsApi';
import { shopService } from '../../../services';
import { getByShopId } from '../../../redux/shop/shopApi';
import { getStatisticOrdersApi } from '../../../redux/statistic/statisticsApi';
const HomeSeller = () => {
    const navigate = useNavigate();
    const dispatch = useDispatch();

    const getUser = JSON.parse(localStorage.getItem('customerInfo'));
    useEffect(() => {
        getAllOrdersByShopApi(dispatch, getUser.shopId, { limit: 5, sortField: 'createdAt', sortDir: 'dec' });
        getByShopId(dispatch, getUser.shopId);
        getStatisticOrdersApi(dispatch, getUser.shopId, { startDate: '2021-01-01', endDate: '2024-01-01' });
    }, []);

    // const user = useSelector((state) => state?.shops?.viewShop);
    // if(user?.shopPrice == null){
    //   swal({
    //     title: 'Notify',
    //     text: 'Cần mua gói gia hạn trước khi thực hiện giao dịch',
    //     icon: 'warning',
    //   });
    //   navigate('/Seller/package')
    // }
    const [startDate, setStartDate] = useState('2021-01-01');
    const [endDate, setEndDate] = useState(new Date().toISOString().split('T')[0]);
    const handleStartDate = (e) => {
        setStartDate(e.target.value);
    };
    const handleEndDate = (e) => {
        setEndDate(e.target.value);
    };
    useEffect(()=> {
      getStatisticOrdersApi(dispatch, getUser.shopId, { startDate, endDate});
    }, [startDate, endDate])
    const statisticOrder = useSelector((state) => state.statistics.statisticOrder.data);

    return (
        <div>
            <div className="widgets">
                <Widget type="order" value={statisticOrder.orderCount} />
                <Widget type="earning" value={statisticOrder.earningTotal} />
                {/* <Widget type="balance" /> */}
            </div>
            <div className="charts">
                <Featured statisticOrder={statisticOrder} />
                <Chart
                    title="Last 6 Months (Revenue)"
                    aspect={2 / 1}
                    groupOrderByDateList={statisticOrder?.groupOrderByDateList}
                    handleStartDate={handleStartDate}
                    handleEndDate={handleEndDate}
                    startDate={startDate}
                    endDate={endDate}
                />
            </div>
            <div className="listContainer">
                <div className="listTitle">Latest Transactions</div>
                <Table />
            </div>
        </div>
    );
};

export default HomeSeller;
