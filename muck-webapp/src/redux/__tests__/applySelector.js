// @flow

import applySelector from '../applySelector';

describe('applySelector', () => {
    const state = {
        module: {}
    };

    describe('When the selector has no arguments', () => {
        const selector = jest.fn();
        selector.mockReturnValue('Hello');
        const result = applySelector(state, 'module', selector);

        it('Calls the selector as expected', () => {
            expect(selector).toHaveBeenCalledTimes(1);
        });
        it('Called the selector with the correct arguments', () => {
            expect(selector).toBeCalledWith({});
        });
        it('Returns the correct value', () => {
            expect(result).toEqual('Hello');
        });
    });

    describe('When the selector has arguments', () => {
        const selector = jest.fn();
        selector.mockReturnValue('Hello');
        const result = applySelector(state, 'module', selector, [1, 2, 3]);

        it('Calls the selector as expected', () => {
            expect(selector).toHaveBeenCalledTimes(1);
        });
        it('Called the selector with the correct arguments', () => {
            expect(selector).toBeCalledWith({}, 1, 2, 3);
        });
        it('Returns the correct value', () => {
            expect(result).toEqual('Hello');
        });
    });
});
