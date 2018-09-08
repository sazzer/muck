// @flow

import axios from 'axios';
import MockAdapter from 'axios-mock-adapter';
import * as testSubject from '../hal';

let mockAxios;

beforeEach(() => {
    mockAxios = new MockAdapter(axios);
});

afterEach(() => {
    mockAxios.restore();
});

describe('loadResource', () => {
    describe('With no links', () => {
        let resource;
        beforeEach(async () => {
            mockAxios.onGet('http://example.com:8080/api/a/b').reply(200, {
                users: [
                    { id: 1, name: 'John Smith' }
                ]
            });

            resource = await testSubject.loadResource('/a/b');
        });

        it('Has the correct data', () => {
            expect(resource.data).toEqual({
                users: [
                    { id: 1, name: 'John Smith' }
                ]
            });
        });
        it('Has the correct links', () => {
            expect(resource.links).toEqual({});
        });
    });
    describe('With no data', () => {
        describe('With a single self link', () => {
            let resource;
            beforeEach(async () => {
                mockAxios.onGet('http://example.com:8080/api/a/b').reply(200, {
                    _links: {
                        self: {
                            href: '/a/b'
                        }
                    }
                });

                resource = await testSubject.loadResource('/a/b');
            });

            it('Has the correct data', () => {
                expect(resource.data).toEqual({});
            });
            it('Has the correct links', () => {
                expect(resource.links).toEqual({
                    self: [
                        {
                            href: '/a/b'
                        }
                    ]
                });
            });
        });

        describe('With a set of links', () => {
            let resource;
            beforeEach(async () => {
                mockAxios.onGet('http://example.com:8080/api/a/b').reply(200, {
                    _links: {
                        other: [{
                            name: 'a1',
                            href: '/first'
                        }, {
                            name: 'b2',
                            href: '/second'
                        }, {
                            name: 'c3',
                            href: '/third'
                        }]
                    }
                });

                resource = await testSubject.loadResource('/a/b');
            });

            it('Has the correct data', () => {
                expect(resource.data).toEqual({});
            });
            it('Has the correct links', () => {
                expect(resource.links).toEqual({
                    other: [
                        {
                            name: 'a1',
                            href: '/first'
                        }, {
                            name: 'b2',
                            href: '/second'
                        }, {
                            name: 'c3',
                            href: '/third'
                        }
                    ]
                });
            });
        });
    });
});
