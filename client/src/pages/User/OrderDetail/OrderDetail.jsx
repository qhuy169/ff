import './orderdetail.scss'
import { getOrdersById } from '../../../redux/order/ordersApi'
import { useEffect } from 'react'
import { useSelector, useDispatch } from 'react-redux';
import {  useParams } from 'react-router-dom';
import { EPayment } from '../../../utils';
function orderDetail(){
    const {id} = useParams()
    const dispatch = useDispatch()
    useEffect(()=>{
        getOrdersById(dispatch,id)
    },[])
    const orderDetail = useSelector(state => state.orders?.orderDetail?.data)
    console.log("getorderDetail",orderDetail)
    return(
        <div className="container-fluid">

        <div className="container">   
        <div className="grid grid-cols-12 gap-8">
            <div className="col-span-12">
                <div className="text-center py-3">
                    <h1 className="text-3xl mb-0"><a href="#" className="text-muted"></a> Order #{orderDetail.id}</h1>
                </div>
                <div className="card mb-4 ">
                    <div className="card-body p-12">
                    <div className="mb-3 flex justify-between">
                        <div>
                        <span className="me-3">{orderDetail?.createAt}</span>

                        </div>
                    
                    </div>
                    <div className="flex flex-col gap-5">
                        <div>
                        <div>
                            <div>
                            <div className="flex gap-6 mb-2 justify-between">
                                <div className='flex'>
                                    <div className="flex-shrink-0">
                                    <img src="https://via.placeholder.com/280x280/87CEFA/000000" alt="" width="35" className="img-fluid"></img>
                                    </div>
                                    <div className="flex-lg-grow-1 ms-3 ml-4">
                                        <h6 className="small mb-0"><a href="#" className="text-reset">Wireless Headphones with Noise Cancellation Tru Bass Bluetooth HiFi</a></h6>
                                        <span className="small">Color: Black</span>
                                    </div>
                                </div>
                               
                                <span className='align-middle'>1</span>
                                <span className="text-end">79.9989821311329898đ</span>
                            </div>
                            </div>
                           
                        </div>
                   
                        </div>
                        <div className='flex flex-col gap-5'>
                        <div className='flex justify-between'>
                            <span colspan="2">Subtotal</span>
                            <span className="text-end">$159,98</span>
                        </div>
                        <div  className='flex justify-between'>
                            <span colspan="2">Phí vận chuyển</span>
                            <span className="text-end">{orderDetail?.transportFee}đ</span>
                        </div>
                        <div  className='flex justify-between'>
                            <span colspan="2">Discount (Code: NEWYEAR)</span>
                            <span className="text-danger text-end">{orderDetail?.totalPriceDiscount}</span>
                        </div>
                        <div  className='flex justify-between'>
                            <span colspan="2">TOTAL</span>
                            <span className="text-end">{orderDetail?.totalPrice}</span>
                        </div>
                        </div>
                    </div>
                    </div>
                </div>

                
            </div>
            <div className="col-span-12 grid gap-8 grid-cols-2">
            <div className="card mb-4 col-span-1">
                    <div className="card-body">
                    <div className="row p-12">
                        <div className="col-lg-6 mb-8">
                        <h3 className="h6">Phương thức thanh toán</h3>
                       
                        <p> {EPayment.getNameFromIndex(orderDetail.payment)} <br></br>
                        Tổng: {orderDetail?.totalPrice}đ </p>
                        </div>
                        <div className="col-lg-6">
                    
                        <address>
                            <strong>Trạng thái</strong><br></br>
                                ${orderDetail?.status}
                            <br></br>
                           
                        </address>
                        </div>
                    </div>
                    </div>
                </div>
      
                <div className="card mb-4 col-span-1">
                    <div className="card-body">
                    <div className="row p-12">
                        <div className="col-lg-6">
                        <h2>Name: {orderDetail?.user?.fullName}</h2>
                        <h2>Email: {orderDetail?.user?.email}</h2>
                        <h3 className="h6 mt-12">Billing address</h3>
                        <address>
                            
                            {`${orderDetail?.address?.homeAdd}, ${orderDetail?.address?.ward}, ${orderDetail?.address?.district}, ${orderDetail?.address?.city}`}
                            <br></br>
                            <span className='text-2xl' title="Phone">P: {orderDetail.phone}</span>
                        </address>
                        </div>
                    </div>
                    </div>
                </div>
            </div>
        </div>
        </div>
  </div>
    )
}
export default orderDetail