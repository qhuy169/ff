import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { discountService } from "../../../services/discount.service";

const DiscountList = () => {
  const [discounts, setDiscounts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showModal, setShowModal] = useState(false);
  const [editDiscount, setEditDiscount] = useState(null);
  const navigate = useNavigate();

  const shopId = (() => {
    try {
      const customerInfo = JSON.parse(localStorage.getItem("customerInfo"));
      return customerInfo?.shopId || null;
    } catch (error) {
      console.error("Lỗi khi lấy shopId từ localStorage:", error);
      return null;
    }
  })();

  useEffect(() => {
    if (shopId) {
      loadDiscounts();
    }
  }, [shopId]);

  const loadDiscounts = async () => {
    try {
      setLoading(true);
      const response = await discountService.getDiscountByShopId(shopId);
      setDiscounts(response.data);
    } catch (error) {
      console.error("Lỗi khi lấy danh sách giảm giá:", error);
    } finally {
      setLoading(false);
    }
  };

  const handleEdit = (discount) => {
    setEditDiscount(discount);
    setShowModal(true);
  };

  const handleDelete = async (id) => {
    if (window.confirm("Bạn có chắc chắn muốn xóa giảm giá này?")) {
      try {
        await discountService.deleteDiscount(id);
        alert("Xóa giảm giá thành công!");
        loadDiscounts();
      } catch (error) {
        console.error("Lỗi khi xóa giảm giá:", error);
      }
    }
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setEditDiscount((prev) => ({ ...prev, [name]: value }));
  };

  const handleUpdate = async () => {
    try {
      const formData = new FormData();
  
      const discountData = {
        id: editDiscount.id,
        name: editDiscount.name || null,
        description: editDiscount.description || null,
        quantity: editDiscount.quantity,
        percent: editDiscount.percent,
        code: editDiscount.code,
        cappedAt: editDiscount.cappedAt || null,
        price: editDiscount.price || null,
        minSpend: editDiscount.minSpend || 0,
        startDate: editDiscount.startDate,
        endDate: editDiscount.endDate,
        type: editDiscount.type || 1,
        shopId: shopId,
      };
  
      // Chuyển object thành JSON và append vào formData
      formData.append("data", new Blob([JSON.stringify(discountData)], { type: "application/json" }));
  
      // Nếu có file ảnh thì thêm vào formData
      if (editDiscount.imageFile) {
        formData.append("image", editDiscount.imageFile);
      }
  
      const response = await discountService.putDiscount(editDiscount.id, formData);
  
      if (response.status === 200) {
        alert("Cập nhật giảm giá thành công!");
        setShowModal(false);
        loadDiscounts();  // Refresh danh sách
      } else {
        alert("Cập nhật không thành công!");
      }
    } catch (error) {
      console.error("Lỗi khi cập nhật giảm giá:", error.response?.data || error);
      alert("Không thể cập nhật giảm giá!");
    }
  };
    
  return (
    <div className="max-w-4xl mx-auto bg-white p-6 shadow-md rounded-lg">
      <h2 className="text-2xl font-bold mb-4">Danh Sách Giảm Giá</h2>
      <button onClick={() => navigate("/seller/discount/add")} className="bg-green-500 text-white px-4 py-2 rounded-md hover:bg-green-600 mb-4">
        + Thêm giảm giá
      </button>
      {loading ? (
        <p>Đang tải...</p>
      ) : (
        <table className="w-full border-collapse border border-gray-300">
          <thead>
            <tr className="bg-gray-100">
              <th className="border p-2">ID</th>
              <th className="border p-2">Mã</th>
              <th className="border p-2">Giảm (%)</th>
              <th className="border p-2">Số lượng</th>
              <th className="border p-2">Ngày bắt đầu</th>
              <th className="border p-2">Ngày kết thúc</th>
              <th className="border p-2">Hành động</th>
            </tr>
          </thead>
          <tbody>
            {discounts.length > 0 ? (
              discounts.map((discount) => (
                <tr key={discount.id} className="text-center">
                  <td className="border p-2">{discount.id}</td>
                  <td className="border p-2">{discount.code}</td>
                  <td className="border p-2">{discount.percent * 100}%</td>
                  <td className="border p-2">{discount.quantity}</td>
                  <td className="border p-2">{discount.startDate.split("T")[0]}</td>
                  <td className="border p-2">{discount.endDate.split("T")[0]}</td>
                  <td className="border p-2 flex justify-center gap-2">
                    <button onClick={() => handleEdit(discount)} className="bg-blue-500 text-white px-3 py-1 rounded-md hover:bg-blue-600">
                      Sửa
                    </button>
                    <button onClick={() => handleDelete(discount.id)} className="bg-red-500 text-white px-3 py-1 rounded-md hover:bg-red-600">
                      Xóa
                    </button>
                  </td>
                </tr>
              ))
            ) : (
              <tr>
                <td colSpan="7" className="border p-2 text-center">Không có giảm giá nào.</td>
              </tr>
            )}
          </tbody>
        </table>
      )}
      {showModal && (
        <div className="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50">
          <div className="bg-white p-6 rounded-md shadow-lg w-96">
            <h3 className="text-xl font-bold mb-4">Chỉnh Sửa Giảm Giá</h3>
            <input type="text" name="code" value={editDiscount.code} onChange={handleChange} className="w-full p-2 border rounded-md mb-4" />
            <input type="number" name="percent" value={editDiscount.percent} onChange={handleChange} className="w-full p-2 border rounded-md mb-4" />
            <input type="number" name="quantity" value={editDiscount.quantity} onChange={handleChange} className="w-full p-2 border rounded-md mb-4" />
            <button onClick={handleUpdate} className="bg-green-500 text-white px-4 py-2 rounded-md mr-2">Cập nhật</button>
            <button onClick={() => setShowModal(false)} className="bg-gray-500 text-white px-4 py-2 rounded-md">Hủy</button>
          </div>
        </div>
      )}
    </div>
  );
};

export default DiscountList;