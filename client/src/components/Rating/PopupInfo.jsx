function PopupInfo(props) {
    const handleSubmitForm = (e) => {
        e.preventDefault();
        const name = document.getElementById('name').value;
        const email = document.getElementById('email').value;
        const sdt = document.getElementById('sdt').value;
        const sex = document.getElementsByName('sex');
        let sexFor;

        for (let i = 0; i < sex.length; i++) {
            if (sex[i].checked) sexFor = sex[i].value;
        }

        const data = { username: name, email, phone: sdt, sex: sexFor };
        props.onSubmit(data)
    };
    return (
        <div>
            <div
                id="defaultModal"
                tabIndex="-1"
                className=" overflow-y-auto overflow-x-hidden fixed top-0 right-0 left-0 z-50 w-full md:inset-0 h-modal md:h-full justify-center items-center flex"
                aria-modal="true"
                role="dialog"
            >
                <div className="fixed top-0 right-0 bottom-0 left-0 z-10 bg-black opacity-30 m-auto"></div>
                <div className="relative p-1 w-full max-w-3xl h-full md:h-auto z-20">
                    <div className="relative bg-white rounded-lg shadow">
                        <div className="flex flex-col  items-start p-4 rounded-t border-b bg-blue-400">
                            <div className="flex py-2 gap-x-3 mb-2 justify-between w-full">
                                <h3 className="text-xl font-bold text-white "> Thông tin người gửi</h3>
                                <button type="button" data-modal-toggle="defaultModal" onClick={props.onClose}>
                                    <svg
                                        aria-hidden="true"
                                        weight="1em"
                                        height="1em"
                                        fill="currentColor"
                                        viewBox="0 0 20 20"
                                        xmlns="http://www.w3.org/2000/svg"
                                    >
                                        <path
                                            fillRule="evenodd"
                                            d="M4.293 4.293a1 1 0 011.414 0L10 8.586l4.293-4.293a1 1 0 111.414 1.414L11.414 10l4.293 4.293a1 1 0 01-1.414 1.414L10 11.414l-4.293 4.293a1 1 0 01-1.414-1.414L8.586 10 4.293 5.707a1 1 0 010-1.414z"
                                            clipRule="evenodd"
                                        ></path>
                                    </svg>
                                    <span className="sr-only">Close modal</span>
                                </button>
                            </div>
                            <div className="w-full flex gap-3 items-center">
                                <input className="p-2" type="radio" name="sex" id="Anh" value="Anh"></input>
                                <label htmlFor="Anh" className="ml-2 text-white">
                                    Anh
                                </label>
                                <input className="p-2" type="radio" name="sex" id="Chị" value="Chị"></input>
                                <label htmlFor="Chị" className="ml-2 text-white">
                                    Chị
                                </label>
                            </div>
                        </div>

                        <form className="p-6 space-y-6" onSubmit={handleSubmitForm}>
                            <div className="flex flex-col gap-6">
                                <input
                                    name="name"
                                    type="text"
                                    className="w-full px-[5px] outline-none border-1  py-[10px] leading-[16px] text-xl"
                                    placeholder="Họ tên (bắt buộc)"
                                    id="name"
                                    required
                                ></input>
                                <input
                                    name="email"
                                    type="text"
                                    className="w-full px-[5px] outline-none border-1  py-[10px] leading-[16px] text-xl"
                                    placeholder="Email (để nhận phản hồi qua email)"
                                    id="email"
                                    required
                                ></input>
                                <input
                                    name="sdt"
                                    type="text"
                                    className="w-full px-[5px] outline-none border-1  py-[10px] leading-[16px] text-xl"
                                    placeholder="Sdt"
                                    id="sdt"
                                    required
                                ></input>
                            </div>

                            <div className="flex items-center justify-end p-1 space-x-2 pr-7 py-2 rounded-b border-t border-gray-300 ">
                                <button
                                    type="submit"
                                    className="cursor-pointer text-white px-7 py-3 bg-blue-400 rounded-md"
                                >
                                    Gửi
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
            {/* )} */}
        </div>
    );
}

export default PopupInfo;
