import "./chart.scss";
import {
  AreaChart,
  Area,
  XAxis,
  CartesianGrid,
  Tooltip,
  ResponsiveContainer,
} from "recharts";
import { useEffect, useState } from "react";
import axiosInstance from "../../api";

const Chart = ({ aspect, title }) => {
  const [data, setData] = useState([]); // State để lưu dữ liệu biểu đồ
  const [loading, setLoading] = useState(true); // State để quản lý trạng thái tải dữ liệu

  useEffect(() => {
    const fetchChartData = async () => {
      try {
        const response = await axiosInstance.get("orders/revenue/last-six-months"); // Gọi API
        const formattedData = response.data.data.map((item) => ({
          name: item.month, // Tên tháng
          Total: parseFloat(item.total), // Tổng doanh thu
        }));
        setData(formattedData); // Cập nhật dữ liệu biểu đồ
      } catch (error) {
        console.error("Lỗi khi lấy dữ liệu biểu đồ:", error);
      } finally {
        setLoading(false);
      }
    };

    fetchChartData();
  }, []);

  return (
    <div className="chart">
      <div className="title">{title}</div>
      <ResponsiveContainer width="100%" aspect={aspect}>
        <AreaChart
          width={730}
          height={250}
          data={loading ? [] : data} // Hiển thị dữ liệu khi đã tải xong
          margin={{ top: 10, right: 30, left: 0, bottom: 0 }}
        >
          <defs>
            <linearGradient id="total" x1="0" y1="0" x2="0" y2="1">
              <stop offset="5%" stopColor="#8884d8" stopOpacity={0.8} />
              <stop offset="95%" stopColor="#8884d8" stopOpacity={0} />
            </linearGradient>
          </defs>
          <XAxis dataKey="name" stroke="gray" />
          <CartesianGrid strokeDasharray="3 3" className="chartGrid" />
          <Tooltip />
          <Area
            type="monotone"
            dataKey="Total"
            stroke="#8884d8"
            fillOpacity={1}
            fill="url(#total)"
          />
        </AreaChart>
      </ResponsiveContainer>
    </div>
  );
};

export default Chart;