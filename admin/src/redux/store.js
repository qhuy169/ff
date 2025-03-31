// import orderReducer from './order/ordersSlice';

import { configureStore} from '@reduxjs/toolkit';
import orderSlice from './order/ordersSlice';
import brandSlice from './brand/brandSlice';
import categoriesSlice from './category/categoriesSlice';
import commentSlice from './comment/commentSlice';
import productSlice from './product/productsSlice';
import reviewSlice from './review/reviewSlice';
import shopSlice from './shop/shopSlice';
import userSlice from './user/userSlice';
import showModalSlice from './modal/showModalSlice';
import notificationsSlice from './notification/notificationsSlice';
//khoi tao store
export const store = configureStore({
    reducer: {   
        orders: orderSlice,
        brands: brandSlice,
        categories: categoriesSlice,
        comments:commentSlice,
        products: productSlice,
        reviews: reviewSlice,
        shops: shopSlice,
        users: userSlice,
        modal:showModalSlice,

        notifications: notificationsSlice,
    },
});
