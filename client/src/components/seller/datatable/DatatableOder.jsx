import './datatable.scss';
import { DataGrid } from '@mui/x-data-grid';
import { OderColumns } from '../datablesource/datablesource';
import { Link, useNavigate } from 'react-router-dom';
import { useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { useEffect } from 'react';
import { getAllOrdersByShopApi, updateStatusOrderApi } from '../../../redux/orderShop/orderShopsApi';
import { EOrderStatus, EOrderStatusGHN } from '../../../utils';
import { ghn } from '../../../services/shipping';
import swal from 'sweetalert';
import { addNotificationByIdApi } from '../../../redux/notification/notificationsApi';

const Datatable = () => {
    const dispatch = useDispatch();
    const navigate = useNavigate();
    const getUser = JSON.parse(localStorage.getItem('customerInfo'));
    const shop = useSelector((state) => state?.shops?.viewShop);
    const [searchTerm, setSearchTerm] = useState('');
    const [filteredOrders, setFilteredOrders] = useState([]);
    useEffect(() => {
        if(shop?.checkPackage == 1){
            swal({
              title: 'Notify',
              text: 'Cần mua gói gia hạn trước khi thực hiện giao dịch',
              icon: 'warning',
            });
            navigate('/Seller/package')
          }
        getAllOrdersByShopApi(dispatch, getUser.shopId);
    }, []);  
   

    const { content: orderList = [] } = useSelector((state) => state.orderShops.pageOrder.data);
    useEffect(() => {
        if (searchTerm) {
            const filtered = orderList.filter((order) =>
                order?.id?.toString().includes(searchTerm) || // Tìm theo mã đơn hàng
                order?.user?.fullName?.toLowerCase().includes(searchTerm.toLowerCase()) // Tìm theo tên khách hàng
            );
            setFilteredOrders(filtered);
        } else {
            setFilteredOrders(orderList);
        }
    }, [searchTerm, orderList]);
 
    const handleAccept = async (order) => {
        swal({
            text: 'Bạn có xác nhận đơn hàng và tạo đơn hàng shipper không',
            icon: 'info',
            buttons: {
                cancel: true,
                confirm: true,
            },
        }).then(async (isOK) => {
            if (isOK) {
                let res = await ghn.createOrderGHN(order);
                console.log('res', res?.data?.data);
                let data = res.data?.data;

                let resDetails = await ghn.getOrderDetailGHN(data.order_code);

                const dataResponse = {
                    status: EOrderStatusGHN.getIndexFromName(resDetails?.data?.data.status),
                    shipOrderCode: data?.order_code,
                    expectedDeliveryTime: data?.expected_delivery_time,
                    transportFee: data?.total_fee,
                };
                updateStatusOrderApi(dispatch, order.id, dataResponse);

                swal({
                    title: 'Thành công',
                    text: 'Xác nhận đơn hàng thành công',
                    icon: 'success',
                });
                addNotificationByIdApi(
                    order.user.id,
                    `Đơn hàng với mã ${order.id} đã được cập nhật trạng thái`,
                    `Vào lúc ${Date.now()}, đơn hàng với mã ${
                        order.id
                    } đã được cập nhật trạng thái thành "Đã xác nhận" và tạo đơn hàng vận chuyển thành công!`,
                    'history',
                );
            }
        });
    };

    const handleCancel = async (order) => {
        swal({
            text: 'Bạn có muốn hủy đơn này không',
            icon: 'info',
            buttons: {
                cancel: true,
                confirm: true,
            },
            dangerMode: true,
        }).then(async (isOK) => {
            if (isOK) {
                //let reason = prompt('Nhập lý do hủy đơn hàng này', 'Không đủ hàng');
                swal({
                    text: 'Nhập lý do hủy đơn hàng',
                    content: {
                        element: 'input',
                        attributes: {
                            defaultValue: 'Không đủ hàng',
                        },
                    },
                }).then(async (reason) => {
                    if (reason !== null) {
                        let data = {
                            status: EOrderStatus.ORDER_CANCELLED.index,
                            log: reason,
                            shipOrderCode: null,
                            expectedDeliveryTime: null,
                        };
                        if (order?.shipOrderCode) {
                            let res = await ghn.cancelOrderGHN(order.shipOrderCode);
                            updateStatusOrderApi(dispatch, order.id, data);
                            swal({
                                title: 'Thành công',
                                text: 'Hủy đơn hàng thành công',
                                icon: 'success',
                            });
                            addNotificationByIdApi(
                                order.user.id,
                                `Đơn hàng với mã ${order.id} đã được cập nhật trạng thái`,
                                `Vào lúc ${Date.now()}, đơn hàng với mã ${
                                    order.id
                                } đã bị hủy bởi shop với lý do "${reason}"!`,
                                'history',
                            );
                        } else {
                            updateStatusOrderApi(dispatch, order.id, data);
                            swal({
                                title: 'Thành công',
                                text: 'Hủy đơn hàng thành công',
                                icon: 'success',
                            });
                            addNotificationByIdApi(
                                order.user.id,
                                `Đơn hàng với mã ${order.id} đã được cập nhật trạng thái`,
                                `Vào lúc ${Date.now()}, đơn hàng với mã ${
                                    order.id
                                } đã bị hủy bởi shop với lý do "${reason}"!`,
                                'history',
                            );
                        }
                    }
                });
            }
        });
    };

    const actionColumn = [
        {
            field: 'actionStatus',
            headerName: 'Action Update Status',
            width: 200,
            renderCell: (params) => {
                return (
                    <div className="cellAction text-[12px]">
                        {params.row.status === EOrderStatus.ORDER_SHIPPING.index && (
                            <div className="updateButton" onClick={() => handleAccept(params.row)}>
                                Accept
                            </div>
                        )}
                        {params.row.status !== EOrderStatus.ORDER_CANCELLED.index && (
                            <div className="deleteButton" onClick={() => handleCancel(params.row)}>
                                Cancel
                            </div>
                        )}
                    </div>
                );
            },
        },
        {
            field: 'action',
            headerName: 'Action',
            width: 200,
            renderCell: (params) => {
                return (
                    <div className="cellAction text-[12px]">
                        <Link to={`/seller/orders/${params.row._id}`}>
                            <div className="updateButton">Detail</div>
                        </Link>
                    </div>
                );
            },
        },
    ];
    return (
        <div className="datatable">
            <div className="datatableTitle">
            <input
                    type="text"
                    placeholder="Tìm kiếm theo mã đơn hoặc khách hàng..."
                    className="searchInput"
                    value={searchTerm}
                    onChange={(e) => setSearchTerm(e.target.value)}
                />
            </div>
            <DataGrid
                sx={{
                    typography: {
                        fontSize: 12,
                        '& .MuiTablePagination-displayedRows': {
                            fontSize: 12,
                        },
                    },
                }}
                getRowId={(row) => row.id}
                className="datagrid"
                rows={filteredOrders}
                columns={OderColumns.concat(actionColumn)}
                pageSize={8}
                rowsPerPageOptions={[8]}
                checkboxSelection
            />
        </div>
    );
};

export default Datatable;  