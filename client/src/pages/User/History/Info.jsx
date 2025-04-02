import { useState } from 'react';
import { Card, Typography, Space } from 'antd';
import { InfoCircleOutlined, UnorderedListOutlined } from '@ant-design/icons';
import PurchaseHistory from './PurchaseHistory';
import Welcome from './Welcome';
import Profile from './Profile';

const { Text } = Typography;

function Info() {
    const [activeTab, setActiveTab] = useState(null); // Tracks the active tab: 'purchaseHistory' or 'profile'

    const handleTabClick = (tabName) => {
        setActiveTab((prevTab) => (prevTab === tabName ? null : tabName)); // Toggle the tab
    };

    return (
        <div className="bg-white">
            <div
                className="w-full max-w-[1200px] mx-auto py-8 flex gap-10"
                style={{ display: 'flex', flexDirection: 'row', alignItems: 'flex-start' }}
            >
                {/* Sidebar */}
                <div
                    className="flex flex-col"
                    style={{
                        flex: '0 0 400px', // Fixed width for the sidebar
                        maxWidth: '400px',
                        minWidth: '400px',
                    }}
                >
                    <Welcome />
                    <Space direction="vertical" size="large" style={{ width: '100%' }}>
                        {/* Purchase History Tab */}
                        <Card
                            hoverable
                            onClick={() => handleTabClick('purchaseHistory')}
                            style={{
                                border: activeTab === 'purchaseHistory' ? '1px solid #1890ff' : '1px solid #f0f0f0',
                                borderRadius: '8px',
                            }}
                        >
                            <Space align="center">
                                <UnorderedListOutlined
                                    style={{
                                        fontSize: '24px',
                                        color: activeTab === 'purchaseHistory' ? '#1890ff' : '#000',
                                    }}
                                />
                                <Text
                                    strong
                                    style={{
                                        fontSize: '18px',
                                        color: activeTab === 'purchaseHistory' ? '#1890ff' : '#000',
                                    }}
                                >
                                    Danh sách đơn hàng đã mua
                                </Text>
                            </Space>
                        </Card>

                        {/* Profile Tab */}
                        <Card
                            hoverable
                            onClick={() => handleTabClick('profile')}
                            style={{
                                border: activeTab === 'profile' ? '1px solid #1890ff' : '1px solid #f0f0f0',
                                borderRadius: '8px',
                            }}
                        >
                            <Space align="center">
                                <InfoCircleOutlined
                                    style={{
                                        fontSize: '24px',
                                        color: activeTab === 'profile' ? '#1890ff' : '#000',
                                    }}
                                />
                                <Text
                                    strong
                                    style={{
                                        fontSize: '18px',
                                        color: activeTab === 'profile' ? '#1890ff' : '#000',
                                    }}
                                >
                                    Thông tin cá nhân và số địa chỉ
                                </Text>
                            </Space>
                        </Card>
                    </Space>
                </div>

                {/* Main Content */}
                <div
                    className="w-full"
                    style={{
                        flex: '1', // Main content takes the remaining space
                        overflow: 'hidden', // Prevents content overflow
                    }}
                >
                    {activeTab === 'purchaseHistory' && <PurchaseHistory />}
                    {activeTab === 'profile' && <Profile />}
                </div>
            </div>
        </div>
    );
}

export default Info;