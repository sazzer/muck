// @flow

import uriTemplates from 'uri-templates';
import getConfig from '../config';

/**
 * Helper to build a URI from a template
 * @param uri the URI to build from
 * @param params The parameters to bind into the URI
 * @return the built URI
 */
export function buildUri(uri: string, params: { [string] : any } = {}) : string {
    const uriBase = getConfig('API_URI');

    return uriTemplates(uriBase + uri).fill(params);

}
