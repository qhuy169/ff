import { axiosClient } from '~/api';

const branch_api = "/shipment";

export const shipmentService = {
    getAllOrderSameArea() {
        return axiosClient.get(`${branch_api}/same-area`);
    },
    getAllOrderShipment() {
        return axiosClient.get(`${branch_api}/get-all?status=SHIPPING`);
    },
    postShipment([id]) {
        return axiosClient.post(`${branch_api}/receive-order`,[id]);
    },
    putShipment(id) {
        return axiosClient.put(`${branch_api}/${id}`,{
            "status": "ORDER_COMPLETED",
            "log": null,
            "createdAt": Date.now()
        });
    }
};
