package uk.co.grahamcox.muck.service.authorization.rest

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import uk.co.grahamcox.muck.service.authorization.AuthorizationFailedException
import uk.co.grahamcox.muck.service.rest.Problem
import java.net.URI

/**
 * Controller Advice for the Authorization failures
 */
@RestControllerAdvice
class AuthorizationControllerAdvice {
    /**
     * Handle an Authorization Failed exception to return the correct problem
     */
    @ExceptionHandler(AuthorizationFailedException::class)
    fun handleAuthorizationFailed() =
            Problem(
                    type = URI("tag:grahamcox.co.uk,2018,problems/access-denied"),
                    title = "Access denied to this resource",
                    statusCode = HttpStatus.FORBIDDEN

            )
}
