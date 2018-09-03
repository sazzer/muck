package uk.co.grahamcox.muck.service.authentication.rest

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import uk.co.grahamcox.muck.service.rest.Problem
import java.net.URI

/**
 * Controller Advice for Authentication calls
 */
@RestControllerAdvice
class AuthenticationControllerAdvice {
    /**
     * Handle when an Access Token was required but not provided
     */
    @ExceptionHandler(MissingAccessTokenException::class)
    fun handleMissingAccessToken() = Problem(
            type = URI("tag:grahamcox.co.uk,2018,problems/missing-access-token"),
            title = "No Access Token was provided",
            statusCode = HttpStatus.UNAUTHORIZED
    )
}
