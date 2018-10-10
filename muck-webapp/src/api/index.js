// @flow

import { loadResource } from './hal';
import type {Resource} from "./hal";
import requester from './requester';

export { loadResource, putResource } from './hal';
export { buildUri } from './urlBuilder';
export default requester;

/** The API Root */
let apiRoot: Promise<Resource>;

/**
 * Get the API Root resource
 */
export function getApiRoot(): Promise<Resource> {
    if (!apiRoot) {
        apiRoot = loadResource('/');
    }
    return apiRoot;
}
