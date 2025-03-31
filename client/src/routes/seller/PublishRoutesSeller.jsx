import { lazy, Suspense } from 'react';
import Loading from '~/components/Loading';
import SingInSeller from '~/components/SingInSellerForm';
import DiscountList from '../../pages/Seller/Discount/DiscountList';
import DiscountForm from '../../pages/Seller/Discount/DiscountForm';
import ListDiscount from '../../pages/Seller/List/ListDiscount';
import Discountupdate from '../../pages/Seller/Discount/Discountupdate';
const SingIn = lazy(() => import('~/components/SingInForm'));
const SingUp = lazy(() => import('~/components/SignUpForm'));
const HomeSeller = lazy(() => import('~/pages/Seller/Home/HomeSeller'));
const ListOders = lazy(() => import('~/pages/Seller/List/ListOder'));
const ListProducts = lazy(() => import('~/pages/Seller/List/ListProduct'));
const ListFeedbacks = lazy(() => import('~/pages/Seller/List/ListFeedback'));
const OrderDetail = lazy(() => import('../../pages/Seller/Order/OrderDetail'));
const AddProducts = lazy(() => import('~/pages/Seller/New/NewProduct'));
const ListMessage = lazy(() => import('~/pages/Seller/Report/HistoryEmail'));
const ListDiscounts = lazy(() => import('../../pages/Seller/Discount/DiscountList'));
const AddDiscount = lazy(() => import('../../pages/Seller/Discount/DiscountForm'));

const SaleServices= lazy(() => import('~/pages/Seller/Service/SaleService'));

export const publishRoutesSeller = [
    {
        index: true,
        element: (
            <Suspense fallback={<Loading />}>
                <HomeSeller />
            </Suspense>
        ),
    },
    {
        path: 'products',
        children: [
            {
                index: true,
                element: (
                    <Suspense fallback={<Loading />}>
                        <ListProducts />
                    </Suspense>
                ),
            },
            {
                path: 'addProduct',
                element: (
                    <Suspense fallback={<Loading />}>
                        <AddProducts />
                    </Suspense>
                ),
            },
            {
                path: 'edit/:productId',
                element: (
                    <Suspense fallback={<Loading />}>
                        <AddProducts isUpdate />
                    </Suspense>
                ),
            },
        ],
    },
    
    {
        path: 'orders',
        children: [
            {
                index: true,
                element: (
                    <Suspense fallback={<Loading />}>
                        <ListOders />
                    </Suspense>
                ),
            },
            {
                path: ':orderId',  // Đảm bảo đúng cấu trúc URL
                element: (
                    <Suspense fallback={<Loading />}>
                        <OrderDetail />
                    </Suspense>
                ),
            },
        ],
    },

    {
        path: 'messages',
        element: (
            <Suspense fallback={<Loading />}>
                <ListMessage />
            </Suspense>
        ),
    },
    {
        path: 'package',
        element: (
            <Suspense fallback={<Loading />}>
                <SaleServices />
            </Suspense>
        ),
    },
    {
        path: 'feedbacks',
        element: (
            <Suspense fallback={<Loading />}>
                <ListFeedbacks />
            </Suspense>
        ),
    },
    {
        path: 'discount',
        element: (
            <Suspense fallback={<Loading />}>
                <DiscountForm />
            </Suspense>
        ),
    },
    {
        path: 'discount/list',
        element: (
            <Suspense fallback={<Loading />}>
                <ListDiscount />

            </Suspense>
        ),
    }, {
        path: 'discount/edit/:id',
        element: (
            <Suspense fallback={<Loading />}>
                <Discountupdate />
            </Suspense>
        ),
    },
    {
        path: 'SignIn',
        element: (
            <Suspense fallback={<Loading />}>
                <SingIn />
            </Suspense>
        ),
    },
    {
        path: 'SignUp',
        element: (
            <Suspense fallback={<Loading />}>
                <SingUp />
            </Suspense>
        ),
    },
    {
        path: 'SignInSeller',
        element: (
            <Suspense fallback={<Loading />}>
                <SingInSeller />
            </Suspense>
        ),
    },
];
