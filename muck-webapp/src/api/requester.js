// @flow

import axios from 'axios';
import {buildUri} from "./urlBuilder";

/** The access token used to access the service */
let bearerToken;

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
    const headers = {};
    if (bearerToken) {
        headers['Authorization'] = `Bearer ${bearerToken}`;
    }

    return axios.request({
        url: buildUri(config.url, config.params),
        timeout: 5000,
        headers,
        method: config.method || 'GET',
        data: config.data
    });
}

/**
 * Set the bearer token to be used for future requests
 * @param token the token to use
 */
export function setBearerToken(token?: string) {
    bearerToken = token;
}
