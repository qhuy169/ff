import './datatable.scss';
import { DataGrid } from '@mui/x-data-grid';
import { productColumns } from '../datablesource/datablesource';
import { Link, useNavigate } from 'react-router-dom';
import { useState, useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { getAllProductApi, deleteProduct } from '../../../redux/product/productsApi';
import swal from 'sweetalert';

const Datatable = () => {
    const dispatch = useDispatch();
    const navigate = useNavigate();
    const getUser = JSON.parse(localStorage.getItem('customerInfo'));
    const shop = useSelector((state) => state?.shops?.viewShop);
    
    const [searchTerm, setSearchTerm] = useState('');
    const [filteredProducts, setFilteredProducts] = useState([]);

    useEffect(() => {
        if (shop?.checkPackage == 1) {
            swal({
                title: 'Notify',
                text: 'Cần mua gói gia hạn trước khi thực hiện giao dịch',
                icon: 'warning',
            });
            navigate('/Seller/package');
        }
        getAllProductApi(dispatch, { shopId: getUser?.shopId });
    }, []);

    const productList = useSelector((state) => state.products?.pageProductShop?.data);
console.log(productList);

    useEffect(() => {
        if (getUser.role !== 1) {
            navigate('/');
        }
    }, []);

    useEffect(() => {
        if (searchTerm) {
            const filtered = productList?.content?.filter((product) =>
                product?.title?.toLowerCase()?.includes(searchTerm.toLowerCase()) // 🛠️ Kiểm tra null
            );
            setFilteredProducts(filtered);
        } else {
            setFilteredProducts(productList?.content || []);
        }
    }, [searchTerm, productList]);
    
    const handleDelete = (id) => {
        deleteProduct(id, dispatch, navigate, productList);
    };

    const actionColumn = [
        {
            field: 'action',
            headerName: 'Action',
            width: 200,
            renderCell: (params) => {
                return (
                    <div className="cellAction">
                        <Link to={`/seller/products/edit/${params.row.id}`}>
                            <div className="updateButton">Edit</div>
                        </Link>
                        <div className="deleteButton" onClick={() => handleDelete(params.row.id)}>
                            Disable
                        </div>
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
                    placeholder="🔍 Tìm kiếm sản phẩm..."
                    value={searchTerm}
                    onChange={(e) => setSearchTerm(e.target.value)}
                    className="p-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-400"
                />
                <Link to="/seller/products/addProduct" className="link">
                    Add New
                </Link>
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
                rows={filteredProducts}
                columns={productColumns.concat(actionColumn)}
                pageSize={9}
                rowsPerPageOptions={[9]}
                checkboxSelection
            />
        </div>
    );
};

export default Datatable;
