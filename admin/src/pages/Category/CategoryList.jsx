import { useState, useEffect } from "react";
import CategoryService from "../../services/category.service";
import categoryService from "../../services/category.service";
import { useNavigate } from "react-router-dom";

const CategoryList = () => {
  const [categories, setCategories] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showModal, setShowModal] = useState(false);
  const navigate = useNavigate();

  const [editCategory, setEditCategory] = useState({
    id: "",
    name: "",
    description: "",
    parentCategoryId: null,
    imageFile: null
  });

  useEffect(() => {
    loadCategories();
  }, []);

  const loadCategories = async () => {
    try {
      setLoading(true);
      const response = await CategoryService.getAllCategories();
      setCategories(response.data);
    } catch (error) {
      console.error("Lỗi khi tải danh mục:", error);
    } finally {
      setLoading(false);
    }
  };

  // Mở modal chỉnh sửa
  const handleEdit = (category) => {
    setEditCategory({
      id: category.id,
      name: category.name,
      description: category.description || "",
      parentCategoryId: category.parentCategory ? category.parentCategory.id : null,
      imageFile: null
    });
    setShowModal(true);
  };

  // Xử lý thay đổi input
  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setEditCategory((prev) => ({ ...prev, [name]: value }));
  };

  // Xử lý tải ảnh lên
  const handleFileChange = (e) => {
    setEditCategory((prev) => ({ ...prev, imageFile: e.target.files[0] }));
  };

  // Gửi dữ liệu cập nhật lên API
  const handleUpdate = async () => {
    try {
        const formData = new FormData();
        
        // Chuyển object thành JSON string
        const categoryData = {
            name: editCategory.name,
            description: editCategory.description,
            parentCategoryId: editCategory.parentCategoryId || null
        };

        formData.append("data", new Blob([JSON.stringify(categoryData)], { type: "application/json" }));
        
        if (editCategory.imageFile) {
            formData.append("imageFile", editCategory.imageFile);
        }

        console.log("Dữ liệu gửi lên API:", Object.fromEntries(formData.entries()));

        await CategoryService.updateCategory(editCategory.id, formData);
        alert("Cập nhật danh mục thành công!");
        setShowModal(false);
        loadCategories();
    } catch (error) {
        console.error("Lỗi khi cập nhật danh mục:", error.response?.data || error);
        alert("Không thể cập nhật danh mục! Lỗi: " + (error.response?.data?.message || "Không xác định"));
    }
};


  return (
    <div className="max-w-4xl mx-auto bg-white p-6 shadow-md rounded-lg">
      <h2 className="text-2xl font-bold mb-4">Danh Sách Danh Mục</h2>
      <button
        onClick={() => navigate("/category/add")}
        className="bg-green-500 text-white px-4 py-2 rounded-md hover:bg-green-600 mb-4"
      >
        + Thêm danh mục
      </button>

      {loading ? (
        <p>Đang tải...</p>
      ) : (
        <table className="w-full border-collapse border border-gray-300">
          <thead>
            <tr className="bg-gray-100">
              <th className="border p-2">ID</th>
              <th className="border p-2">Tên danh mục</th>
              <th className="border p-2">Mô tả</th>
              <th className="border p-2">Hành động</th>
            </tr>
          </thead>
          <tbody>
            {categories.length > 0 ? (
              categories.map((category) => (
                <tr key={category.id} className="text-center">
                  <td className="border p-2">{category.id}</td>
                  <td className="border p-2">{category.title}</td>
                  <td className="border p-2">{category.description || "Không có mô tả"}</td>
                  <td className="border p-2 flex justify-center gap-2">
                    <button
                      onClick={() => handleEdit(category)}
                      className="bg-blue-500 text-white px-3 py-1 rounded-md hover:bg-blue-600"
                    >
                      Sửa
                    </button>
                    <button
                      onClick={() => alert("Xóa danh mục (Chưa triển khai)")}
                      className="bg-red-500 text-white px-3 py-1 rounded-md hover:bg-red-600"
                    >
                      Xóa
                    </button>
                  </td>
                </tr>
              ))
            ) : (
              <tr>
                <td colSpan="4" className="border p-2 text-center">
                  Không có danh mục nào.
                </td>
              </tr>
            )}
          </tbody>
        </table>
      )}

      {/* Modal sửa danh mục */}
      {showModal && (
        <div className="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50">
          <div className="bg-white p-6 rounded-md shadow-lg w-96">
            <h3 className="text-xl font-bold mb-4">Chỉnh Sửa Danh Mục</h3>
            <label className="block font-medium">Tên danh mục</label>
            <input
              type="text"
              name="name"
              value={editCategory.name}
              onChange={handleInputChange}
              className="w-full p-2 border rounded-md focus:ring focus:ring-blue-200 mb-4"
            />
            <label className="block font-medium">Mô tả</label>
            <textarea
              name="description"
              value={editCategory.description}
              onChange={handleInputChange}
              className="w-full p-2 border rounded-md focus:ring focus:ring-blue-200 mb-4"
            ></textarea>
            <label className="block font-medium">Ảnh danh mục</label>
            <input
              type="file"
              accept="image/*"
              onChange={handleFileChange}
              className="w-full p-2 border rounded-md focus:ring focus:ring-blue-200 mb-4"
            />
            <div className="flex justify-end gap-2">
              <button
                onClick={() => setShowModal(false)}
                className="bg-gray-500 text-white px-4 py-2 rounded-md hover:bg-gray-600"
              >
                Hủy
              </button>
              <button
                onClick={handleUpdate}
                className="bg-green-500 text-white px-4 py-2 rounded-md hover:bg-green-600"
              >
                Cập nhật
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default CategoryList;
