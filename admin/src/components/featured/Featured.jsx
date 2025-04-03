import "./featured.scss";
import MoreVertIcon from "@mui/icons-material/MoreVert";
import { CircularProgressbar } from "react-circular-progressbar";
import "react-circular-progressbar/dist/styles.css";
import KeyboardArrowDownIcon from "@mui/icons-material/KeyboardArrowDown";
import KeyboardArrowUpOutlinedIcon from "@mui/icons-material/KeyboardArrowUpOutlined";
import { useEffect, useState } from "react";
import axiosInstance from "../../api";

const Featured = () => {
  const [statistics, setStatistics] = useState({
    todayRevenue: 0,
    lastWeekRevenue: 0,
    lastMonthRevenue: 0,
    targetRevenue: 0,
  });
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchStatistics = async () => {
      try {
        const response = await axiosInstance.get("orders/statistics");
        setStatistics(response.data);
      } catch (error) {
        console.error("Lỗi khi lấy số liệu thống kê:", error);
      } finally {
        setLoading(false);
      }
    };

    fetchStatistics();
  }, []);

  const { todayRevenue, lastWeekRevenue, lastMonthRevenue, targetRevenue } = statistics;

  return (
    <div className="featured">
      <div className="top">
        <h1 className="title">Total Revenue</h1>
        <MoreVertIcon fontSize="small" />
      </div>
      <div className="bottom">
        <div className="featuredChart">
          <CircularProgressbar
            value={loading ? 0 : (todayRevenue / targetRevenue) * 100}
            text={loading ? "Loading..." : `${((todayRevenue / targetRevenue) * 100).toFixed(1)}%`}
            strokeWidth={5}
          />
        </div>
        <p className="title">Total sales made today</p>
        <p className="amount">{loading ? "Loading..." : `$${todayRevenue.toLocaleString()}`}</p>
        <p className="desc">
          Previous transactions processing. Last payments may not be included.
        </p>
        <div className="summary">
          <div className="item">
            <div className="itemTitle">Target</div>
            <div className="itemResult positive">
              <KeyboardArrowUpOutlinedIcon fontSize="small" />
              <div className="resultAmount">${targetRevenue.toLocaleString()}</div>
            </div>
          </div>
          <div className="item">
            <div className="itemTitle">Last Week</div>
            <div className="itemResult positive">
              <KeyboardArrowUpOutlinedIcon fontSize="small" />
              <div className="resultAmount">${lastWeekRevenue.toLocaleString()}</div>
            </div>
          </div>
          <div className="item">
            <div className="itemTitle">Last Month</div>
            <div className="itemResult positive">
              <KeyboardArrowUpOutlinedIcon fontSize="small" />
              <div className="resultAmount">${lastMonthRevenue.toLocaleString()}</div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Featured;