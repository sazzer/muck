// @flow

/** Type representing an error for a specific field */
export type FieldError = {
    field: string,
    error: string
};

/** Type representing errors from making some call */
export type Errors = {
    error: string,
    fieldErrors?: Array<FieldError>
};
