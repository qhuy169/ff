import { useState, useEffect } from "react";
import { useForm } from "react-hook-form";
import DiscountService from "../../services/discount.service";
import discountService from "../../services/discount.service";

const DiscountForm = () => {
  const { register, handleSubmit, watch, reset, setValue } = useForm();
  const [imagePreview, setImagePreview] = useState(null);

  // Lấy giá trị từ form
  const name = watch("name", "");
  const code = watch("code", "");

  // Tạo slug tự động khi nhập tên giảm giá
  useEffect(() => {
    if (name) {
      const slug = name
        .toLowerCase()
        .replace(/\s+/g, "-")
        .replace(/[^a-z0-9-]/g, "");
      setValue("slug", slug);
    }
  }, [name, setValue]);

  // Tạo mã giảm giá tự động nếu chưa có
  useEffect(() => {
    if (!code && name) {
      const generatedCode =
        name.toUpperCase().replace(/\s+/g, "_") + "_" + Math.floor(1000 + Math.random() * 9000);
      setValue("code", generatedCode);
    }
  }, [name, code, setValue]);

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
  
    // Chuẩn bị dữ liệu JSON
    const jsonData = {
      name: data.name,
      code: data.code, // Thêm trường code
      slug: data.slug,
      description: data.description || "",
      percent: data.percent ? parseFloat(data.percent) : 0,
      price: data.price ? parseFloat(data.price) : 0,
      quantity: data.quantity ? parseInt(data.quantity) : 0,
      // Thêm startDate và endDate nếu có
      startDate: data.startDate || null,
      endDate: data.endDate || null,
    };
  
    // Đính kèm dữ liệu JSON dưới dạng Blob
    formData.append("data", new Blob([JSON.stringify(jsonData)], { type: "application/json" }));
  
    // Đính kèm ảnh nếu có
    if (data.image?.[0]) {
      formData.append("image", data.image[0]);
    }
  
    try {
      // Gọi API để tạo giảm giá
      await discountService.createDiscount(formData);
      alert("Thêm giảm giá thành công!");
      reset();
      setImagePreview(null); // Reset ảnh preview
    } catch (error) {
      console.error("Lỗi tạo giảm giá:", error);
      alert("Có lỗi xảy ra!");
    }
  };
  
  return (
    <div className="max-w-xl mx-auto bg-white p-6 shadow-md rounded-lg">
      <h2 className="text-2xl font-bold mb-4 flex items-center gap-2">
        <i className="bi bi-tags text-blue-500"></i> Thêm Giảm Giá
      </h2>
      <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
        {/* Tên giảm giá */}
        <div>
          <label className="block font-medium">Tên giảm giá</label>
          <input
            {...register("name", { required: true })}
            className="w-full p-2 border rounded-md focus:ring focus:ring-blue-200"
          />
        </div>

        {/* Mã giảm giá */}
        <div>
          <label className="block font-medium">Mã giảm giá</label>
          <input
            {...register("code", { required: true })}
            className="w-full p-2 border rounded-md"
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

        {/* % giảm giá */}
        <div>
          <label className="block font-medium">% Giảm giá</label>
          <input
            type="number"
            {...register("percent")}
            className="w-full p-2 border rounded-md"
            min="0"
            max="100"
          />
        </div>

        {/* Giá giảm */}
        <div>
          <label className="block font-medium">Giá giảm</label>
          <input
            type="number"
            {...register("price")}
            className="w-full p-2 border rounded-md"
            min="0"
          />
        </div>

        {/* Số lượng */}
        <div>
          <label className="block font-medium">Số lượng</label>
          <input
            type="number"
            {...register("quantity")}
            className="w-full p-2 border rounded-md"
            min="0"
          />
        </div>

        {/* Ảnh */}
        <div>
          <label className="block font-medium">Ảnh giảm giá</label>
          <input
            type="file"
            {...register("image")}
            className="w-full border p-2 rounded-md"
          />
          {imagePreview && (
            <img
              src={imagePreview}
              alt="Ảnh giảm giá"
              className="mt-2 w-32 h-32 object-cover rounded-md"
            />
          )}
        </div>

        <button
          type="submit"
          className="w-full bg-blue-500 text-white py-2 rounded-md hover:bg-blue-600 flex items-center justify-center gap-2"
        >
          <i className="bi bi-plus-lg"></i> Thêm giảm giá
        </button>
      </form>
    </div>
  );
};

export default DiscountForm;
