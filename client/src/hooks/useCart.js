import { useState, useEffect } from 'react';
import { useSelector, useDispatch } from 'react-redux';
function useCart() {
    const cartItems = useSelector((state) => state.cartItems?.value)||[];
    const totalPrice = cartItems.reduce(
        (total, item) => total + Number(item.quantity) * Number(item.price - item.price * item.discount),
        0,
    );
    const totalQuantity = cartItems.reduce((total, item) => total + Number(item.quantity), 0);

    return { cartItems, totalPrice, totalQuantity };
}

export default useCart;
