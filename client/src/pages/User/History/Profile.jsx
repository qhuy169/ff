import React, { useState } from 'react';
import { Form, Input, Button, Typography, message, Upload, Radio } from 'antd';
import { UploadOutlined } from '@ant-design/icons';
import axios from 'axios';
import { Pass } from 'react-bootstrap-icons';

const { Title } = Typography;

const Profile = () => {
    const user = JSON.parse(localStorage.getItem('customerInfo'));
    const { address } = user;
    const [form] = Form.useForm();
    const [edit, setEdit] = useState(false);
    const [file, setFile] = useState(null); // State to store the uploaded file

    const handleFinish = async (values) => {
        const id = user.id; // Extract the user ID from the current user data
        const data = {
            username: values.username,
            password: values.password,
            enabled: 1,
            firstName: values.firstName,
            lastName: values.lastName,
            gender: values.gender,
            email: values.email,
            phone: values.phone,
            role: user.role,
            address: {
                homeAdd: values.homeAdd,
                ward: values.ward,
                district: values.district,
                city: values.city,
            },
        };

        // Create a FormData object to handle both JSON data and the file
        const formData = new FormData();
        formData.append('data', new Blob([JSON.stringify(data)], { type: 'application/json' }));
        if (file) {
            formData.append('image', file);
        }

        try {
            const response = await axios.put(`http://localhost:8080/api/v1/users/${id}`, formData, {
                headers: {
                    'Content-Type': 'multipart/form-data',
                },
            });

            message.success('Profile updated successfully!');
            setEdit(false);

            // Optionally update localStorage with the new user data
            const updatedUser = { ...user, ...data };
            localStorage.setItem('customerInfo', JSON.stringify(updatedUser));
        } catch (error) {
            console.error('Error updating profile:', error);
            message.error('An error occurred while updating the profile.');
        }
    };

    const handleFileChange = (info) => {
        if (info.file.status === 'done' || info.file.status === 'uploading') {
            setFile(info.file.originFileObj);
        }
    };

    return (
        <div style={{ margin: '0 auto', padding: '20px' }} className="w-full max-w-[600px]">
            <Title level={3} className="text-center">Thông tin cá nhân</Title>
            <Form
                form={form}
                layout="vertical"
                initialValues={{
                    gender: user.gender,
                    Password: user.password,
                    username: user.username,
                    firstName: user.firstName,
                    lastName: user.lastName,
                    email: user.email,
                    phone: user.phone,
                    homeAdd: address?.homeAdd,
                    ward: address?.ward,
                    district: address?.district,
                    city: address?.city,
                }}
                onFinish={handleFinish}
                disabled={!edit}
            >
                <Form.Item
                    label="Tên đăng nhập"
                    name="username"
                    rules={[{ required: true, message: 'Vui lòng nhập tên đăng nhập!' }]}
                >
                    <Input placeholder="Tên đăng nhập" />
                </Form.Item>
                <Form.Item
                    label="Mật khẩu"
                    name="password"
                    rules={[{ required: true, message: 'Vui lòng nhập mật khẩu!' }]}
                >
                    <Input.Password placeholder="Mật khẩu" />
                </Form.Item>
                <Form.Item
                    label="Họ"
                    name="lastName"
                    rules={[{ required: true, message: 'Vui lòng nhập họ!' }]}
                >
                    <Input placeholder="Họ" />
                </Form.Item>

                <Form.Item
                    label="Tên"
                    name="firstName"
                    rules={[{ required: true, message: 'Vui lòng nhập tên!' }]}
                >
                    <Input placeholder="Tên" />
                </Form.Item>
                <Form.Item
                    label="Giới tính"
                    name="gender"
                    rules={[{ required: true, message: 'Vui lòng chọn giới tính!' }]}
                >
                    <Radio.Group>
                        <Radio value={1}>Nam</Radio>
                        <Radio value={2}>Nữ</Radio>
                    </Radio.Group>
                </Form.Item>
                <Form.Item
                    label="Email"
                    name="email"
                    rules={[{ type: 'email', message: 'Email không hợp lệ!' }]}
                >
                    <Input placeholder="Email" />
                </Form.Item>

                <Form.Item
                    label="Số điện thoại"
                    name="phone"
                    rules={[{ required: true, message: 'Vui lòng nhập số điện thoại!' }]}
                >
                    <Input placeholder="Số điện thoại" />
                </Form.Item>

                <Form.Item
                    label="Địa chỉ - Số nhà"
                    name="homeAdd"
                    rules={[{ required: true, message: 'Vui lòng nhập số nhà!' }]}
                >
                    <Input placeholder="Số nhà" />
                </Form.Item>

                <Form.Item
                    label="Phường/Xã"
                    name="ward"
                    rules={[{ required: true, message: 'Vui lòng nhập phường/xã!' }]}
                >
                    <Input placeholder="Phường/Xã" />
                </Form.Item>

                <Form.Item
                    label="Quận/Huyện"
                    name="district"
                    rules={[{ required: true, message: 'Vui lòng nhập quận/huyện!' }]}
                >
                    <Input placeholder="Quận/Huyện" />
                </Form.Item>

                <Form.Item
                    label="Thành phố"
                    name="city"
                    rules={[{ required: true, message: 'Vui lòng nhập thành phố!' }]}
                >
                    <Input placeholder="Thành phố" />
                </Form.Item>

                <Form.Item label="Ảnh đại diện">
                    <Upload
                        beforeUpload={() => false} // Prevent automatic upload
                        onChange={handleFileChange}
                        showUploadList={true}
                    >
                        <Button icon={<UploadOutlined />}>Chọn ảnh</Button>
                    </Upload>
                </Form.Item>

                {edit && (
                    <Form.Item>
                        <Button type="primary" htmlType="submit" block>
                            Cập nhật
                        </Button>
                    </Form.Item>
                )}
            </Form>

            <Button
                type="default"
                block
                style={{ marginTop: '20px' }}
                onClick={() => setEdit((prev) => !prev)}
            >
                {edit ? 'Hủy' : 'Sửa'}
            </Button>
        </div>
    );
};

export default Profile;