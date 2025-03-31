import "./datatable.scss";
import { DataGrid } from "@mui/x-data-grid";
import { userColumns } from "../../../datatablesource";
import { Link, useNavigate } from "react-router-dom";
import { useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { useEffect } from "react";
import { deleteUser, getAllUsers } from "../../../redux/apiRequest";

const Datatable = () => {
  const dispatch = useDispatch()
  const navigate = useNavigate()

  const user = useSelector((state) => state.auth.login?.currentUser)
 
  const userList = useSelector((state)=> state.user.users?.allUsers)
  const [data, setData] = useState(userList);
 
  useEffect(()=>{
    if(user.role !=='1'){
      navigate('/')
    }
    if(user?.accessToken){
      getAllUsers(user?.accessToken,dispatch)
    }
   

  // eslint-disable-next-line react-hooks/exhaustive-deps
  },[]) 

  const handleDelete = (id) => {
    setData(data.filter((item) => item._id !== id));
    deleteUser(user?.accessToken, dispatch, id)
  };

  const actionColumn = [
    {
      field: "action",
      headerName: "Action",
      width: 200,
      renderCell: (params) => {
        return (
          <div className="cellAction">
            <Link to={`/admin/users/${params.row._id}`}style={{ textDecoration: "none" }}>
              <div className="viewButton">View</div>
            </Link>
            <div
              className="deleteButton"
              onClick={() => handleDelete(params.row._id)}
            >
              Delete
            </div>
            <Link to={`/admin/users/edit/${params.row._id}`}style={{ textDecoration: "none" }}>
            <div
              className="updateButton" >Update</div>
            </Link>
          </div>
        );
      },
    },
  ];
  return (
    <div className="datatable">
      <div className="datatableTitle">
  
        <Link to="/admin/users/new" className="link">
          Add New
        </Link>
      </div>   
        <DataGrid getRowId={(row) => row._id}
        className="datagrid"
        rows={userList}
        columns={userColumns.concat(actionColumn)}
        pageSize={8}
        rowsPerPageOptions={[8]}
        checkboxSelection
          />
    </div>
  );
};

export default Datatable;
