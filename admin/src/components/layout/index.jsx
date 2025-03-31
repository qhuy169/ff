import { Outlet } from "react-router-dom";
import { LanguageProvider } from "../../context/LanguageContext";
import Navbar from "../navbar/Navbar";
import Sidebar from "../sidebar/Sidebar";
import "./layout.scss";
function Layout() {
  return (
    <LanguageProvider>
      <div className="home">
        <Sidebar />
        <div className="homeContainer">
          <Navbar />
          <Outlet />
        </div>
      </div>
    </LanguageProvider>
  );
}

export default Layout;
