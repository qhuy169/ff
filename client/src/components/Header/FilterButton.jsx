
import { useEffect, useState } from 'react';
import { Cart, CaretDownFill, Search, ChevronLeft } from 'react-bootstrap-icons';
import { getAllProductApi } from '../../redux/product/productsApi';
import { useDispatch } from 'react-redux';

import useLocationForm from '../../components/LocationForm/useLocationForm';
function FilterButton() {
    const MENU_ITEMS = [
        {
            title: 'Tỉnh An Giang',
            children: {
                data: [
                    {
                        title: 'Huyện An Phú',
                        children: {
                            data: [
                                {
                                    title: 'Phú hữu',
                                },
                                {
                                    title: 'Phú hữu2',
                                },
                                {
                                    title: 'Phú hữu3',
                                },
                                {
                                    title: 'Phú hữu4',
                                },
                                {
                                    title: 'Phú hữu5',
                                },
                                {
                                    title: 'Phú hữu6',
                                },
                                {
                                    title: 'Phú hữu7',
                                },
                                {
                                    title: 'Phú hữu8',
                                },
                                {
                                    title: 'Phú hữu9',
                                },
                                {
                                    title: 'Phú hữu',
                                },
                                {
                                    title: 'Phú hữu2',
                                },
                                {
                                    title: 'Phú hữu3',
                                },
                                {
                                    title: 'Phú hữu',
                                },
                                {
                                    title: 'Phú hữu2',
                                },
                                {
                                    title: 'Phú hữu3',
                                },
                            ],
                        },
                    },
                    {
                        title: 'Huyện Thoại sơn',
                        children: {
                            data: [
                                {
                                    title: 'xãthoại sơn',
                                },
                                {
                                    title: 'xã thoại sơn2',
                                },
                                {
                                    title: 'xã thoại sơn3',
                                },
                            ],
                        },
                    },
                ],
            },
        },
        {
            title: 'Tỉnh Long Xuyên',
            children: {
                data: [
                    {
                        title: 'Huyện Cái dầu',
                        children: {
                            data: [
                                {
                                    title: 'xã cái dầu',
                                },
                                {
                                    title: 'xã cái dầu2',
                                },
                                {
                                    title: 'xã cái dầu3',
                                },
                            ],
                        },
                    },
                    {
                        title: 'Huyện Cái dầu2',
                        children: {
                            data: [
                                {
                                    title: 'xã cai dầu',
                                },
                                {
                                    title: 'xã cai dầu2',
                                },
                                {
                                    title: 'xã cai dầu3',
                                },
                            ],
                        },
                    },
                ],
            },
        },
        {
            title: 'Bà rịa vũng tàu',

            children: {
                data: [
                    {
                        title: 'Tự trị',
                        children: {
                            data: [
                                {
                                    title: 'Lý liên kiệt',
                                },
                                {
                                    title: 'Lý liên kiệt2',
                                },
                                {
                                    title: 'Lý liên kiệt3',
                                },
                            ],
                        },
                    },
                    {
                        title: 'Tự trị2',
                        children: {
                            data: [
                                {
                                    title: 'xãthoại sơn',
                                },
                                {
                                    title: 'xã thoại sơn2',
                                },
                                {
                                    title: 'xã thoại sơn3',
                                },
                            ],
                        },
                    },
                ],
            },
        },
        {
            title: 'Tỉnh Long An',

            children: {
                data: [
                    {
                        title: 'Huyện An Phú',
                        children: {
                            data: [
                                {
                                    title: 'Phú hữu',
                                },
                                {
                                    title: 'Phú hữu2',
                                },
                                {
                                    title: 'Phú hữu3',
                                },
                            ],
                        },
                    },
                    {
                        title: 'Huyện Thoại sơn',
                        children: {
                            data: [
                                {
                                    title: 'xãthoại sơn',
                                },
                                {
                                    title: 'xã thoại sơn2',
                                },
                                {
                                    title: 'xã thoại sơn3',
                                },
                            ],
                        },
                    },
                ],
            },
        },
        {
            title: 'Tỉnh Bình Định',

            children: {
                data: [
                    {
                        title: 'Huyện An Phú',
                        children: {
                            data: [
                                {
                                    title: 'Phú hữu',
                                },
                                {
                                    title: 'Phú hữu2',
                                },
                                {
                                    title: 'Phú hữu3',
                                },
                            ],
                        },
                    },
                    {
                        title: 'Huyện Thoại sơn',
                        children: {
                            data: [
                                {
                                    title: 'xãthoại sơn',
                                },
                                {
                                    title: 'xã thoại sơn2',
                                },
                                {
                                    title: 'xã thoại sơn3',
                                },
                            ],
                        },
                    },
                ],
            },
        },
        {
            title: 'Tỉnh Đắc Lắc',

            children: {
                data: [
                    {
                        title: 'Huyện An Phú',
                        children: {
                            data: [
                                {
                                    title: 'Phú hữu',
                                },
                                {
                                    title: 'Phú hữu2',
                                },
                                {
                                    title: 'Phú hữu3',
                                },
                            ],
                        },
                    },
                    {
                        title: 'Huyện Thoại sơn',
                        children: {
                            data: [
                                {
                                    title: 'xãthoại sơn',
                                },
                                {
                                    title: 'xã thoại sơn2',
                                },
                                {
                                    title: 'xã thoại sơn3',
                                },
                            ],
                        },
                    },
                ],
            },
        },
        {
            title: 'Tỉnh Bắc Kan',
            children: {
                data: [
                    {
                        title: 'Huyện Cái dầu',
                        children: {
                            data: [
                                {
                                    title: 'xã cái dầu',
                                },
                                {
                                    title: 'xã cái dầu2',
                                },
                                {
                                    title: 'xã cái dầu3',
                                },
                            ],
                        },
                    },
                    {
                        title: 'Huyện Cái dầu2',
                        children: {
                            data: [
                                {
                                    title: 'xã cai dầu',
                                },
                                {
                                    title: 'xã cai dầu2',
                                },
                                {
                                    title: 'xã cai dầu3',
                                },
                            ],
                        },
                    },
                ],
            },
        },
        {
            title: 'Tỉnh Hòa Bình',
            children: {
                data: [
                    {
                        title: 'Huyện Cái dầu',
                        children: {
                            data: [
                                {
                                    title: 'xã cái dầu',
                                },
                                {
                                    title: 'xã cái dầu2',
                                },
                                {
                                    title: 'xã cái dầu3',
                                },
                            ],
                        },
                    },
                    {
                        title: 'Huyện Cái dầu2',
                        children: {
                            data: [
                                {
                                    title: 'xã cai dầu',
                                },
                                {
                                    title: 'xã cai dầu2',
                                },
                                {
                                    title: 'xã cai dầu3',
                                },
                            ],
                        },
                    },
                ],
            },
        },
        {
            title: 'Tỉnh Lạng Sơn',
            children: {
                data: [
                    {
                        title: 'Huyện Cái dầu',
                        children: {
                            data: [
                                {
                                    title: 'xã cái dầu',
                                },
                                {
                                    title: 'xã cái dầu2',
                                },
                                {
                                    title: 'xã cái dầu3',
                                },
                            ],
                        },
                    },
                    {
                        title: 'Huyện Cái dầu2',
                        children: {
                            data: [
                                {
                                    title: 'xã cai dầu',
                                },
                                {
                                    title: 'xã cai dầu2',
                                },
                                {
                                    title: 'xã cai dầu3',
                                },
                            ],
                        },
                    },
                ],
            },
        },
        {
            title: 'Tỉnh Phú Thọ',
            children: {
                data: [
                    {
                        title: 'Huyện Cái dầu',
                        children: {
                            data: [
                                {
                                    title: 'xã cái dầu',
                                },
                                {
                                    title: 'xã cái dầu2',
                                },
                                {
                                    title: 'xã cái dầu3',
                                },
                            ],
                        },
                    },
                    {
                        title: 'Huyện Cái dầu2',
                        children: {
                            data: [
                                {
                                    title: 'xã cai dầu',
                                },
                                {
                                    title: 'xã cai dầu2',
                                },
                                {
                                    title: 'xã cai dầu3',
                                },
                            ],
                        },
                    },
                ],
            },
        },
        {
            title: 'Tỉnh Sơn La',
            children: {
                data: [
                    {
                        title: 'Huyện Cái dầu',
                        children: {
                            data: [
                                {
                                    title: 'xã cái dầu',
                                },
                                {
                                    title: 'xã cái dầu2',
                                },
                                {
                                    title: 'xã cái dầu3',
                                },
                            ],
                        },
                    },
                    {
                        title: 'Huyện Cái dầu2',
                        children: {
                            data: [
                                {
                                    title: 'xã cai dầu',
                                },
                                {
                                    title: 'xã cai dầu2',
                                },
                                {
                                    title: 'xã cai dầu3',
                                },
                            ],
                        },
                    },
                ],
            },
        },
        {
            title: 'Tỉnh Long Thành',
            children: {
                data: [
                    {
                        title: 'Huyện Cái dầu',
                        children: {
                            data: [
                                {
                                    title: 'xã cái dầu',
                                },
                                {
                                    title: 'xã cái dầu2',
                                },
                                {
                                    title: 'xã cái dầu3',
                                },
                            ],
                        },
                    },
                    {
                        title: 'Huyện Cái dầu2',
                        children: {
                            data: [
                                {
                                    title: 'xã cai dầu',
                                },
                                {
                                    title: 'xã cai dầu2',
                                },
                                {
                                    title: 'xã cai dầu3',
                                },
                            ],
                        },
                    },
                ],
            },
        },
        {
            title: 'Tỉnh Thạnh Hóa',
            children: {
                data: [
                    {
                        title: 'Huyện Cái dầu',
                        children: {
                            data: [
                                {
                                    title: 'xã cái dầu',
                                },
                                {
                                    title: 'xã cái dầu2',
                                },
                                {
                                    title: 'xã cái dầu3',
                                },
                            ],
                        },
                    },
                    {
                        title: 'Huyện Cái dầu2',
                        children: {
                            data: [
                                {
                                    title: 'xã cai dầu',
                                },
                                {
                                    title: 'xã cai dầu2',
                                },
                                {
                                    title: 'xã cai dầu3',
                                },
                            ],
                        },
                    },
                ],
            },
        },
        {
            title: 'Tỉnh Mộc Hóa',
            children: {
                data: [
                    {
                        title: 'Huyện Cái dầu',
                        children: {
                            data: [
                                {
                                    title: 'xã cái dầu',
                                },
                                {
                                    title: 'xã cái dầu2',
                                },
                                {
                                    title: 'xã cái dầu3',
                                },
                            ],
                        },
                    },
                    {
                        title: 'Huyện Cái dầu2',
                        children: {
                            data: [
                                {
                                    title: 'xã cai dầu',
                                },
                                {
                                    title: 'xã cai dầu2',
                                },
                                {
                                    title: 'xã cai dầu3',
                                },
                            ],
                        },
                    },
                ],
            },
        },
        {
            title: 'Tỉnh TPHCM',
            children: {
                data: [
                    {
                        title: 'Huyện Cái dầu',
                        children: {
                            data: [
                                {
                                    title: 'xã cái dầu',
                                },
                                {
                                    title: 'xã cái dầu2',
                                },
                                {
                                    title: 'xã cái dầu3',
                                },
                            ],
                        },
                    },
                    {
                        title: 'Huyện Cái dầu2',
                        children: {
                            data: [
                                {
                                    title: 'xã cai dầu',
                                },
                                {
                                    title: 'xã cai dầu2',
                                },
                                {
                                    title: 'xã cai dầu3',
                                },
                            ],
                        },
                    },
                ],
            },
        },
        {
            title: 'Tỉnh Hà Nội',
            children: {
                data: [
                    {
                        title: 'Huyện Cái dầu',
                        children: {
                            data: [
                                {
                                    title: 'xã cái dầu',
                                },
                                {
                                    title: 'xã cái dầu2',
                                },
                                {
                                    title: 'xã cái dầu3',
                                },
                            ],
                        },
                    },
                    {
                        title: 'Huyện Cái dầu2',
                        children: {
                            data: [
                                {
                                    title: 'xã cai dầu',
                                },
                                {
                                    title: 'xã cai dầu2',
                                },
                                {
                                    title: 'xã cai dầu3',
                                },
                            ],
                        },
                    },
                ],
            },
        },
    ];
    const [hide, setHide] = useState(false);
    const [history, setHistory] = useState([{ data: MENU_ITEMS }]);
    const [clickXa, setClickXa] = useState(false);

    const { state, onCitySelect, onDistrictSelect, onWardSelect, onSubmit } = useLocationForm(false);
    const { cityOptions, districtOptions, wardOptions, selectedCity, selectedDistrict, selectedWard } = state;
    //console.log(cityOptions)
    //setItem custom
    // useEffect(()=>{
    //     const originalSetItem = localStorage.setItem;
    //     localStorage.setItem = function(key, value) {
    //     const event = new Event('itemInserted');
    //     event.value = value; // Optional..
    //     event.key = key; // Optional..
    //     document.dispatchEvent(event);
    //     originalSetItem.apply(this, arguments);
    //     };
    // },[])

    let locationed = JSON.parse(localStorage.getItem('locationed'));
    // if(locationed){
    //     locationed.reverse().toString()
    // }
    const dispatch = useDispatch();
    const [valueLocation, setValueLocation] = useState([]);
    let location;
    if (valueLocation.length === 0) {
        location = 'Quý khách vui lòng cho biết Địa Chỉ Nhận Hàng để biết chính xác thời gian giao hàng';
    } else {
        if (valueLocation.length === 1) {
            location = `Chọn Quận, huyện tại ${valueLocation[0]}`;
        }
        if (valueLocation.length === 2) {
            location = `Chọn Phường xã tại ${valueLocation[1]}, ${valueLocation[0]}`;
        }
    }

    let current = history[history.length - 1];
    const handleCloseModal = () => {
        console.log(hide);
        setHide(!hide);
        setHistory((prev) => prev.slice(0, 1));
        setValueLocation([]);
    };
    const onChange = (item) => {
        localStorage.setItem('locationed', JSON.stringify([...valueLocation, item.title]));
        handleCloseModal();
        setClickXa(!clickXa);
    };
    const hanleremovelocation = () => {
        localStorage.setItem('locationed', JSON.stringify([]));
        setValueLocation([]);
        setClickXa(!clickXa);
    };
    const handleBack = () => {
        setHistory((prev) => {
            return prev.slice(0, history.length - 1);
        });
        setValueLocation(valueLocation.slice(0, valueLocation.length - 1));
    };
    const renderItems = () => {
        return current?.data.map((item, index) => {
            const isParent = !!item.children;

            return (
                <li
                    className="cursor-pointer hover:bg-blue-100 rounded-md col-span-1 h-[37px] border-b-1 p-3"
                    key={index}
                    onClick={() => {
                        setValueLocation([...valueLocation, item.title]);
                        if (isParent) {
                            setHistory((prev) => {
                                return [...prev, item.children];
                            });
                        } else {
                            onChange(item);
                        }
                    }}
                >
                    {item.title}
                </li>
            );
        });
    };
    ///Click vào xã sẽ render ra dữ liệu
    let locationChoice = JSON.parse(localStorage.getItem('locationed'));
    let province;
    if (locationChoice) {
        if (locationChoice?.length > 0) {
            province = locationChoice[0];
            // setProvince(locationChoice[0])
        } else {
            // setProvince('')
            province = '';
            locationChoice = [];
        }
    }

    useEffect(() => {
        if (locationChoice?.length === 0) {
            getAllProductApi(dispatch);
        } else {
            getAllProductApi(dispatch, {location: province});
        }
    }, [clickXa]);
    return (
        <div>
            <button className="flex bg-gray-300 px-4 py-1 items-center rounded-lg text-lg" onClick={handleCloseModal}>
                <div>
                    <span className="mr-2 line-clamp-1 max-w-[200px] w-max">
                      Lọc theo: {locationed?.length > 1 ? locationed : 'Mọi tỉnh thành'}
                    </span>
                </div>
             
            </button>
            {hide && (
                <div>
                    <div
                        id="defaultModal"
                        tabindex="-1"
                        class=" overflow-y-auto overflow-x-hidden fixed top-0 right-0 left-0 z-50 w-full md:inset-0 h-modal md:h-full justify-center items-center flex"
                        aria-modal="true"
                        role="dialog"
                    >
                        <div className="fixed top-0 right-0 bottom-0 left-0 z-10 bg-black opacity-30 m-auto"></div>
                        <div class="relative p-1 w-full max-w-3xl h-full md:h-auto z-20 rounded-lg">
                            <div class="relative bg-white rounded-lg shadow">
                                <div class="flex flex-col  items-start p-4 rounded-t border-b bg-green-300 ">
                                    <div className="flex py-2 gap-x-3  justify-between w-full">
                                        {valueLocation?.length >= 1 && (
                                            <ChevronLeft
                                                className="cursor-pointer text-3xl text-white"
                                                onClick={handleBack}
                                            ></ChevronLeft>
                                        )}

                                        <h3 class="text-xl font-bold text-white ">
                                            {' '}
                                            {locationed !== null && valueLocation?.length < 1 && locationed?.length > 0
                                                ? `Địa chỉ đã chọn: ${locationed}`
                                                : location}
                                        </h3>

                                        <button
                                            type="button"
                                            class=" text-gray-600 bg-transparent hover:bg-gray-200 hover:text-gray-300 rounded-lg text-sm px-3 py-2 ml-auto inline-flex items-center  dark:hover:bg-gray-600 dark:hover:text-white"
                                            data-modal-toggle="defaultModal"
                                            onClick={handleCloseModal}
                                        >
                                            <svg
                                                aria-hidden="true"
                                                class="w-5 h-5"
                                                fill="currentColor"
                                                viewBox="0 0 20 20"
                                                xmlns="http://www.w3.org/2000/svg"
                                            >
                                                <path
                                                    fill-rule="evenodd"
                                                    d="M4.293 4.293a1 1 0 011.414 0L10 8.586l4.293-4.293a1 1 0 111.414 1.414L11.414 10l4.293 4.293a1 1 0 01-1.414 1.414L10 11.414l-4.293 4.293a1 1 0 01-1.414-1.414L8.586 10 4.293 5.707a1 1 0 010-1.414z"
                                                    clip-rule="evenodd"
                                                ></path>
                                            </svg>
                                            <span class="sr-only">Close modal</span>
                                        </button>
                                    </div>
                                    <span
                                        className="text-[11px] mb-2 w-[59px] text-white cursor-pointer pb-2"
                                        onClick={hanleremovelocation}
                                    >
                                        {locationed !== null && valueLocation?.length < 1 && locationed?.length > 0
                                            ? 'Xóa địa chỉ'
                                            : ''}
                                    </span>
                                    <div className="w-full relative">
                                        <Search className="z-10 absolute left-[2%] top-[27%] font-bold text-2xl"></Search>
                                        <input
                                            className="w-full  px-[30px] py-[8px] rounded-md outline-none leading-[16px] text-xl"
                                            placeholder="Nhập tỉnh thành"
                                        ></input>
                                    </div>
                                </div>

                                <div class="p-6 space-y-6">
                                    {/* {current.data.map((item, index) => {
                                                const isParent = !!item.children
                                                return (
                                                    <div onClick={()=>{
                                                        if(isParent) {
                                                            setHistory((prev) => {
                                                                return [...prev, item.children]
                                                            })
                                                        }else{'
                                                            
                                                        }
                                                    }}>{item.title}</div>
                                                   
                                                )
                                            })} */}
                                    {valueLocation.length < 1 && (
                                        <h2 className="text-center font-semibold text-2xl">
                                            Hoặc chọn tỉnh, thành phố
                                        </h2>
                                    )}

                                    <ul className=" h-[270px] overflow-y-scroll grid gap-x-10 gap-y-6 grid-cols-2">
                                        {renderItems()}
                                    </ul>
                                </div>

                                <div class="flex items-center p-1 space-x-2 rounded-b border-t border-gray-300 "></div>
                            </div>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
}

export default FilterButton;
