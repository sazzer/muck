// @flow

import { loadResource } from './hal';
import type {Resource} from "./hal";
export { loadResource, putResource } from './hal';

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
