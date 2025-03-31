import './datatable.scss';
import { DataGrid } from '@mui/x-data-grid';
import { Link, useNavigate } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux';
import { useEffect, useState } from 'react';
import { discountService } from '../../../services';
import swal from 'sweetalert';

const DatatableDiscount = () => {
    const dispatch = useDispatch();
    const navigate = useNavigate();
    const getUser = JSON.parse(localStorage.getItem('customerInfo'));

    // State to store the discount data
    const [discounts, setDiscounts] = useState([]);

    // Fetch discount data for the shop
    useEffect(() => {
        const fetchDiscounts = async () => {
            try {
                const response = await discountService.getDiscountByShopId(getUser?.shopId);
                setDiscounts(response.data); // Assuming response.data contains the discount data
            } catch (error) {
                console.error("Error fetching discounts", error);
            }
        };
        fetchDiscounts();
    }, [getUser?.shopId]);

    const shop = useSelector((state) => state?.shops?.viewShop);

    useEffect(() => {
        if (getUser.role !== 1) {
            navigate('/');
        }

        if (shop?.checkPackage === 1) {
            swal({
                title: 'Notify',
                text: 'Cần mua gói gia hạn trước khi thực hiện giao dịch',
                icon: 'warning',
            });
            navigate('/Seller/package');
        }
    }, [getUser, shop, navigate]);

    // Delete discount
    const handleDelete = (id) => {
        // You can call the delete API here and refresh the data
        discountService.deleteDiscount(id)
            .then(() => {
                setDiscounts(discounts.filter(discount => discount.id !== id)); // Remove the deleted discount from the state
                swal("Success", "Discount deleted successfully", "success");
            })
            .catch((error) => {
                swal("Error", "Failed to delete the discount", "error");
            });
    };

    const actionColumn = [
        {
            field: 'action',
            headerName: 'Action',
            width: 200,
            renderCell: (params) => {
                return (
                    <div className="cellAction">
                        <Link to={`/Seller/discount/edit/${params.row.id}`}>
                            <div className="updateButton">Edit</div>
                        </Link>
                        <div className="deleteButton" onClick={() => handleDelete(params.row.id)}>
                            Delete
                        </div>
                    </div>
                );
            },
        },
    ];

    // Columns for displaying discount details
    const columns = [
        { field: 'code', headerName: 'Discount Code', width: 180 },
        { field: 'name', headerName: 'Discount Name', width: 180 },
        { field: 'percent', headerName: 'Discount Percent', width: 180, renderCell: (params) => `${params.row.percent * 100}%` },
        { field: 'minSpend', headerName: 'Min Spend', width: 150 },
        { field: 'startDate', headerName: 'Start Date', width: 180, renderCell: (params) => new Date(params.row.startDate).toLocaleDateString() },
        { field: 'endDate', headerName: 'End Date', width: 180, renderCell: (params) => new Date(params.row.endDate).toLocaleDateString() },
        ...actionColumn, // Add action column for Edit/Delete
    ];

    return (
        <div className="datatable">
            <div className="datatableTitle">
                <Link to="/seller/discount" className="link">
                    Add New Discount
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
                rows={discounts || []}
                columns={columns}
                pageSize={9}
                rowsPerPageOptions={[9]}
                checkboxSelection
            />
        </div>
    );
};

export default DatatableDiscount;
