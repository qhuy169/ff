import { EProductStatus, EPayment, EShippingMethod, EGender, EOrderStatus } from '../../../utils';

export const feedbackColumns = [
    {
        field: 'id',
        headerName: 'ID',
        width: 50,
        renderCell: (params) => {
            return <div className="cellWithImg">{params.row.id}</div>;
        },
    },
    {
        field: 'content',
        headerName: 'Content',
        width: 500,
        renderCell: (params) => {
            return <div className="cellWithImg">{params.row.content}</div>;
        },
    },
    {
        field: 'author',
        headerName: 'Author',
        width: 150,
        renderCell: (params) => {
            return <div className="cellWithImg">{params.row.author.fullName}</div>;
        },
    },
    {
        field: 'productId',
        headerName: 'Product Id',
        width: 100,
        renderCell: (params) => {
            return <div className="cellWithImg">{params.row.productId}</div>;
        },
    },
    {
        field: 'star',
        headerName: 'Star',
        width: 50,
        renderCell: (params) => {
            return <div className="cellWithImg">{params.row.star}</div>;
        },
    },
    {
        field: 'timeDistance',
        headerName: 'Time Distance',
        width: 150,
        renderCell: (params) => {
            return <div className="cellWithImg">{params.row.timeDistance}</div>;
        },
    },
    {
        field: 'updated',
        headerName: 'Updated',
        width: 100,
        renderCell: (params) => {
            return <div className="cellWithImg">{params.row.updated ? 'Mới' : 'Đã cập nhật'}</div>;
        },
    },
    {
        field: 'createdAt',
        headerName: 'Create dAt',
        width: 150,
        renderCell: (params) => {
            return <div className="cellWithImg">{params.row.createdAt}</div>;
        },
    },
];


export const OderColumns = [
    {
        field: 'id',
        headerName: 'ID',
        width: 50,
        renderCell: (params) => {
            return <div className="cellWithImg">{params.row.id}</div>;
        },
    },
    // {

    //   width: 215  ,
    //   renderCell: (params) => {
    //     return (
    //       <div className="cellWithImg">
    //         {params.row.buy_date}
    //       </div>
    //     );
    //   },
    // },

    {
        field: 'customer',
        headerName: 'Customer',
        width: 200,
        renderCell: (params) => {
            return <div className="cellWithImg">{params.row.user.username}</div>;
        },
    },
    {
        field: 'gender',
        headerName: 'gender',
        width: 150,
        renderCell: (params) => {
            return (
                <div className={`cellWithStatus `}>{EGender.getNameFromIndex(params.row.gender)}</div>
            );
        },
    },
    {
        field: 'phone',
        headerName: 'phone',
        width: 100,
        renderCell: (params) => {
            return <div className={`cellWithStatus`}>{params.row.phone}</div>;
        },
    },
    {
        field: 'email',
        headerName: 'email',
        width: 200,
        renderCell: (params) => {
            return <div className={`cellWithStatus`}>{params.row.email}</div>;
        },
    },
    {
        field: 'address',
        headerName: 'Address',
        width: 450,
        renderCell: (params) => {
            return (
                <div className={`cellWithStatus`}>
                    {`${params.row.address.homeAdd}, ${params.row.address.ward}, ${params.row.address.district}, ${params.row.address.city}`}
                </div>
            );
        },
    },

    {
        field: 'totalPrice',
        headerName: 'total price',
        width: 120,
        renderCell: (params) => {
            return <div className={`cellWithStatus`}>{params.row.totalPrice}</div>;
        },
    },

    {
        field: 'payment',
        headerName: 'Payment',
        width: 130,
        renderCell: (params) => {
            return (
                <div className={`cellWithStatus`}>{EPayment.getNameFromIndex(params.row.payment)}</div>
            );
        },
    },
    {
      field: 'shippingMethod',
      headerName: 'shipping method',
      width: 130,
      renderCell: (params) => {
          return (
              <div className={`cellWithStatus`}>{EShippingMethod.getNameFromIndex(params.row.shippingMethod)}</div>
          );
      },
  },
  {
      field: 'createdAt',
      headerName: 'Create dAt',
      width: 150,
      renderCell: (params) => {
          return <div className="cellWithImg">{params.row.createdAt}</div>;
      },
  },
    {
        field: 'status',
        headerName: 'Status',
        width: 160,
        renderCell: (params) => {
            return <div className={`cellWithStatus`}>{EOrderStatus.getNameFromIndex(params.row.status)}</div>;
        },
    },
];
export const productColumns = [
    {
        field: 'id',
        headerName: 'ID',
        width: 50,
        renderCell: (params) => {
            return <div className="cellWithImg">{params.row.id}</div>;
        },
    },
    {
        field: 'product',
        headerName: 'Product',
        width: 250,
        renderCell: (params) => {
            return (
                <div className="cellWithImg">
                    <img className="cellImg2" src={params.row.img} alt="avatar" />
                    {params.row.title}
                </div>
            );
        },
    },
    {
        field: 'price',
        headerName: 'Price',
        width: 100,
        renderCell: (params) => {
            return <div className="cellWithImg">{params.row.price}</div>;
        },
    },

    {
        field: 'category',
        headerName: 'Category',
        width: 120,
        renderCell: (params) => {
            return <div className="cellWithImg">{params.row.category}</div>;
        },
    },
    {
        field: 'brand',
        headerName: 'Brand',
        width: 100,
        renderCell: (params) => {
            return <div className={`cellWithStatus`}>{params.row.brand}</div>;
        },
    },
    {
        field: 'amount',
        headerName: 'Amount',
        width: 80,
        renderCell: (params) => {
            return <div className={`cellWithStatus`}>{params.row.availableQuantity}</div>;
        },
    },

    {
        field: 'status',
        headerName: 'Status',
        width: 150,
        renderCell: (params) => {
            return (
                <div className={`cellWithStatus`}>
                    {EProductStatus.getNameFromIndex(params.row?.status) ||
                        EProductStatus.PRODUCT_TRADING.name}
                </div>
            );
        },
    },
];

export const orderShipmentColumns = [
    {
        field: 'id',
        headerName: 'ID',
        width: 50,
        renderCell: (params) => {
            return <div className="cellWithImg">{params.row.id}</div>;
        },
    },
    {
        field: 'product',
        headerName: 'Product',
        width: 250,
        renderCell: (params) => {
            return (
                <div className="cellWithImg">
                    <img className="cellImg2" src={params.row.img} alt="avatar" />
                    {params.row.title}
                </div>
            );
        },
    },
    {
        field: 'price',
        headerName: 'Price',
        width: 100,
        renderCell: (params) => {
            return <div className="cellWithImg">{params.row.price}</div>;
        },
    },

    {
        field: 'category',
        headerName: 'Category',
        width: 120,
        renderCell: (params) => {
            return <div className="cellWithImg">{params.row.category}</div>;
        },
    },
    {
        field: 'brand',
        headerName: 'Brand',
        width: 100,
        renderCell: (params) => {
            return <div className={`cellWithStatus`}>{params.row.brand}</div>;
        },
    },
    {
        field: 'amount',
        headerName: 'Amount',
        width: 80,
        renderCell: (params) => {
            return <div className={`cellWithStatus`}>{params.row.availableQuantity}</div>;
        },
    },
    {
        field: 'status',
        headerName: 'Status',
        width: 100,
        renderCell: (params) => {
            return (
                <div className={`cellWithStatus`}>
                    {EProductStatus.getNameFromIndex(params.row?.status) ||
                        EProductStatus.PRODUCT_TRADING.name}
                </div>
            );
        },
    },
];