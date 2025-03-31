import { lazy, Suspense } from 'react';
import Loading from '~/components/Loading';
const HomeShipment = lazy(() => import('~/pages/Shipment/Shipment'));
const ListOders = lazy(() => import('~/pages/Shipment/ShipmentOrder'));

export const publishRoutesShipment = [
    {
        index: true,
        element: (
            <Suspense fallback={<Loading />}>
                <HomeShipment />
            </Suspense>
        ),
    },
    {
        path: 'listorders',
        element: (
            <Suspense fallback={<Loading />}>
                <ListOders />
            </Suspense>
        ),
    },
];
