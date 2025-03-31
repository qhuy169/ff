import { Navigate, useRoutes } from 'react-router-dom';
import { CommonLayout, DefaultLayout,ShipmentLayout } from '../components/Layout';
import NotFound from '../pages/User/NotFound';
import { publishRoutes } from './PublishRoutes';
import { publishRoutesSeller } from './seller/PublishRoutesSeller';
import { productDetailRoute } from './ProductDetailRoutes';
import { lazy, Suspense, useEffect } from 'react';
import Loading from '~/components/Loading';
import { publishRoutesShipment } from './shipment/PublishRoutesShipment';
const Home = lazy(() => import('../pages/User/Home'));

export default function Routes() {
    const routes = [
        {
            path: '/',
            element: <CommonLayout />,
            children: [
                ...publishRoutes,
                productDetailRoute,
                { path: '*', element: <NotFound title="Not found" /> },
            ],
        },
        {
            path: '/Seller',
            element: <DefaultLayout />,
            children: [
                ...publishRoutesSeller,
                { path: '*', element: <NotFound title="Not found" /> },
            ],
        },
        {
            path: '/Shipment',
            element: <ShipmentLayout />,
            children: [
                ...publishRoutesShipment,
                { path: '*', element: <NotFound title="Not found" /> },
            ],
        },
        {
            path: '*',
            element: <Navigate to="/"/>
        },
        {
            path: '#',
            element: <Navigate to="/"/>
        },
    ];
    return useRoutes(routes);
}
