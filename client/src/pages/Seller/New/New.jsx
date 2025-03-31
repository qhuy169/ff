import "./new.scss";
import Sidebar from "../../../Components/seller/sidebar/Sidebar";
import Navbar from "../../../Components/seller/navbar/Navbar";
import DriveFolderUploadOutlinedIcon from "@mui/icons-material/DriveFolderUploadOutlined";
import { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { useNavigate, useParams } from "react-router-dom";

const New = ({  title,action }) => {
  const dispatch= useDispatch();
  const navigate= useNavigate();

  const user = useSelector((state) => state.auth.login?.currentUser)
  const selectedUser = useSelector((state) => state.user.users?.allUsers)
  const [file, setFile] = useState(selectedUser?.image);

  const [username,setUsername]=useState(selectedUser?.username);
  const [password,setPassword] = useState(selectedUser?.password);
  const [email,setEmail]= useState(selectedUser?.email);
  const [phone,setPhone]= useState(selectedUser?.phone);
  const [fullname,setFullname] = useState(selectedUser?.fullname);
  const [role,setRole]=useState(selectedUser?.role);  
  const {id}= useParams();
 
  //Load trang
  useEffect(()=>{
  
    if(user?.accessToken){
      // get1(user?.accessToken,dispatch,id)
     // getAllUsers(user?.accessToken,dispatch)
    }
  // eslint-disable-next-line react-hooks/exhaustive-deps
  },[]) 

  const handleLogin = (e) =>{
    e.preventDefault()
    
    const newUser = {
      username: username,
      password: password,
      email,
      phone,
      fullname,
      role,
      image: file,
    }
    // editUser(newUser,dispatch,navigate,id,user?.accessToken)
  }
  return (
    <div className="new">
      <Sidebar />
      <div className="newContainer">
        <Navbar />
        <div className="top">
          <h1>{title}</h1>
        </div>
        <div className="bottom">
          <div className="left">
            <img
              src={
                file
                  ? URL.createObjectURL(file)
                  : `${selectedUser?.image}`
              }
              alt=""
            />
          </div>
          <div className="right">
            <form onSubmit={handleLogin}>
              <div className="formInput">
                <label htmlFor="file">
                  Image: <DriveFolderUploadOutlinedIcon className="icon" />
                </label>
                <input
                  type="file"
                  id="file"
                  onChange={(e) => setFile(e.target.files[0])}
                  style={{ display: "none" }}
                />
              </div>

                <div className="formInput" >
                  <label>Username</label>
                  <input type='text' placeholder={selectedUser?.username} onChange={(e)=>setUsername(e.target.value)} />
                </div>
                <div className="formInput" >
                  <label>Full Name</label>
                  <input type='text' placeholder={selectedUser?.fullname} onChange={(e)=>setFullname(e.target.value)}/>
                </div>
           
                <div className="formInput" >
                  <label>Password</label>
                  <input type='password' onChange={(e)=>setPassword(e.target.value)} />
                </div>
           
                <div className="formInput" >
                  <label>Email</label>
                  <input type='email' placeholder={selectedUser?.email} onChange={(e)=>setEmail(e.target.value)} />
                </div>
           
                <div className="formInput" >
                  <label>Confirm Password</label>
                  <input type='password'  />
                </div>
           
                <div className="formInput" >
                  <label>Phone</label>
                  <input type='text' placeholder={selectedUser?.phone}  onChange={(e)=>setPhone(e.target.value)}/>
                </div>
                
                <select className="table-group-action-input form-control" onChange={(e)=>setRole(e.target.value)} placeholder={selectedUser?.role} > 
                <option value="3" >Customer</option>
                <option value="2" >Seller</option>
              </select>
           
              <button type='submit'>Send</button>
            </form>
          </div>
        </div>
      </div>
    </div>
  );
};

export default New;
