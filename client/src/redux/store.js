import { configureStore } from '@reduxjs/toolkit';



import userReducer from './user/userSlice';
import categoryReducer from './category/categoriesSlice';
import searchSlice from './search/searchSlice';
import rateReducer from './rate/ratesSlice';
import productReducer from './product/productsSlice';
import shopReducer from './shop/shopSlice';
import cartItemsReducer from './shopping-cart/cartItemsSlide';
import orderSlice from './order/orderSlice';
import orderShopSlice from './orderShop/orderShopSlice';
import historyOrdersSlice from './history/historyOrdersSlice';
import discountReducer from './discount/discountsSlice';
import brandReducer from './brand/brandSlice';
import productModalReducer from './product-modal/productModalSlice';
import statisticReducer from './statistic/statisticsSlice';

import chatContactReducer from './chat/contact/contactsSlice';
import notificationReducer from './notification/notificationsSlice';
import emails from './Email/EmailSlice';
//khoi tao store
export const store = configureStore({
    reducer: {
        search: searchSlice,
        user: userReducer,
        categories: categoryReducer,
        cartItems: cartItemsReducer,
        products: productReducer,
        rates: rateReducer,
        shops: shopReducer,
        orders: orderSlice,
        orderShops: orderShopSlice,
        historyOrders: historyOrdersSlice,
        discounts: discountReducer,
        brands: brandReducer,
        productModal: productModalReducer,
        statistics: statisticReducer,

        chatContacts: chatContactReducer,
        notifications: notificationReducer,
        emails: emails,
    },
});
