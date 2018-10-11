// @flow

import { buildUri } from "../urlBuilder";

describe('buildUri', () => {
    it('Works with no URI parameters', () => {
        const result = buildUri('/a/b/c');
        expect(result).toEqual('http://example.com:8080/api/a/b/c');
    });

    it('Works with Path Parameters only', () => {
        const result = buildUri('/user/{userId}', { userId: 'abc123' });
        expect(result).toEqual('http://example.com:8080/api/user/abc123');
    });

    it('Works with Querystring parameters only', () => {
        const result = buildUri('/user{?name}', { name: 'graham' });
        expect(result).toEqual('http://example.com:8080/api/user?name=graham');
    });

    it('Works with absent Querystring parameters', () => {
        const result = buildUri('/user{?name}', {});
        expect(result).toEqual('http://example.com:8080/api/user');
    });

    it('Works wth Path and Querystring parameters', () => {
        const result = buildUri('/user/{userId}{?details}', { userId: 'abc123', details: true });
        expect(result).toEqual('http://example.com:8080/api/user/abc123?details=true');
    });

    it('Works wth unexpected parameters', () => {
        const result = buildUri('/user/{userId}', { userId: 'abc123', details: true });
        expect(result).toEqual('http://example.com:8080/api/user/abc123');
    });
});
