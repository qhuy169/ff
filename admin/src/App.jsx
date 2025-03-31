import { BrowserRouter } from "react-router-dom";
import "./style/dark.scss";
import "./App.css";
import { useContext } from "react";
import { DarkModeContext } from "./context/darkModeContext";
import { useUser } from "./context/UserContext";
import { AdminRoutes } from "./routes/route";
function App() {
    const { darkMode } = useContext(DarkModeContext);

    return (
        <div className={darkMode ? "app dark" : "app"}>
            <BrowserRouter>
                <AdminRoutes />
            </BrowserRouter>
        </div>
    );
}

export default App;
