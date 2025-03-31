import { useParams, useNavigate } from 'react-router-dom';
import { useEffect, useState } from 'react';
import { useDispatch } from 'react-redux';
import { getOrdersById } from '../../../redux/order/ordersApi';
import { ArrowBarLeft } from 'react-bootstrap-icons';

const OrderDetail = () => {
    const { orderId } = useParams();
    const navigate = useNavigate();
    const dispatch = useDispatch();
    
    const [order, setOrder] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchOrderDetail = async () => {
            try {
                const response = await getOrdersById(dispatch, orderId);
                setOrder(response);
            } catch (error) {
                console.error('Error fetching order details:', error);
            } finally {
                setLoading(false);
            }
        };

        fetchOrderDetail();
    }, [orderId]);

    if (loading) {
        return (
            <div className="flex items-center justify-center h-screen">
                <div className="animate-spin rounded-full h-10 w-10 border-t-4 border-blue-500"></div>
            </div>
        );
    }

    return (
        <div className="max-w-3xl mx-auto mt-8 p-6 bg-white shadow-lg rounded-lg border border-gray-200">
            {/* Nút Quay lại */}
            <button 
                onClick={() => navigate(-1)} 
                className="flex items-center text-gray-600 hover:text-blue-500 mb-4"
            >
                <ArrowBarLeft className="w-5 h-5 mr-2" />
                Quay lại
            </button>

            {/* Tiêu đề */}
            <h2 className="text-2xl font-semibold text-gray-800 mb-4">
                Chi tiết đơn hàng #{order?.id}
            </h2>

            {/* Nội dung đơn hàng */}
            <div className="space-y-4">
                <div className="p-4 bg-gray-100 rounded-lg">
                    <p className="text-lg font-medium text-gray-700">
                        🛍️ Trạng thái: <span className="font-semibold text-blue-600">{order?.status}</span>
                    </p>
                </div>
                <div className="p-4 bg-gray-100 rounded-lg">
                    <p className="text-lg font-medium text-gray-700">
                        💰 Tổng tiền: <span className="font-semibold text-green-600">${order?.total}</span>
                    </p>
                </div>
            </div>

            {/* Thêm thông tin đơn hàng */}
            <div className="mt-6">
                <h3 className="text-lg font-semibold text-gray-800">Sản phẩm trong đơn hàng</h3>
                <ul className="mt-2 space-y-2">
                    {order?.items?.map((item, index) => (
                        <li key={index} className="p-3 bg-white shadow-md rounded-lg border">
                            <div className="flex items-center justify-between">
                                <span className="text-gray-700">{item.productName}</span>
                                <span className="font-semibold text-gray-800">x{item.quantity}</span>
                            </div>
                        </li>
                    ))}
                </ul>
            </div>
        </div>
    );
};

export default OrderDetail;
