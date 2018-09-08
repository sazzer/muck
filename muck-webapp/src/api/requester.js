// @flow

import axios from 'axios';
import getConfig from '../config';

/** The base URI to call */
const API_BASE=getConfig('API_URI');

/** Type representing the request to be made */
export type RequestConfig = {
    url: string,
    method?: string,
    params?: { [string] : any },
    data?: { [string] : any },
};

/** Type indicating the response that was received */
export type Response = {
    data: { [string] : any },
    status: number,
    headers: { [string] : any }
}

/**
 * Actually make a request using the given request config
 * @param config the configuration of the request
 * @return the response from making this request
 */
export default function request(config: RequestConfig): Promise<Response> {
    return axios.request({
        baseURL: API_BASE,
        timeout: 5000,
        ...config
    });
}
