import { useEffect, useRef } from 'react';
import './Delivery.scss'
import { orderShopService } from '../../../services';
import Button from '@mui/material/Button';
import { useState } from 'react';
const Delivery = () => {
    const[data,setData] = useState([])
    useEffect(()=>{
      hanleCLickGet()
    },[])
    const hanleCLickGet= async()=>{
      let res = await orderShopService.getLogs(valRef.current.value)
    console.log(res.data.logs,"Data retrieved")

      setData(res.data)
    }

    const valRef = useRef()
    return (
        <div className="xMDeox">
       <div className = "">   
          <label for="default-search" class="mb-2 text-sm font-medium text-gray-900 sr-only dark:text-white">Search</label>
          <div class="relative">
              <div class="absolute inset-y-0 left-0 flex items-center pl-3 pointer-events-none">
                  <svg aria-hidden="true" class="w-5 h-5 text-gray-500 dark:text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"></path></svg>
              </div>
              <input ref={valRef} type="search" id="default-search" class="h-full block w-full p-4 pl-10 text-sm text-gray-900 border border-gray-300 rounded-lg bg-gray-50 focus:ring-blue-500 focus:border-blue-500 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500" placeholder="Mã vận đơn..."></input>
              <button onClick={hanleCLickGet} class="text-white absolute right-2.5 bottom-2.5 bg-blue-700 hover:bg-blue-800 focus:ring-4 focus:outline-none focus:ring-blue-300 font-medium rounded-lg text-sm px-4 py-2 dark:bg-blue-600 dark:hover:bg-blue-700 dark:focus:ring-blue-800">Search</button>
          </div>
      </div> 
      {data.length !== 0 ? (  <div className="">
            <div className="--tO6n pt-4" style={{}}>
              <div className="AJo2nP">
                <div className="cBtCqV">
                  <div className="yO9lYJ">
                    <svg
                      enableBackground="new 0 0 11 11"
                      viewBox="0 0 11 11"
                      x={0}
                      y={0}
                      className="shopee-svg-icon icon-arrow-left"
                    >
                      <g>
                        <path d="m8.5 11c-.1 0-.2 0-.3-.1l-6-5c-.1-.1-.2-.3-.2-.4s.1-.3.2-.4l6-5c .2-.2.5-.1.7.1s.1.5-.1.7l-5.5 4.6 5.5 4.6c.2.2.2.5.1.7-.1.1-.3.2-.4.2z" />
                      </g>
                    </svg>
                    <span>TRỞ LẠI</span>
                  </div>
                  <div className="w8MDQX">
                    <span>MÃ ĐƠN HÀNG. {data?.id}</span>
                    <span className="EkDKzu">|</span>
                    <span className="capx2D">Đơn hàng đã hoàn thành</span>
                  </div>
                </div>
                <div className="O2KPzo">
                  <div className="mn7INg xFSVYg" />
                  <div className="mn7INg EfbgJE" />
                </div>
                {/* <div className="bHBbO4">
                  <div className="stepper">
                    <div className="stepper__step stepper__step--finish">
                      <div className="stepper__step-icon stepper__step-icon--finish">
                      <img src='https://cdn-icons-png.flaticon.com/512/5220/5220625.png'></img>
                      </div>
                      <div className="stepper__step-text">Đơn hàng đã đặt</div>
                      <div className="stepper__step-date">00:19 03-06-2023</div>
                    </div>
                    <div className="stepper__step stepper__step--finish">
                      <div className="stepper__step-icon stepper__step-icon--finish">
                      <img src = 'https://cdn-icons-png.flaticon.com/512/2867/2867644.png'></img>
                      </div>
                      <div className="stepper__step-text">
                        Đã xác nhận thông tin thanh toán
                      </div>
                      <div className="stepper__step-date">00:49 03-06-2023</div>
                    </div>
                    <div className="stepper__step stepper__step--finish">
                    <div className="stepper__step-icon stepper__step-icon--finish">
                      <div>
                        <img width="244" height="244" src="https://cdn-icons-png.flaticon.com/512/8383/8383003.png" alt="delivery"/>
                      </div>
                      </div>
                      <div className="stepper__step-text">Đã giao cho ĐVVC</div>
                      <div className="stepper__step-date">16:23 03-06-2023</div>
                    </div>
                    <div className="stepper__step stepper__step--finish">
                      <div className="stepper__step-icon stepper__step-icon--finish">
                       <img width="244" height="244" src='https://cdn-icons-png.flaticon.com/512/6476/6476707.png'></img>
                      </div>
                      <div className="stepper__step-text">Đã nhận được hàng</div>
                      <div className="stepper__step-date">07:03 09-06-2023</div>
                    </div>
                    <div className="stepper__step stepper__step--pending">
                      <div className="stepper__step-icon stepper__step-icon--pending">
                        <img src='https://cdn-icons-png.flaticon.com/512/3898/3898127.png'></img>
                      </div>
                      <div className="stepper__step-text">Đánh giá</div>
                      <div className="stepper__step-date" />
                    </div>
                    <div className="stepper__line">
                      <div
                        className="stepper__line-background"
                        style={{ background: "rgb(224, 224, 224)" }}
                      />
                      <div
                        className="stepper__line-foreground"
                        style={{
                          width: "calc((100% - 140px) * 1)",
                          background: "rgb(45, 194, 88)"
                        }}
                      />
                    </div>
                  </div>
                </div>
                <div className="O2KPzo">
                  <div className="mn7INg xFSVYg" />
                  <div className="mn7INg EfbgJE" />
                </div> */}
             
                <div className="O2KPzo">
                  <div className="mn7INg xFSVYg" />
                  <div className="mn7INg EfbgJE" />
                </div>
           
                <div>
                  <div className="cmp831">
                    <div className="DM1xQK" />
                  </div>
                  <div className="mu8SJw">
                    <div className="_0Ihttg">
                      <div className="PW9gQm">Địa chỉ nhận hàng</div>
                      <div className="P9zS+I">
                        <div className="g5X7+k">
                          <div>
                            <div>PNTech Xpress</div>
                            <div>{data?.id}</div>
                          </div>
                        </div>
                      </div>
                    </div>
                    <div className="wMj1t2">
                      <div className="iWu+Gv">
                        <div className="KZmoHt">Họ Tên: {data?.fullName}</div>
                        <div className="AnJAa1">
                          <span>SDT: {data?.phone}</span>
                          <br />
                          Địa chỉ: {`${data?.address?.homeAdd}, ${data?.address?.ward}, ${data?.address?.district}, ${data?.address?.city}`}
                        </div>
                      </div>
                      <div className="ifE+r-">
                        <div>
                          {data?.logs?.map(item=>(
              <div className="rqUx-N cuJgPF">
              <div className="_4yfsbS" />
              <div className="JNurwA">
                <div className="flex w-[24px] relative">
                  <img
                    className="AXDO-g h-[24px]"
                    title="image"
                    src="https://cf.shopee.vn/file/delivered_parcel_active_3x"
                  />
                  <div className="B3MLEe">{item?.createdAt}</div>

                </div>
                <div className="u4VSsO ml-8 p-4">
                  {/* <p className="_0P1byN">Đã giao</p> */}
                  <p>
                  {item?.log}
                  {/* <span>. Người nhận hàng: Nguyên kim--</span>.{" "}
                  <span className="_5jk8NB" tabIndex={0}>
                    Xem hình ảnh giao hàng
                  </span> */}
                </p>
    </div>
  </div>
</div>
                          ))}
                        
                         
                        </div>
                        <span className="yG4MyU">Rút gọn</span>
                      </div>
                    </div>
                  </div>
                </div>
                <div>
                  <div>
                    <div className="mbaGbp">
                    {data?.orderItems?.map(item=>(
                            <div className="FbLutl">
                            <div>
                              <a
                                className="x7nENX"
                                href="/1KG-BỘT-NGŨ-CỐC-17-LOẠI-HẠT-MẸ-TÔM-(DINH-DƯỠNG-LỢI-SỮA-TĂNG-CÂN-GIẢM-CÂN-NỘI-TIẾT-TỐ-GRANOLA-BỘT-ĂN-DẶM)-i.74151382.1346266415"
                              >
                                <div />
                                <div className="aybVBK">
                                  <div className="_7uZf6Q flex gap-6">
                                    <div className='h-[45px] w-[45px] '>
                                      <img className="h-full w-full rounded-full" src={item?.product?.img}></img>
                                    </div>
                                    <div>
                                     
                                    </div>
                                    <div>
                                    <div className="iJlxsT">
                                          <span className="x5GTyN">
                                            {item?.product?.title}
                                          </span>
                                          <div className="_3F1-5M">x{item?.quantity}</div>
                                        </div>
                                    </div>
                                  </div>
                                </div>
                                <div className="_9UJGhr">
                                  <div className="rjqzk1">
                                    <span className="j2En5+">₫{item?.totalPrice}</span>
                                  </div>
                                </div>
                              </a>
                              <div className="_3VbM7s tjM0Qz">
                                <div className="SbXhyL">
                                  <div className="I2Mvul">
                                    Chính sách tiêu dùng sản phẩm
                                  </div>
                                  <div className="+EBiPA">
                                    <div className="WUExvN">
                                      <span>Xem chi tiết</span>
                                      <svg
                                        width={12}
                                        height={12}
                                        fill="none"
                                        xmlns="http://www.w3.org/2000/svg"
                                      >
                                        <path
                                          fillRule="evenodd"
                                          clipRule="evenodd"
                                          d="M9.293 6L4.146.854l.708-.708L10 5.293a1 1 0 010 1.414l-5.146 5.147-.708-.707L9.293 6z"
                                          fill="#000"
                                          fillOpacity=".54"
                                        />
                                      </svg>
                                    </div>
                                  </div>
                                </div>
                                <div className="_7w0r1r">
                                  <div className="IQfYqJ">
                                    <span>Cò hiệu lực</span>
                                    <span> (Hết hạn 09-06-2024)</span>
                                  </div>
                                </div>
                              </div>
                            </div>
                            <div className="Cde7Oe" />
                          </div>      
                    ))}
                     
                    </div>
                    <div className="RZJjTX">
                      <div className="TokOv1">
                        <div className="_8kMYJ3">
                          <span>Tổng tiền hàng</span>
                        </div>
                        <div className="CxyZBG">
                          <div>₫{data?.totalPrice}</div>
                        </div>
                      </div>
                      <div className="TokOv1">
                        <div className="_8kMYJ3">
                          <span>Phí vận chuyển</span>
                        </div>
                        <div className="CxyZBG">
                          <div>₫{data?.transportFee}</div>
                        </div>
                      </div>
                      <div className="TokOv1">
                        <div className="_8kMYJ3">
                          <span>Giảm giá phí vận chuyển</span>
                          <div
                            className="stardust-popover W97Kqg"
                            id="stardust-popover2"
                            tabIndex={0}
                          >
                            <div role="button" className="stardust-popover__target">
                              <div>
                                <svg
                                  width={16}
                                  height={16}
                                  fill="none"
                                  xmlns="http://www.w3.org/2000/svg"
                                >
                                  <path
                                    clipRule="evenodd"
                                    d="M8 15A7 7 0 108 1a7 7 0 000 14z"
                                    stroke="#000"
                                    strokeOpacity=".54"
                                  />
                                  <path
                                    fillRule="evenodd"
                                    clipRule="evenodd"
                                    d="M8 6a1 1 0 100-2 1 1 0 000 2zM7.25 7.932v3.636c0 .377.336.682.75.682s.75-.305.75-.682V7.932c0-.377-.336-.682-.75-.682s-.75.305-.75.682z"
                                    fill="#000"
                                    fillOpacity=".54"
                                  />
                                </svg>
                              </div>
                            </div>
                          </div>
                        </div>
                        <div className="CxyZBG">
                          <div>-₫0</div>
                        </div>
                      </div>
                      <div className="TokOv1 a59vwO">
                        <div className="_8kMYJ3 B6pCRN">
                          <span>Thành tiền</span>
                        </div>
                        <div className="CxyZBG">
                          <div className="_8ZGgbl">₫{data?.totalPrice}</div>
                        </div>
                      </div>
                    </div>
                 
                  </div>
                </div>
                <div className="TX9IwS">
                  <div className="O2KPzo">
                    <div className="mn7INg xFSVYg" />
                    <div className="mn7INg EfbgJE" />
                  </div>
                  <div className="TokOv1">
                    <div className="_8kMYJ3">
                      <span>
                        <span className="JMmT2C">
                          <span className="qyvpC4">
                            <svg
                              width={16}
                              height={17}
                              viewBox="0 0 253 263"
                              fill="none"
                              xmlns="http://www.w3.org/2000/svg"
                            >
                              <path
                                fillRule="evenodd"
                                clipRule="evenodd"
                                d="M126.5 0.389801C126.5 0.389801 82.61 27.8998 5.75 26.8598C5.08763 26.8507 4.43006 26.9733 3.81548 27.2205C3.20091 27.4677 2.64159 27.8346 2.17 28.2998C1.69998 28.7657 1.32713 29.3203 1.07307 29.9314C0.819019 30.5425 0.688805 31.198 0.689995 31.8598V106.97C0.687073 131.07 6.77532 154.78 18.3892 175.898C30.003 197.015 46.7657 214.855 67.12 227.76L118.47 260.28C120.872 261.802 123.657 262.61 126.5 262.61C129.343 262.61 132.128 261.802 134.53 260.28L185.88 227.73C206.234 214.825 222.997 196.985 234.611 175.868C246.225 154.75 252.313 131.04 252.31 106.94V31.8598C252.31 31.1973 252.178 30.5414 251.922 29.9303C251.667 29.3191 251.292 28.7649 250.82 28.2998C250.35 27.8358 249.792 27.4696 249.179 27.2225C248.566 26.9753 247.911 26.852 247.25 26.8598C170.39 27.8998 126.5 0.389801 126.5 0.389801Z"
                                fill="#ee4d2d"
                              />
                              <path
                                fillRule="evenodd"
                                clipRule="evenodd"
                                d="M207.7 149.66L119.61 107.03C116.386 105.472 113.914 102.697 112.736 99.3154C111.558 95.9342 111.772 92.2235 113.33 88.9998C114.888 85.7761 117.663 83.3034 121.044 82.1257C124.426 80.948 128.136 81.1617 131.36 82.7198L215.43 123.38C215.7 120.38 215.85 117.38 215.85 114.31V61.0298C215.848 60.5592 215.753 60.0936 215.57 59.6598C215.393 59.2232 215.128 58.8281 214.79 58.4998C214.457 58.1705 214.063 57.909 213.63 57.7298C213.194 57.5576 212.729 57.4727 212.26 57.4798C157.69 58.2298 126.5 38.6798 126.5 38.6798C126.5 38.6798 95.31 58.2298 40.71 57.4798C40.2401 57.4732 39.7735 57.5602 39.3376 57.7357C38.9017 57.9113 38.5051 58.1719 38.1709 58.5023C37.8367 58.8328 37.5717 59.2264 37.3913 59.6604C37.2108 60.0943 37.1186 60.5599 37.12 61.0298V108.03L118.84 147.57C121.591 148.902 123.808 151.128 125.129 153.884C126.45 156.64 126.797 159.762 126.113 162.741C125.429 165.72 123.755 168.378 121.363 170.282C118.972 172.185 116.006 173.221 112.95 173.22C110.919 173.221 108.915 172.76 107.09 171.87L40.24 139.48C46.6407 164.573 62.3785 186.277 84.24 200.16L124.49 225.7C125.061 226.053 125.719 226.24 126.39 226.24C127.061 226.24 127.719 226.053 128.29 225.7L168.57 200.16C187.187 188.399 201.464 170.892 209.24 150.29C208.715 150.11 208.2 149.9 207.7 149.66Z"
                                fill="#fff"
                              />
                            </svg>
                          </span>
                        </span>
                        <span className="_3Nh1BH ml-2">Thanh toán online</span>
                      </span>
                    </div>
                  
                  </div>
                </div>
              </div>
            </div>
          </div>):(<div className='flex justify-center mt-20 bg-none p-6'><img height='270' width='270' src='https://cdn-icons-png.flaticon.com/512/945/945414.png'></img></div>)}
        
        </div>
        );
};
export default Delivery;

