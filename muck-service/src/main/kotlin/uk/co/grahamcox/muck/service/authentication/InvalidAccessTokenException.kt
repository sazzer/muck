package uk.co.grahamcox.muck.service.authentication

/**
 * Indication that an Access Token is invalid
 */
class InvalidAccessTokenException(msg: String) : RuntimeException(msg)
