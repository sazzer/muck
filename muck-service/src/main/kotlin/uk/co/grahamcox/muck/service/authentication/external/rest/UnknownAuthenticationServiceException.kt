package uk.co.grahamcox.muck.service.authentication.external.rest

/**
 * Exception to indicate that the given Authentication Service was unknown
 */
class UnknownAuthenticationServiceException(val authenticationService: String) :
        RuntimeException("Unknown authentication service: $authenticationService")
