import "./list.scss";
import { useState, useEffect } from "react";
import Sidebar from "~/components/sidebar/Sidebar";
import Navbar from "~/components/navbar/Navbar";
import Datatable from "~/components/userdatatable/UserDatatable";
import { useLocation } from "react-router-dom";
import { UserService } from "~/services/user.service";
import { userColumns } from "~/datatablesource";

const ListUser = () => {
    const locationUrl = useLocation();
    console.log(locationUrl.pathname);
    const [data, setData] = useState([]);

    useEffect(() => {
        async function getUsers() {
            const res = await UserService.getUsers().then((res) =>
                setData(res.data)
            );
        }
        getUsers();
    }, []);
    return (
        <div className="list">
            
            <div className="listContainer">
                
                <Datatable
                    rows={data}
                    title=""
                    userColumns={userColumns}
                />
            </div>
        </div>
    );
};

export default ListUser;
