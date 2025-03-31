import { useState } from 'react';
import { Link } from 'react-router-dom';
import { XCircle } from 'react-bootstrap-icons';
import { numberWithCommas } from '~/utils';
import { useDispatch } from 'react-redux';
import { removeItem, updateItem } from '~/redux/shopping-cart/cartItemsSlide';
import { useSelector } from 'react-redux';
import { CounterQuantity } from '~/components/Selector';
import { getAllDiscountByShopId,getAllDiscountByUser } from '../../../redux/discount/discountsApi';
import { useEffect } from 'react';
function ProductItem(props) {
    const cartItems = useSelector((state) => state.cartItems.value);
    const dispatch = useDispatch();
    const removeCartItem = () => {
        cartItems.forEach((item) => {
            if (item.slug === props.slug && item.color === props.color) {
                dispatch(removeItem(item));
            }
        });
    };
    const updateCartItem = (value) => {
        cartItems.forEach((item) => {
            if (item.slug === props.slug && item.color === props.color) {
                dispatch(updateItem({ ...item, quantity: value }));
            }
        });
    };

    return (
        <div className="flex justify-between my-8 border-b pb-4 relative">
           
            <div className="flex flex-col items-center justify-center w-28 gap-4">
                <img src={props.img} alt="" />
                
            </div>
            <div className="flex-grow">
                <div className="flex justify-between">
                    <Link to={`/${props.category}/${props.slug}`} className="font-semibold">
                        {props.title}
                    </Link>
                    <div>
                        <p className="text-red-500">{numberWithCommas(props.price)}₫</p>
                        <p className="line-through my-1">{numberWithCommas(props.discount != 0 ? (`${props.originPrice}₫`) : '')}</p>
                    </div>
                </div>
                <div className="flex justify-between items-center">
                    
                
                    
                    <CounterQuantity onChange={(value) => updateCartItem(value)} value={props.quantity} />
                    <div className="text-lg text-gray-600">
                        <button type="button" onClick={() => removeCartItem()} className="text-gray-400 border border-solid p-2 border-orange-200">
                            <i>
                                <XCircle />
                            </i>
                            &nbsp;Xóa
                        </button>
                    </div>
                </div>
              
            </div>
    
        </div>
    );
}

export default ProductItem;
