import "./navbar.scss";
import SearchOutlinedIcon from "@mui/icons-material/SearchOutlined";
import LanguageOutlinedIcon from "@mui/icons-material/LanguageOutlined";
import DarkModeOutlinedIcon from "@mui/icons-material/DarkModeOutlined";
import FullscreenExitOutlinedIcon from "@mui/icons-material/FullscreenExitOutlined";
import NotificationsNoneOutlinedIcon from "@mui/icons-material/NotificationsNoneOutlined";
import ChatBubbleOutlineOutlinedIcon from "@mui/icons-material/ChatBubbleOutlineOutlined";
import ListOutlinedIcon from "@mui/icons-material/ListOutlined";
import { DarkModeContext } from "../../context/darkModeContext";
import { useContext } from "react";

const Navbar = () => {
  
  const { dispatch } = useContext(DarkModeContext);
  const getSeller = JSON.parse(localStorage.getItem('customerInfo'))
  return (
    <div className="navbar">
    <div className="wrapper">
     
      <div className="items">
        <div className="item">
          <LanguageOutlinedIcon className="icon" />
          English
        </div>    
        <div className="item">
        <DarkModeOutlinedIcon
          className="icon"
          onClick={() => dispatch({ type: "TOGGLE" })}
        />
      </div>
        <div className="item">
          <NotificationsNoneOutlinedIcon className="icon" />
          <div className="counter">1</div>
        </div>
        <div className="item">
          <ChatBubbleOutlineOutlinedIcon className="icon" />
          <div className="counter">2</div>
        </div>
        <div className="item">
          <ListOutlinedIcon className="icon" />
        </div>
        <div className="item">
          <img
            src={getSeller?.avatar ? getSeller?.avatar : 'https://cdn.tgdd.vn/GameApp/4/242186/Screentshots/tai-avatar-star-online-game-ban-sung-kinh-dien-11-06-2021-1.jpg'}
            alt=""
            className="avatar"
          />
        </div>
      </div>
      <div className="search">
        <input type="text" placeholder="Search..." className="focus:outline-none!importtant" />
      </div>
    </div>
  </div>
  );
};

export default Navbar;
