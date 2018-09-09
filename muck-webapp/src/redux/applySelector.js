// @flow

/**
 * Apply a selector to a single modules state.
 * This is designed to work from the `select` effect that Redux-Saga provides.
 *
 * @param state The entire application state
 * @param module The module to apply the selector to
 * @param selector The selector to apply
 * @param args Any arguments to the selector
 * @return the result of the selector
 */
export default function applySelector<STATE, RESULT>(state: { [string] : STATE },
                                                     module: string,
                                                     selector: (STATE, ...args: Array<any>) => RESULT,
                                                     args: Array<any> = []): RESULT {
    const moduleState = state[module];
    return selector.apply(this, [moduleState, ...args]);
}
