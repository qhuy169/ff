import { useLanguage } from "./context/LanguageContext";
import { EProductStatus } from "./utils";

export const productColumns = () => {
  const language = useLanguage();
  return [
  {
    field: "id",
    headerName: `${language["field.id"]}`,
    width: 50,
    renderCell: (params) => {
      return <div className="cellWithImg">{params.row.id}</div>;
    },
  },
  {
    field: "product",
    headerName: `${language["table.product"]}`,
    width: 700,
    renderCell: (params) => {
      return (
        <div className="cellWithImg">
          <img className="cellImg" src={params.row.img} alt="avatar" />
          {params.row.title}
        </div>
      );
    },
  },
  {
    field: "price",
    headerName: `${language["field.originPrice"]}`,
    width: 100,
    renderCell: (params) => {
      return <div className="cellWithImg">{params.row.originPrice}</div>;
    },
  },

  {
    field: "category",
    headerName: `${language["table.category"]}`,
    width: 160,
    renderCell: (params) => {
      return <div className="cellWithImg">{params.row.category}</div>;
    },
  },
  {
    field: "brand",
    headerName: `${language["table.brand"]}`,
    width: 100,
    renderCell: (params) => {
      return <div className={`cellWithStatus`}>{params.row.brand}</div>;
    },
  },
  {
    field: "shop",
    headerName: `${language["table.shop"]}`,
    width: 100,
    renderCell: (params) => {
      return <div className={`cellWithStatus`}>{params.row.shop}</div>;
    },
  },
  {
    field: "availableQuantity",
    headerName: `${language["field.availableQuantity"]}`,
    width: 150,
    renderCell: (params) => {
      return (
        <div className={`cellWithStatus`}>{params.row.availableQuantity}</div>
      );
    },
  },
  // {
  //   field: "seller",
  //   headerName: "Seller",
  //   width: 200,
  //   renderCell: (params) => {
  //     return (
  //       <div className="cellWithImg">

  //         {params.row.shop.name}
  //       </div>
  //     );
  //   },
  // },

  {
    field: "status",
    headerName: `${language["field.status"]}`,
    width: 150,
    renderCell: (params) => {
      return (
        <div className={`cellWithStatus`}>
          {EProductStatus.getNameFromIndex(params.row?.status)}
        </div>
      );
    },
  },
];
}

export const commentColumns = [
  { field: "id", headerName: "IdComment", width: 70 },

  {
    field: "creator",
    headerName: "Username",
    width: 150,
    valueGetter: (params) => {
      let result = [];
      if (params.row.creator) {
        if (params.row.creator.username) {
          result.push(params.row.creator.username);
        }
      } else {
        result = ["Unknown"];
      }
      return result.join(", ");
    },
  },
  {
    field: "content",
    headerName: "Content",
    width: 230,
  },

  {
    field: "create_date",
    headerName: "Date",
    width: 100,
  },
  {
    field: "replyforId",
    headerName: "IdParent",
    width: 130,
    valueGetter: (params) => {
      let result = [];
      if (params.row.creator) {
        if (params.row.creator.replyforId) {
          result.push(params.row.creator.replyforId);
        }
      } else {
        result = ["Unknown"];
      }
      return result.join(", ");
    },
  },
  ,
  {
    field: "productId",
    headerName: "Product",
    width: 100,
  },
];

export const userColumns = [
  { field: "id", headerName: "ID", width: 150 },
  {
    field: "avatar",
    headerName: "Avatar",
    width: 230,
    renderCell: (params) => {
      return (
        <div className="cellWithImg">
          <img className="cellImg" src={params.row.avatar} alt="avatar" />
        </div>
      );
    },
  },
  {
    field: "username",
    headerName: "Name",
    width: 230,
    renderCell: (params) => {
      return <div className="cellWithImg">{params.row.username}</div>;
    },
  },
  {
    field: "sex",
    headerName: "Gender",
    width: 100,
  },
  {
    field: "address",
    headerName: "Address",
    width: 500,
    valueGetter: (params) => {
      let result = [];
      if (params.row.address) {
        result.push(
          params.row.address.homeAdd +
            ", " +
            params.row.address.ward +
            ", " +
            params.row.address.district +
            ", " +
            params.row.address.city
        );
      } else {
        result = ["Unknown"];
      }
      return result.join(", ");
    },
  },
];

export const reviewProductColumns = [
  {
    field: "id",
    headerName: "ID",
    width: 50,
    renderCell: (params) => {
      return <div className="cellWithImg">{params.row.id}</div>;
    },
  },
  {
    field: "product",
    headerName: "Product",
    width: 550,
    renderCell: (params) => {
      return (
        <div className="cellWithImg">
          <img className="cellImg" src={params.row.img} alt="avatar" />
          {params.row.title}
        </div>
      );
    },
  },

  {
    field: "star",
    headerName: "Stars",
    width: 80,
    renderCell: (params) => {
      return <div className="cellWithImg">{params.row.star}</div>;
    },
  },
  {
    field: "totalVote",
    headerName: "Total Vote",
    width: 90,
    renderCell: (params) => {
      return <div className="cellWithImg">{params.row.totalVote}</div>;
    },
  },
];

export const reviewDetailColumns = [
  {
    field: "id",
    headerName: "ID",
    width: 400,
    renderCell: (params) => {
      return <div className="cellWithImg">{params.row.id}</div>;
    },
  },

  {
    field: "user",
    headerName: "User",
    width: 100,
    renderCell: (params) => {
      return <div className="cellWithImg">{params.row.user.username}</div>;
    },
  },

  {
    field: "star",
    headerName: "Stars",
    width: 80,
    renderCell: (params) => {
      return <div className="cellWithImg">{params.row.star}</div>;
    },
  },
  {
    field: "content",
    headerName: "Content",
    width: 230,
  },
];
