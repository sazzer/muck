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
            it('Can get the links correctly', () => {
                expect(resource.getLinks('self')).toEqual([
                    {
                        href: '/a/b'
                    }
                ]);
            });
            it('Can get a single unnamed link correctly', () => {
                expect(resource.getLink('self')).toEqual({
                    href: '/a/b'
                });
            });
            it('Can not get a single unknown named link', () => {
                expect(resource.getLink('self', 'a')).toBeUndefined();
            });
            it('Can not get a single unknown link', () => {
                expect(resource.getLink('unknown')).toBeUndefined();
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
            it('Can get the links correctly', () => {
                expect(resource.getLinks('other')).toEqual([
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
                ]);
            });
            it('Can get a single named link correctly', () => {
                expect(resource.getLink('other', 'a1')).toEqual({
                    name: 'a1',
                    href: '/first'
                });
            });
            it('Can not get an unknown named link', () => {
                expect(resource.getLink('other', 'unknown')).toBeUndefined();
            });
            it('Gets the first link when no name provided', () => {
                expect(resource.getLink('other')).toEqual({
                    name: 'a1',
                    href: '/first'
                });
            });
        });
    });
});
