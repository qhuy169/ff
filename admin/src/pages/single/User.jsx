import { useState, useEffect } from "react";
import Sidebar from "~/components/sidebar/Sidebar";
import Navbar from "~/components/navbar/Navbar";
import Datatable from "~/components/datatable/Datatable";
import { useParams } from "react-router-dom";
import { ProductService } from "~/services";
import { userColumns } from "~/datatablesource";
import { useDispatch, useSelector } from "react-redux";
import { getUser, getUsers } from "../../redux/user/userApi";
const UsersView = () => {
    const { userId } = useParams();
    const dispatch = useDispatch();
    console.log(userId);
    useEffect(() => {
        getUser(dispatch, userId);
    }, []);
    let getUserData = useSelector((state) => state.users.user.data);

    return (
        <div className="list">
            <div className="listContainer">
                <Datatable
                    rows={getUserData}
                    title=""
                    productColumns={userColumns}
                    type="user"
                />
            </div>
        </div>
    );
};

export default UsersView;


// import "./single.scss";
// import { useState, useEffect } from "react";
// import { useParams } from "react-router-dom";

// import Sidebar from "../../components/sidebar/Sidebar";
// import Navbar from "../../components/navbar/Navbar";
// import Chart from "../../components/chart/Chart";
// import List from "../../components/table/Table";

// import { UserService } from "~/services";

// const User = () => {
//     const { userId } = useParams();
//     const [data, setData] = useState([]);
//     const [editMode, setEditMode] = useState(false);

//     useEffect(() => {
//         async function getUser() {
//             const res = await UserService.getUser(userId);
//             setData(res.data);
//             console.log(data);
//         }
//         getUser();
//     }, []);

//     const [userName, setUserName] = useState("");
//     const [userGender, setUserGender] = useState("");
//     const [userPhoneNumber, setUserPhoneNumber] = useState("");
//     const [userAddress, setUserAddress] = useState("");

//     const handleEdit = (e) => {
//         e.preventDefault();
//         if (editMode === true) {
//             setEditMode(false);
//         } else setEditMode(true);
//     };
//     const handleSubmitEdit = (e) => {
//         e.preventDefault();
//     };
//     return (
//         <div className="single">
//             <Sidebar />
//             <div className="singleContainer">
//                 <Navbar />
//                 <div className="top">
//                     <div className="left">
//                         <div
//                             className="editButton"
//                             onClick={(e) => handleEdit(e)}
//                         >
//                             Edit
//                         </div>
//                         <h1
//                             className="title"
//                             style={{
//                                 height: "auto",
//                                 fontSize: "2em",
//                                 fontWeight: "bold",
//                                 color: "#555",
//                                 marginBottom: "10px",
//                             }}
//                         >
//                             Information
//                         </h1>
//                         <div className="item">
//                             <form
//                                 id="frmEdit"
//                                 onSubmit={(e) => handleSubmitEdit(e)}
//                             >
//                                 <img
//                                     src="https://icon-library.com/images/anonymous-avatar-icon/anonymous-avatar-icon-25.jpg"
//                                     alt=""
//                                     className="itemImg"
//                                 />
//                                 <div className="details">
//                                     <input
//                                         type="text"
//                                         value={data.map(
//                                             (item) => item.username
//                                         )}
//                                         onChange={(e) =>
//                                             setUserName(e.target.value)
//                                         }
//                                         style={{
//                                             display: editMode
//                                                 ? "block"
//                                                 : "none",
//                                             height: "auto",
//                                             fontSize: "2em",
//                                             fontWeight: "bold",
//                                             color: "#555",
//                                             marginBottom: "10px",
//                                         }}
//                                     ></input>
//                                     <h1
//                                         style={{
//                                             display: !editMode
//                                                 ? "block"
//                                                 : "none",
//                                             fontSize: "2em",
//                                             fontWeight: "bold",
//                                             color: "#555",
//                                             marginBottom: "10px",
//                                         }}
//                                     >
//                                         {data.map((item) => item.username)}
//                                     </h1>
//                                     <div className="detailItem">
//                                         <span className="itemKey">Gender:</span>
//                                         <span
//                                             className="itemValue"
//                                             style={{
//                                                 display: !editMode
//                                                     ? "block"
//                                                     : "none",
//                                             }}
//                                         >
//                                             {data.map((item) => item.sex)}
//                                         </span>
//                                         <select
//                                             id="gender"
//                                             name="genderlist"
//                                             form="frmEdit"
//                                             value={data.map((item) => item.sex)}
//                                             onChange={(e) =>
//                                                 setUserGender(e.target.value)
//                                             }
//                                             style={{
//                                                 display: editMode
//                                                     ? "block"
//                                                     : "none",
//                                                 height: "auto",
//                                                 fontSize: "14px",
//                                             }}
//                                         >
//                                             <option value="Nam">Nam</option>
//                                             <option value="Nữ">Nữ</option>
//                                         </select>
//                                     </div>
//                                     <div className="detailItem">
//                                         <span className="itemKey">Phone:</span>
//                                         <span
//                                             className="itemValue"
//                                             style={{
//                                                 display: !editMode
//                                                     ? "block"
//                                                     : "none",
//                                             }}
//                                         >
//                                             {data.map((item) => item.phone)}
//                                         </span>
//                                         <input
//                                             type="text"
//                                             value={data.map(
//                                                 (item) => item.phone
//                                             )}
//                                             onChange={(e) =>
//                                                 setUserPhoneNumber(
//                                                     e.target.value
//                                                 )
//                                             }
//                                             style={{
//                                                 display: editMode
//                                                     ? "block"
//                                                     : "none",
//                                                 height: "auto",
//                                             }}
//                                         ></input>
//                                     </div>
//                                     <div className="detailItem">
//                                         <span className="itemKey">
//                                             Address:
//                                         </span>
//                                         <input
//                                             type="text"
//                                             value={data.map(
//                                                 (item) =>
//                                                     item.address.homeAdd +
//                                                     ", " +
//                                                     item.address.ward +
//                                                     ", " +
//                                                     item.address.district +
//                                                     ", " +
//                                                     item.address.city
//                                             )}
//                                             onChange={(e) =>
//                                                 setUserAddress(e.target.value)
//                                             }
//                                             style={{
//                                                 display: editMode
//                                                     ? "block"
//                                                     : "none",
//                                                 height: "auto",
//                                                 width: "100%",
//                                             }}
//                                         ></input>
//                                         <span
//                                             className="itemValue"
//                                             style={{
//                                                 display: !editMode
//                                                     ? "block"
//                                                     : "none",
//                                             }}
//                                         >
//                                             {data.map(
//                                                 (item) =>
//                                                     item.address.homeAdd +
//                                                     ", " +
//                                                     item.address.ward +
//                                                     ", " +
//                                                     item.address.district +
//                                                     ", " +
//                                                     item.address.city
//                                             )}
//                                         </span>
//                                     </div>
//                                 </div>
//                                 <button
//                                     id="btnSubmitEdit"
//                                     type="submit"
//                                     className="btnSubmitEdit"
//                                     style={{
//                                         display: editMode ? "block" : "none",
//                                     }}
//                                 >
//                                     Submit
//                                 </button>
//                             </form>
//                         </div>
//                     </div>
//                     <div className="right">
//                         <Chart
//                             aspect={3 / 1}
//                             title="User Spending ( Last 6 Months)"
//                         />
//                     </div>
//                 </div>
//                 <div className="bottom">
//                     <h1 className="title">Last Transactions</h1>
//                     <List />
//                 </div>
//             </div>
//         </div>
//     );
// };

// export default User;


