import { lazy, Suspense } from 'react';
import Loading from '~/components/Loading';

const Home = lazy(() => import('../pages/User/Home'));
const SingIn = lazy(() => import('../components/SingInForm'));
const SingUp = lazy(() => import('../components/SignUpForm'));
const Category = lazy(() => import('../pages/User/Category'));
const Cart = lazy(() => import('../pages/User/Cart'));
import { ChatContextProvider } from '../context/ChatContext';
const Chat = lazy(() => import('../pages/User/Chat'));
const ShopInfo = lazy(() => import('../pages/User/ShopInfo'));
const Order = lazy(() => import('../pages/User/Order'));
const PurchaseHistory = lazy(() => import('../pages/User/History/PurchaseHistory'));
const History = lazy(() => import('../pages/User/History'));
const Profile = lazy(() => import('../pages/User/History/Profile'));
const OrderDetail = lazy(() => import('../pages/User/OrderDetail'));
const SearchPage = lazy(() => import('../pages/User/Search'));
//serler
const HomeSeller = lazy(() => import('../pages/Seller/Home/HomeSeller'));
const DeliveryPage = lazy(() => import('../pages/User/Delivery/Delivery'));
import SingInSeller from '~/components/SingInSellerForm';
import ChatContainer from '../components/Chat/ChatContainer';
export const publishRoutes = [
    {
        index: true,
        element: (
            <Suspense fallback={<Loading />}>
                <Home title="Website" />
            </Suspense>
        ),
    },
    {
        path: '/:categorySlug',
        element: (
            <Suspense fallback={<Loading />}>
                <Category title="Máy tính bảng, tablet giá rẻ, trả góp 0%" />
            </Suspense>
        ),
    },

    {
        path: '/search',
        // path: 'tim-kiem',
        element: (
            <Suspense fallback={<Loading />}>
                <SearchPage title="Tìm kiếm | PNTech" />
            </Suspense>
        ),
        children: [
            {
                path: '?keyword=:keyword',
                index: true,
                // path: 'tim-kiem',
                element: (
                    <Suspense fallback={<Loading />}>
                        <SearchPage title="Tìm kiếm | PNTech" />
                    </Suspense>
                ),
            },
        ],
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
        path: '/cart',
        element: (
            <Suspense fallback={<Loading />}>
                <Cart title="Giỏ hàng - PNTech" />
            </Suspense>
        ),
    },
    {
        path: '/chat',
        element: (
            <Suspense fallback={<Loading />}>
                <ChatContextProvider>
                    <Chat title="Chat - PNTech" />
                </ChatContextProvider>
            </Suspense>
        ),
        children: [
            {
                path: ':userId',
                index: true,
                // path: 'tim-kiem',
                element: (
                    <Suspense fallback={<Loading />}>
                        <ChatContextProvider>
                            <Chat title="Chat - PNTech" />
                        </ChatContextProvider>
                    </Suspense>
                ),
            },
        ],
    },
    {
        path: '/ShopInfo/:slug',
        element: (
            <Suspense fallback={<Loading />}>
                <ShopInfo title="Thông tin Shop | PNTech" />
            </Suspense>
        ),
    },

    {
        path: 'order',
        element: (
            <Suspense fallback={<Loading />}>
                <Order title="Đơn hàng - PNTech" />
            </Suspense>
        ),
    },
    {
        path: 'order/detail/:id',
        element: (
            <Suspense fallback={<Loading />}>
                <OrderDetail title="Chi tiết Đơn hàng - PNTech" />
            </Suspense>
        ),
    },

    {
        path: 'history',
        element: (
            <Suspense fallback={<Loading />}>
                <History title="Lịch sử mua hàng | PNTech" />
            </Suspense>
        ),
    },
    {
        path: 'history/purchaseHistory',
        element: (
            <Suspense fallback={<Loading />}>
                <PurchaseHistory title="Lịch sử mua hàng | PNTech" />
            </Suspense>
        ),
    },
    {
        path: '/history/profile',
        element: (
            <Suspense fallback={<Loading />}>
                <Profile title="Lịch sử mua hàng | PNTech" />
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
    {
        path: 'Seller',
        element: (
            <Suspense fallback={<Loading />}>
                <HomeSeller />
            </Suspense>
        ),
    },
    {
        path: 'Delivery',
        element: (
            <Suspense fallback={<Loading />}>
                <DeliveryPage />
            </Suspense>
        ),
    },
];
