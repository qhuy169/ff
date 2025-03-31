import { useState } from 'react';
import PurchaseHistory from './PurchaseHistory';
import Welcome from './Welcome';
import Profile from './Profile';
import clsx from 'clsx';
import { InfoCircle, List } from 'react-bootstrap-icons';
function Info() {
    const [tab, setTab] = useState(false);
    const [tabInfo,setTabInfo]=useState(false);
    const activeTabStyle = 'rounded-full border border-blue-300 border-solid text-blue-400';
    return (
        <div className="bg-white">
            <div className="w-full max-w-[1200px] mx-auto py-8 flex gap-10">
                <div className="flex flex-col">
                <Welcome />
                    <div
                        onClick={() => setTab(!tab)}
                        className={clsx(
                            'flex w-full items-center my-2 p-4 cursor-pointer',
                            tab && activeTabStyle,
                        )}
                    >
                        <div className="w-12 h-12">
                           
                        </div>
                        <List className='text-xl'></List>
                        <p className="text-2xl font-medium ml-4">Danh sách đơn hàng đã mua</p>

                    </div>
                        <div className="">{tab ? <PurchaseHistory /> : ''}</div>
                    <div
                        onClick={() => setTabInfo(!tabInfo)}
                        className={clsx(
                            'flex w-full items-center my-2 p-4 cursor-pointer',
                           tabInfo && activeTabStyle,
                        )}
                    >
                        <div className="w-12 h-12 items-center">
                        </div>
                        <InfoCircle></InfoCircle>
                        <p className="text-2xl font-medium ml-4">Thông tin cá nhân và số địa chỉ</p>
                    </div>
                </div>
                <div className="">{tabInfo  ?  <Profile />: ''}</div>
            </div>
        </div>
    );
}

export default Info;
