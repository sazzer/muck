// @flow

import request from './requester';
import halson from 'halson';
import uriTemplates from 'uri-templates';
import type {Response} from './requester';

/** Type representing a Link in a HAL Resource */
export type Link = {
    href: string,
    templated?: boolean,
    type?: string,
    name?: string
};

/** Type representing a set of links */
export type Links = Array<Link>;

/** Type representing a HAL Resource */
export type Resource = {
    data: { [string] : any },
    links: { [string] : Links },
    getLinks: (rel: string) => Links,
    getLink: (rel: string, name?: string) => ?Link
};

/** Type representing the params for loading a resource */
export type LoadResourceParams = {
    pathParams?: { [string] : any },
    queryParams?: { [string] : any }
};

/** Type representing the params for putting a resource */
export type PutResourceParams = {
    pathParams?: { [string] : any },
    queryParams?: { [string] : any },
    resource: { [string] : any }
};

function parseResponse(response: Response): Resource {
    const data = response.data;
    const processed = halson(data);

    const responseData = Object.keys(data)
        .filter((key) => key !== '_links')
        .reduce((obj, key) => {
            obj[key] = data[key];
            return obj;
        }, {});

    const responseLinks = processed.listLinkRels()
        .reduce((obj, rel) => {
            obj[rel] = processed.getLinks(rel);
            return obj;
        }, {});

    return {
        data: responseData,
        links: responseLinks,
        getLinks: (rel: string) => responseLinks[rel] || [],
        getLink: (rel: string, name?: string): ?Link => {
            const links = responseLinks[rel] || [];
            return links.find((link) => (link.name === name) || (name === undefined));
        }
    };
}

/**
 * Load the HAL Resource that is referenced by the given URL and Parameters
 * @param url the URL
 * @param params the URL Parameters
 * @return the resource
 */
export function loadResource(url: string, params: LoadResourceParams = {}): Promise<Resource> {
    const realUrl = uriTemplates(url)
        .fill(params.pathParams);

    return request({
        url: realUrl,
        params: params.queryParams
    }).then(parseResponse);
}

/**
 * PUT the HAL Resource that is referenced by the given URL and Parameters
 * @param url the URL
 * @param params the Parameters
 * @return the resource
 */
export function putResource(url: string, params: PutResourceParams): Promise<Resource> {
    const realUrl = uriTemplates(url)
        .fill(params.pathParams);

    return request({
        url: realUrl,
        method: 'PUT',
        params: params.queryParams,
        data: params.resource
    }).then(parseResponse);
}
