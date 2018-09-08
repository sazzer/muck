// @flow

import request from './requester';
import halson from 'halson';

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
    links: { [string] : Links }
};

/**
 * Load the HAL Resource that is referenced by the given URL and Parameters
 * @param url the URL
 * @param params the Parameters
 * @return the resource
 */
export function loadResource(url: string, params?: { [string] : any }): Promise<Resource> {
    return request({
        url,
        params
    }).then((response) => {
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
            links: responseLinks
        };
    });
}
