import { convertLength } from '@mui/material/styles/cssUtils';
import { discountService } from '../../services';

import {getAllDiscounts,addCheckoutDiscount,clearCheckoutDiscount,discounts,savetDiscountsUser,getAllDiscountsUser,getAllDiscountsUserForShop,reduceDiscountsShop
} from './discountsSlice';


export const getAllDiscountByShopId= async (dispatch, shopId) => {
    let res = await discountService.getDiscountByShopId(shopId);
    dispatch(getAllDiscounts(res.data));
};

export const getAllDiscountByUser = async (dispatch) => {
    let res = await discountService.getDiscountByUser();
    dispatch(getAllDiscountsUser(res.data));
};


export const getDiscountByIdApi = async (dispatch, id) => {
    let res = await discountService.getDiscountById(id);
    dispatch(getOneDiscount(res.data));
}

export const checkDiscountCodeApi = async (dispatch, code) => {
    let res = await discountService.checkDiscountCode(code);
    dispatch(getOneDiscount(res.data));
}
export const saveDiscountUser = async (dispatch,code,dupArray) => {
    let res = await discountService.saveDiscount(code);
    dispatch(savetDiscountsUser(res.data));
}
export const loadDiscountUserForShop = (dispatch,dupArray=[]) => {
    dispatch(getAllDiscountsUserForShop(dupArray));
}
export const reduceDiscount = (dispatch,id) => {
    dispatch(reduceDiscountsShop(id));
}


// export const createDiscount = async (Discount, dispatch, navigate, DiscountList) => {
//     try {
//         const res = await discountService.postDiscount(Discount);
//         let Discounts = {...DiscountList};
//         Discounts.content = Discounts?.content || [];
//         Discounts.content = Array.from(Discounts.content).push(res.data);
//         dispatch(getPageDiscount(Discounts));
//         navigate('/seller/Discounts');
//     } catch (err) {
//         console.error(err?.message);
//     }
// };

// export const updateDiscount = async (id, Discount, dispatch, navigate, DiscountList) => {
//     try {
//         const res = await discountService.putDiscount(id, Discount);
//         let Discounts = {...DiscountList};
//         Discounts.content = Discounts?.content || [];
//         Discounts.content = Array.from(Discounts.content).map((item) => (item.id === res.data.id ? res.data : item));
//         dispatch(getPageDiscount(Discounts));
//         navigate('/seller/Discounts');
//     } catch (err) {
//         console.error(err?.message);
//     }
// };

// export const deleteDiscount = async (id, dispatch, navigate, DiscountList) => {
//     try {
//         const res = await discountService.deleteDiscount(id);
//         let Discounts = {...DiscountList};
//         console.log(Discounts);
//         Discounts.content = Discounts?.content || [];
//         Discounts.content = Array.from(Discounts.content).filter(item => item.id !== id);
//         dispatch(getPageDiscount(Discounts));
//         navigate('/seller/Discounts');
//     } catch (err) {
//         console.error(err?.message);
//     }
// };
