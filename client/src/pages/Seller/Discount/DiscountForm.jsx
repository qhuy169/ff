import React, { useState } from 'react';
import { discountService } from '../../../services/discount.service';
import { useNavigate } from 'react-router-dom';

const DiscountForm = () => {
    const customerInfo = JSON.parse(localStorage.getItem("customerInfo"));
    const navi = useNavigate()
    const shopId = customerInfo?.shopId;
    const [formData, setFormData] = useState({
        name: '',
        description: '',
        quantity: 0,
        percent: 0,
        code: '',
        cappedAt: '',
        price: 0,
        minSpend: 0,
        shopId: 14,
        startDate: '',
        endDate: '',
        type: 'DISCOUNT_BILL_PRICE',
        image: null,
    });

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData((prev) => ({
            ...prev,
            [name]: value,
        }));
    };

    const handleImageChange = (e) => {
        const file = e.target.files[0];
        setFormData((prev) => ({
            ...prev,
            image: file,
        }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        const discount = {
            name: formData.name,
            description: formData.description,
            quantity: formData.quantity,
            percent: formData.percent,
            code: formData.code,
            cappedAt: formData.cappedAt,
            price: formData.price,
            minSpend: formData.minSpend,
            startDate: formData.startDate,
            shopId: shopId,
            endDate: formData.endDate,
            type: formData.type,
        };

        let formDataToSend = new FormData();
        formDataToSend.append("data", new Blob([JSON.stringify(discount)], { type: "application/json" }));

        if (formData.image) {
            formDataToSend.append('image', formData.image);
        }

        try {
            const response = await discountService.postDiscount(formDataToSend);
            if (response.status === 200) {
                alert('Discount created successfully!');
            }
            navi("/seller/discount/list")
        } catch (error) {
            alert('Error creating discount: ' + error.message);
        }
    };

    return (
        <div className="max-w-4xl mx-auto p-6 bg-white rounded-lg shadow-lg">
            <h2 className="text-3xl font-bold text-center text-gray-800 mb-6">Create Discount</h2>
            <form onSubmit={handleSubmit} encType="multipart/form-data">
                {/* Tên discount */}
                <div className="mb-6">
                    <label htmlFor="name" className="block text-sm font-medium text-gray-700 mb-2">Name</label>
                    <input
                        type="text"
                        id="name"
                        name="name"
                        value={formData.name}
                        onChange={handleChange}
                        className="w-full p-3 border border-gray-300 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
                        required
                    />
                </div>

                {/* Mô tả discount */}
                <div className="mb-6">
                    <label htmlFor="description" className="block text-sm font-medium text-gray-700 mb-2">Description</label>
                    <textarea
                        id="description"
                        name="description"
                        value={formData.description}
                        onChange={handleChange}
                        className="w-full p-3 border border-gray-300 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
                        rows="4"
                        required
                    />
                </div>

                {/* Số lượng */}
                <div className="mb-6">
                    <label htmlFor="quantity" className="block text-sm font-medium text-gray-700 mb-2">Quantity</label>
                    <input
                        type="number"
                        id="quantity"
                        name="quantity"
                        value={formData.quantity}
                        onChange={handleChange}
                        className="w-full p-3 border border-gray-300 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
                        required
                    />
                </div>

                {/* Phần trăm discount */}
                <div className="mb-6">
                    <label htmlFor="percent" className="block text-sm font-medium text-gray-700 mb-2">Percent</label>
                    <input
                        type="number"
                        id="percent"
                        name="percent"
                        value={formData.percent}
                        onChange={handleChange}
                        className="w-full p-3 border border-gray-300 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
                        step="0.01"
                        min="0"
                        max="1"
                        required
                    />
                </div>

                {/* Mã code discount */}
                <div className="mb-6">
                    <label htmlFor="code" className="block text-sm font-medium text-gray-700 mb-2">Discount Code</label>
                    <input
                        type="text"
                        id="code"
                        name="code"
                        value={formData.code}
                        onChange={handleChange}
                        className="w-full p-3 border border-gray-300 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
                        required
                    />
                </div>

                {/* Giới hạn giá trị */}
                <div className="mb-6">
                    <label htmlFor="cappedAt" className="block text-sm font-medium text-gray-700 mb-2">Capped At</label>
                    <input
                        type="number"
                        id="cappedAt"
                        name="cappedAt"
                        value={formData.cappedAt}
                        onChange={handleChange}
                        className="w-full p-3 border border-gray-300 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
                    />
                </div>

                {/* Giá trị */}
                <div className="mb-6">
                    <label htmlFor="price" className="block text-sm font-medium text-gray-700 mb-2">Price</label>
                    <input
                        type="number"
                        id="price"
                        name="price"
                        value={formData.price}
                        onChange={handleChange}
                        className="w-full p-3 border border-gray-300 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
                        required
                    />
                </div>

                {/* Min Spend */}
                <div className="mb-6">
                    <label htmlFor="minSpend" className="block text-sm font-medium text-gray-700 mb-2">Min Spend</label>
                    <input
                        type="number"
                        id="minSpend"
                        name="minSpend"
                        value={formData.minSpend}
                        onChange={handleChange}
                        className="w-full p-3 border border-gray-300 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
                        required
                    />
                </div>

                {/* Ngày bắt đầu */}
                <div className="mb-6">
                    <label htmlFor="startDate" className="block text-sm font-medium text-gray-700 mb-2">Start Date</label>
                    <input
                        type="date"
                        id="startDate"
                        name="startDate"
                        value={formData.startDate}
                        onChange={handleChange}
                        className="w-full p-3 border border-gray-300 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
                        required
                    />
                </div>

                {/* Ngày kết thúc */}
                <div className="mb-6">
                    <label htmlFor="endDate" className="block text-sm font-medium text-gray-700 mb-2">End Date</label>
                    <input
                        type="date"
                        id="endDate"
                        name="endDate"
                        value={formData.endDate}
                        onChange={handleChange}
                        className="w-full p-3 border border-gray-300 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
                        required
                    />
                </div>

                {/* Loại discount */}
                <div className="mb-6">
                    <label htmlFor="type" className="block text-sm font-medium text-gray-700 mb-2">Discount Type</label>
                    <select
                        id="type"
                        name="type"
                        value={formData.type}
                        onChange={handleChange}
                        className="w-full p-3 border border-gray-300 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
                        required
                    >
                        <option value="DISCOUNT_SHOP_PRICE">Shop Price Discount</option>
                        <option value="DISCOUNT_SHOP_PERCENT">Shop Percent Discount</option>
                        <option value="DISCOUNT_BILL_PRICE">Bill Price Discount</option>
                        <option value="DISCOUNT_BILL_PERCENT">Bill Percent Discount</option>
                    </select>
                </div>

                {/* Chọn ảnh */}
                <div className="mb-6">
                    <label htmlFor="image" className="block text-sm font-medium text-gray-700 mb-2">Thumbnail Image</label>
                    <input
                        type="file"
                        id="image"
                        name="image"
                        onChange={handleImageChange}
                        className="w-full p-3 border border-gray-300 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
                    />
                </div>

                {/* Nút gửi */}
                <button
                    type="submit"
                    className="w-full bg-blue-600 text-white py-3 rounded-lg hover:bg-blue-700 transition-colors duration-300"
                >
                    Create Discount
                </button>
            </form>
        </div>
    );
};

export default DiscountForm;
