import axiosInstance from "../api";

const DISCOUNT_API = "/discounts";

class DiscountService {
    // Lấy danh sách tất cả các giảm giá
    getAllDiscounts() {
        return axiosInstance.get(DISCOUNT_API);
    }

    // Lấy danh sách giảm giá theo ID danh mục
    getDiscountsByCategoryId(categoryId) {
        return axiosInstance.get(`${DISCOUNT_API}/category/${categoryId}`);
    }

    // Lấy chi tiết giảm giá theo ID
    getDiscountById(discountId) {
        return axiosInstance.get(`${DISCOUNT_API}/${discountId}`);
    }

    // Tạo mới giảm giá (hỗ trợ upload ảnh)
    createDiscount(discountData) {
        const formData = new FormData();
      
        // Chuẩn bị dữ liệu JSON
        const jsonData = {
          name: discountData.name,
          code: discountData.code, // Thêm trường code
          slug: discountData.slug,
          description: discountData.description || "",
          percent: discountData.percent ? parseFloat(discountData.percent) : 0,
          price: discountData.price ? parseFloat(discountData.price) : 0,
          quantity: discountData.quantity ? parseInt(discountData.quantity) : 0,
          // Thêm startDate và endDate nếu có
          startDate: discountData.startDate || null,
          endDate: discountData.endDate || null,
        };
      
        // Đính kèm dữ liệu JSON dưới dạng Blob
        formData.append("data", new Blob([JSON.stringify(jsonData)], { type: "application/json" }));
      
        // Đính kèm ảnh nếu có
        if (discountData.image && discountData.image[0]) {
          formData.append("image", discountData.image[0]);
        }
      
        return axiosInstance.post(DISCOUNT_API, formData, {
          headers: {
            "Content-Type": "multipart/form-data", // Cần thiết khi gửi FormData
          },
        });
      }
      
    // Cập nhật giảm giá (hỗ trợ upload ảnh)
    updateDiscount(discountId, discountData) {
        const formData = new FormData();

        const jsonData = {
            name: discountData.name,
            slug: discountData.slug,
            code: discountData.code,
            description: discountData.description || "",
            percent: discountData.percent ? parseFloat(discountData.percent) : 0,
            price: discountData.price ? parseFloat(discountData.price) : 0,
            quantity: discountData.quantity ? parseInt(discountData.quantity) : 0,
        };
        formData.append("data", new Blob([JSON.stringify(jsonData)], { type: "application/json" }));

        if (discountData.image && discountData.image[0]) {
            formData.append("image", discountData.image[0]);
        }

        return axiosInstance.put(`${DISCOUNT_API}/${discountId}`, formData, {
            headers: {
                "Content-Type": "multipart/form-data",
            },
        });
    }

    // Xóa giảm giá
    deleteDiscount(discountId) {
        return axiosInstance.delete(`${DISCOUNT_API}/${discountId}`);
    }
}

export default new DiscountService();
