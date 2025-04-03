import "./table.scss";
import Table from "@mui/material/Table";
import TableBody from "@mui/material/TableBody";
import TableCell from "@mui/material/TableCell";
import TableContainer from "@mui/material/TableContainer";
import TableHead from "@mui/material/TableHead";
import TableRow from "@mui/material/TableRow";
import Paper from "@mui/material/Paper";
import { useEffect, useState } from "react";
import axiosInstance from "../../api";

const List = () => {
  const [rows, setRows] = useState([]); // State để lưu dữ liệu bảng
  const [loading, setLoading] = useState(true); // State để quản lý trạng thái tải dữ liệu

  useEffect(() => {
    const fetchRecentOrders = async () => {
      try {
        const response = await axiosInstance.get("orders/recent-orders"); // Gọi API
        setRows(response.data); // Cập nhật dữ liệu bảng
      } catch (error) {
        console.error("Lỗi khi lấy danh sách đơn hàng:", error);
      } finally {
        setLoading(false);
      }
    };

    fetchRecentOrders();
  }, []);

  return (
    <TableContainer component={Paper} className="table">
      <Table sx={{ minWidth: 650 }} aria-label="recent orders table">
        <TableHead>
          <TableRow>
            <TableCell className="tableCell">Tracking ID</TableCell>
            <TableCell className="tableCell">Product</TableCell>
            <TableCell className="tableCell">Customer</TableCell>
            <TableCell className="tableCell">Date</TableCell>
            <TableCell className="tableCell">Amount</TableCell>
            <TableCell className="tableCell">Payment Method</TableCell>
            <TableCell className="tableCell">Status</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {loading ? (
            <TableRow>
              <TableCell colSpan={7} align="center">
                Loading...
              </TableCell>
            </TableRow>
          ) : (
            rows.map((row) => (
              <TableRow key={row.id}>
                {/* Tracking ID */}
                <TableCell className="tableCell">{row.id}</TableCell>

                {/* Product */}
                <TableCell className="tableCell">
                  <div className="cellWrapper">
                    {console.log(row.product.length)}

                    {typeof row.product === "string" && row.product.length > 10
                      ? `${row.product.slice(0, 10)}...`
                      : row.product}
                  </div>
                </TableCell>

                {/* Customer */}
                <TableCell className="tableCell">{row.customer}</TableCell>

                {/* Date */}
                <TableCell className="tableCell">
                  {new Date(row.date).toLocaleDateString()}
                </TableCell>

                {/* Amount */}
                <TableCell className="tableCell">{`$${row.amount.toLocaleString()}`}</TableCell>

                {/* Payment Method */}
                <TableCell className="tableCell">{row.method}</TableCell>

                {/* Status */}
                <TableCell className="tableCell">
                  <span className={`status ${row.status.toLowerCase()}`}>
                    {row.status}
                  </span>
                </TableCell>
              </TableRow>
            ))
          )}
        </TableBody>
      </Table>
    </TableContainer>
  );
};

export default List;