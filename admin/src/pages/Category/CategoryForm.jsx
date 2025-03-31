import { useState, useEffect } from "react";
import { useForm } from "react-hook-form";
import CategoryService from "../../services/category.service";

const CategoryForm = () => {
  const { register, handleSubmit, watch, reset, setValue } = useForm();
  const [categories, setCategories] = useState([]);
  const [imagePreview, setImagePreview] = useState(null);

  useEffect(() => {
    CategoryService.getAllCategories()
      .then((response) => setCategories(response.data))
      .catch((error) => console.error("Lỗi lấy danh mục:", error));
  }, []);

  // Tạo slug tự động khi nhập tên danh mục
  const name = watch("name", "");
  useEffect(() => {
    if (name) {
      const slug = name
        .toLowerCase()
        .replace(/\s+/g, "-")
        .replace(/[^a-z0-9-]/g, "");
      setValue("slug", slug);
    }
  }, [name, setValue]);

  // Xử lý chọn ảnh và xem trước
  const imageFile = watch("image");
  useEffect(() => {
    if (imageFile?.[0]) {
      setImagePreview(URL.createObjectURL(imageFile[0]));
    }
  }, [imageFile]);

  // Xử lý submit form
  const onSubmit = async (data) => {
    const formData = new FormData();

    // Định dạng đúng API: `data` là JSON
    const jsonData = {
      name: data.name,
      description: data.description || "",
      parentCategoryId: data.parentCategoryId
        ? parseInt(data.parentCategoryId)
        : null,
    };
    formData.append("data", new Blob([JSON.stringify(jsonData)], { type: "application/json" }));

    // Đính kèm ảnh nếu có
    if (data.image?.[0]) {
      formData.append("image", data.image[0]);
    }

    try {
      await CategoryService.createCategory(formData);
      alert("Thêm danh mục thành công!");
      reset();
      setImagePreview(null);
    } catch (error) {
      console.error("Lỗi tạo danh mục:", error);
      alert("Có lỗi xảy ra!");
    }
  };

  return (
    <div className="max-w-xl mx-auto bg-white p-6 shadow-md rounded-lg">
      <h2 className="text-2xl font-bold mb-4 flex items-center gap-2">
        <i className="bi bi-tags text-blue-500"></i> Thêm Danh Mục
      </h2>
      <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
        {/* Tên danh mục */}
        <div>
          <label className="block font-medium">Tên danh mục</label>
          <input
            {...register("name", { required: true })}
            className="w-full p-2 border rounded-md focus:ring focus:ring-blue-200"
          />
        </div>

        {/* Slug */}
        <div>
          <label className="block font-medium">Slug</label>
          <input
            {...register("slug")}
            className="w-full p-2 border rounded-md bg-gray-100"
            readOnly
          />
        </div>

        {/* Mô tả */}
        <div>
          <label className="block font-medium">Mô tả</label>
          <textarea
            {...register("description")}
            className="w-full p-2 border rounded-md focus:ring focus:ring-blue-200"
          ></textarea>
        </div>

        {/* Danh mục cha */}
        <div>
          <label className="block font-medium">Danh mục cha</label>
          <select
            {...register("parentCategoryId")}
            className="w-full p-2 border rounded-md"
          >
            <option value="">Không có</option>
            {categories.map((cat) => (
              <option key={cat.id} value={cat.id}>
                {cat.name}
              </option>
            ))}
          </select>
        </div>

        {/* Ảnh (imageFile) */}
        <div>
          <label className="block font-medium">Ảnh danh mục</label>
          <input
            type="file"
            {...register("image")}
            className="w-full border p-2 rounded-md"
          />
          {imagePreview && (
            <img
              src={imagePreview}
              alt="Ảnh danh mục"
              className="mt-2 w-32 h-32 object-cover rounded-md"
            />
          )}
        </div>

        <button
          type="submit"
          className="w-full bg-blue-500 text-white py-2 rounded-md hover:bg-blue-600 flex items-center justify-center gap-2"
        >
          <i className="bi bi-plus-lg"></i> Thêm danh mục
        </button>
      </form>
    </div>
  );
};

export default CategoryForm;
