package uk.co.grahamcox.muck.service.authentication.rest

/**
 * Exception to indicate that an Access Token was required but missing in the request
 */
class MissingAccessTokenException : RuntimeException()
