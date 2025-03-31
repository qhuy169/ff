import { useState, useEffect } from "react";
import DiscountService from "../../services/discount.service";
import { useNavigate } from "react-router-dom";

const DiscountList = () => {
  const [discounts, setDiscounts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showModal, setShowModal] = useState(false);
  const navigate = useNavigate();

  const [editDiscount, setEditDiscount] = useState({
    id: "",
    name: "",
    description: "",
    percentage: "",
    imageFile: null,
  });
  const handleDelete = async (id) => {
    const confirmDelete = window.confirm("Bạn có chắc chắn muốn xóa giảm giá này?");
    if (!confirmDelete) return;

    try {
      await DiscountService.deleteDiscount(id);
      alert("Xóa giảm giá thành công!");
      loadDiscounts();
    } catch (error) {
      console.error("Lỗi khi xóa giảm giá:", error.response?.data || error);
      alert("Không thể xóa giảm giá!");
    }
  };

  useEffect(() => {
    loadDiscounts();
  }, []);

  const loadDiscounts = async () => {
    try {
      setLoading(true);
      const response = await DiscountService.getAllDiscounts();
      setDiscounts(response.data);
      console.log(response.data);
      
    } catch (error) {
      console.error("Lỗi khi tải giảm giá:", error);
    } finally {
      setLoading(false);
    }
  };

  const handleEdit = (discount) => {
    setEditDiscount({
      id: discount.id,
      name: discount.name,
      description: discount.description || "",
      percentage: discount.percentage || "",
      imageFile: null,
    });
    setShowModal(true);
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setEditDiscount((prev) => ({ ...prev, [name]: value }));
  };

  const handleFileChange = (e) => {
    setEditDiscount((prev) => ({ ...prev, imageFile: e.target.files[0] }));
  };

  const handleUpdate = async () => {
    try {
      const formData = new FormData();
      const discountData = {
        name: editDiscount.name,
        description: editDiscount.description,
        percentage: editDiscount.percentage,
      };
      formData.append("data", new Blob([JSON.stringify(discountData)], { type: "application/json" }));
      if (editDiscount.imageFile) {
        formData.append("imageFile", editDiscount.imageFile);
      }
      await DiscountService.updateDiscount(editDiscount.id, formData);
      alert("Cập nhật giảm giá thành công!");
      setShowModal(false);
      loadDiscounts();
    } catch (error) {
      console.error("Lỗi khi cập nhật giảm giá:", error.response?.data || error);
      alert("Không thể cập nhật giảm giá!");
    }
  };

  return (
    <div className="max-w-4xl mx-auto bg-white p-6 shadow-md rounded-lg">
      <h2 className="text-2xl font-bold mb-4">Danh Sách Giảm Giá</h2>
      <button
        onClick={() => navigate("/discount/add")}
        className="bg-green-500 text-white px-4 py-2 rounded-md hover:bg-green-600 mb-4"
      >
        + Thêm giảm giá
      </button>

      {loading ? (
        <p>Đang tải...</p>
      ) : (
        <table className="w-full border-collapse border border-gray-300">
     <thead>
  <tr className="bg-gray-100">
    <th className="border p-2">ID</th>
    <th className="border p-2">Tên</th>
    <th className="border p-2">Mô tả</th>
    <th className="border p-2">Phần trăm</th>
    <th className="border p-2">Giá trị giảm</th>
    <th className="border p-2">Số lượng</th>
    <th className="border p-2">Hành động</th>
  </tr>
</thead>

          <tbody>
  {discounts.length > 0 ? (
    discounts.map((discount) => (
      <tr key={discount.id} className="text-center">
        <td className="border p-2">{discount.id}</td>
        <td className="border p-2">{discount.name || "Không có thông tin"}</td>
        <td className="border p-2">{discount.description || "Không có mô tả"}</td>
        <td className="border p-2">{discount.percent !== null ? `${discount.percent}%` : "Không có"}</td>
        <td className="border p-2">{discount.price}đ</td>
        <td className="border p-2">{discount.quantity}</td>
        <td className="border p-2 flex justify-center gap-2">
          <button
            onClick={() => handleEdit(discount)}
            className="bg-blue-500 text-white px-3 py-1 rounded-md hover:bg-blue-600"
          >
            Sửa
          </button>
          <button
                      onClick={() => handleDelete(discount.id)}
                      className="bg-red-500 text-white px-3 py-1 rounded-md hover:bg-red-600"
                    >
                      Xóa
                    </button>
        </td>
      </tr>
    ))
  ) : (
    <tr>
      <td colSpan="7" className="border p-2 text-center">
        Không có giảm giá nào.
      </td>
    </tr>
  )}
</tbody>

        </table>
      )}

      {showModal && (
        <div className="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50">
          <div className="bg-white p-6 rounded-md shadow-lg w-96">
            <h3 className="text-xl font-bold mb-4">Chỉnh Sửa Giảm Giá</h3>
            <label className="block font-medium">Tên</label>
            <input
              type="text"
              name="name"
              value={editDiscount.name}
              onChange={handleInputChange}
              className="w-full p-2 border rounded-md mb-4"
            />
            <label className="block font-medium">Mô tả</label>
            <textarea
              name="description"
              value={editDiscount.description}
              onChange={handleInputChange}
              className="w-full p-2 border rounded-md mb-4"
            ></textarea>
            <label className="block font-medium">Phần trăm</label>
            <input
              type="number"
              name="percentage"
              value={editDiscount.percentage}
              onChange={handleInputChange}
              className="w-full p-2 border rounded-md mb-4"
            />
            <label className="block font-medium">Ảnh</label>
            <input
              type="file"
              accept="image/*"
              onChange={handleFileChange}
              className="w-full p-2 border rounded-md mb-4"
            />
            <div className="flex justify-end gap-2">
              <button onClick={() => setShowModal(false)} className="bg-gray-500 text-white px-4 py-2 rounded-md">Hủy</button>
              <button onClick={handleUpdate} className="bg-green-500 text-white px-4 py-2 rounded-md">Cập nhật</button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default DiscountList;