import axios from 'axios';
import { GHN_CONFIG } from '../../utils';

const axiosGHN = axios.create({
    withCredentials: false,
    headers: {
        'Token': GHN_CONFIG.token,
        'shopId': GHN_CONFIG.shopId,
        'Content-Type': 'application/json',
        'Accept': 'application/json',
    },
});

export default axiosGHN;
