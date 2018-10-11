// @flow

import axios from 'axios';
import MockAdapter from 'axios-mock-adapter';
import request from '../requester';
import { setBearerToken } from "../requester";

let mockAxios;

beforeEach(() => {
    mockAxios = new MockAdapter(axios);
    setBearerToken(undefined);
});

afterEach(() => {
    mockAxios.restore();
});

describe('GET', () => {
    describe('Simple request', () => {
        let response;

        beforeEach(async () => {
            mockAxios.onGet('http://example.com:8080/api/a/b').reply(200, {
                users: [
                    { id: 1, name: 'John Smith' }
                ]
            }, {
                'X-Some-Header': 'abc123'
            });

            response = await request({
                url: '/a/b'
            });
        });
        it('Returns the expected status code', () => {
            expect(response.status).toEqual(200);
        });
        it('Returns the expected payload', () => {
            expect(response.data).toEqual({
                users: [
                    { id: 1, name: 'John Smith' }
                ]
            });
        });
        it('Returns the expected headers', () => {
            expect(response.headers).toEqual({
                'X-Some-Header': 'abc123'
            });
        });
    });
    describe('Request with parameters', () => {
        let response;

        beforeEach(async () => {
            mockAxios.onGet('http://example.com:8080/api/a/b?start=1&end=5').reply(200, {
                users: [
                    { id: 1, name: 'John Smith' }
                ]
            }, {
                'X-Some-Header': 'abc123'
            });

            response = await request({
                url: '/a/b{?start,end}',
                params: {
                    start: 1,
                    end: 5
                }
            });
        });
        it('Returns the expected status code', () => {
            expect(response.status).toEqual(200);
        });
        it('Returns the expected payload', () => {
            expect(response.data).toEqual({
                users: [
                    { id: 1, name: 'John Smith' }
                ]
            });
        });
        it('Returns the expected headers', () => {
            expect(response.headers).toEqual({
                'X-Some-Header': 'abc123'
            });
        });
    });
});

describe('POST', () => {
    describe('Request with body', () => {
        let response;

        beforeEach(async () => {
            mockAxios.onPost('http://example.com:8080/api/a/b').reply((config) => {
                expect(config.data).toEqual(JSON.stringify({
                    start: 1,
                    end: 5
                }));

                return [
                    200,
                    {
                        users: [
                            { id: 1, name: 'John Smith' }
                        ]
                    },
                    {
                        'X-Some-Header': 'abc123'
                    }
                ]
            });

            response = await request({
                url: '/a/b',
                method: 'POST',
                data: {
                    start: 1,
                    end: 5
                }
            });
        });
        it('Returns the expected status code', () => {
            expect(response.status).toEqual(200);
        });
        it('Returns the expected payload', () => {
            expect(response.data).toEqual({
                users: [
                    { id: 1, name: 'John Smith' }
                ]
            });
        });
        it('Returns the expected headers', () => {
            expect(response.headers).toEqual({
                'X-Some-Header': 'abc123'
            });
        });
    });
});

describe('setBearerToken', () => {
    describe('Without providing a token for future requests', () => {
        let response;

        beforeEach(async () => {
            mockAxios.onGet('http://example.com:8080/api/a/b').reply(200, {
                users: [
                    { id: 1, name: 'John Smith' }
                ]
            }, {
                'X-Some-Header': 'abc123'
            });

            response = await request({
                url: '/a/b'
            });
        });
        it('Has the correct headers', () => {
            expect((response : { [string] : any }).config.headers).toEqual({
                'Accept': 'application/json, text/plain, */*'
            });
        });
    });

    describe('Providing a token for future requests', () => {
        let response;

        beforeEach(async () => {
            mockAxios.onGet('http://example.com:8080/api/a/b').reply(200, {
                users: [
                    { id: 1, name: 'John Smith' }
                ]
            }, {
                'X-Some-Header': 'abc123'
            });

            setBearerToken('someToken');
            response = await request({
                url: '/a/b'
            });
        });
        it('Has the correct headers', () => {
            expect((response : { [string] : any }).config.headers).toEqual({
                'Accept': 'application/json, text/plain, */*',
                'Authorization': 'Bearer someToken'
            });
        });
    });
});
