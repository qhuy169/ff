import Filter from '../../../components/Filter/Filter';
import { useState } from 'react';
const data = [
    {
        id: 1,
        title: 'Giá',
        detail: ['Dưới 2 triệu', 'Từ 2-4 triệu', 'Từ 4-7 triệu', 'Từ 7-13 triệu', 'Từ 13-20 triệu', 'Trên 20 triệu'],
    },
    {
        id: 2,
        title: 'Loại điện thoại',
        detail: ['Android', 'Iphone(IOS)', 'Điện thoại phổ thông'],
    },
    {
        id: 3,
        title: 'Nhu cầu',
        detail: ['Chơi game/Cấu hình cao', 'Chụp ảnh, quay phim', 'Mỏng nhẹ', 'Nhỏ gọn dễ cầm'],
    },
    {
        id: 4,
        title: 'RAM',
        detail: ['2 GB', '3 GB', '4 GB', '6 GB', '8 GB', '12 GB'],
    },
    {
        id: 5,
        title: 'ROM',
        detail: ['32 GB', '64 GB', '128 GB', '256 GB', '512 GB'],
    },
    {
        id: 6,
        title: 'Pin&Sạc',
        detail: ['Pin khủng 5000 mAh', 'Sạc nhanh(từ 18W)', 'Sạc siêu nhanh(từ 33W)', 'Sạc không dây'],
    },
    {
        id: 7,
        title: 'Tính năng đặc biệt',
        detail: ['Kháng nước, bụi', 'Hỗ trợ 5G', 'Bảo mật khuôn mặt 3D', 'Chống rung quang học(OIS)'],
    },
];
const FilterCategory = (props) => {
    return (
        <div style={{ maxWidth: '1200px', margin: '6% auto 0 auto' }}>
            <Filter handle={props.handle} data={data} />
        </div>
    );
};

export default FilterCategory;
