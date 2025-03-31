import "./new.scss";
import Sidebar from "../../components/sidebar/Sidebar";
import Navbar from "../../components/navbar/Navbar";
import DriveFolderUploadOutlinedIcon from "@mui/icons-material/DriveFolderUploadOutlined";
import { useState } from "react";
import { integerPropType } from "@mui/utils";
import { UserService} from "../../services/user.service";
const New = ({ inputs, title }) => {
  const [file, setFile] = useState("");

  const [userName, setUserName] = useState("");
  const [phone, setPhone] = useState();
  const [gender, setGender] = useState();
  const [homeAdd, setHomeAdd] = useState();
  const [ward, setWard] = useState();
  const [district, setDistrict] = useState();
  const [city, setCity] = useState();

  const address  = {
    homeAdd: homeAdd,
    ward: ward,
    district: district,
    city: city
  };
  const dataPost = {
    id: Math.floor(1 + Math.random() * (100000 - 1)),
    username: userName,
    avatar: "https://icon-library.com/images/anonymous-avatar-icon/anonymous-avatar-icon-25.jpg",
    address: address,
    phone: phone,
    sex: gender
  }

  console.log(dataPost);

  const handleSubmitForm = (e) => {
    e.preventDefault();
    try{
      UserService.addUser(dataPost);
      console.log("Add user success");
    } catch(err) {
      console.log(err)
    }
  }
  return (
    <div className="new">
      <div className="newContainer">
        <div className="top">
          <h1>{title}</h1>
        </div>
        <div className="bottom">
          <div className="left">
            <img
              src={
                file
                  ? URL.createObjectURL(file)
                  : "https://icon-library.com/images/no-image-icon/no-image-icon-0.jpg"
              }
              alt=""
            />
          </div>
          <div className="right">
            <form onSubmit={(e) => handleSubmitForm(e)}>
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
              <div>
                <input type="radio" id="male" name="gender" value="Anh" onChange={(e) => setGender(e.target.value)}></input>
                <label style={{ padding: '10px'}}>Male</label>
                <input type="radio" id="female" name="gender" value="Chị" onChange={(e) => setGender(e.target.value)}></input>
                <label style={{ padding: '10px'}}>Female</label>
              </div>

              <div className="formInput">
                <label>Name and surname</label>
                <input type="text" placeholder="Nguyễn Văn A" required onChange={(e) => setUserName(e.target.value)}/>
                <label>Home number and street</label>
                <input type="text" placeholder="1 Võ Văn Ngân" required onChange={(e) => setHomeAdd(e.target.value)}/>
                <label>Ward</label>
                <input type="text" placeholder="Phường Linh Chiểu" required onChange={(e) => setWard(e.target.value)}/>
                <label>District</label>
                <input type="text" placeholder="Quận Thủ Đức" required onChange={(e) => setDistrict(e.target.value)}/>
                <label>City</label>
                <input type="text" placeholder="TP Hồ Chí Minh" required onChange={(e) => setCity(e.target.value)}/>
                <label>Phone</label>
                <input type="tel" placeholder="0123456789" maxLength="10" required onChange={(e) => setPhone(e.target.value)}/>
                <button type="submit">Send</button>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  );
};

export default New;
