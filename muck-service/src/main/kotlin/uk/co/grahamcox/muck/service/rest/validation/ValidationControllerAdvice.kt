package uk.co.grahamcox.muck.service.rest.validation

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import uk.co.grahamcox.muck.service.rest.Problem
import java.net.URI
import javax.validation.ConstraintViolationException

/**
 * Controller Advice for handling Constraint Violation Exceptions
 */
@RestControllerAdvice
class ValidationControllerAdvice {
    companion object {
        /** The logger to use */
        private val LOG = LoggerFactory.getLogger(ValidationControllerAdvice::class.java)
    }

    /**
     * Handle the Constraint Violation Exception
     * @param e The exception to handle
     */
    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraintViolation(e: ConstraintViolationException): Problem {
        LOG.debug("Handling constraint violation", e)

        val violations = e.constraintViolations.map {
            Violation(
                    field = it.propertyPath.toString(),
                    title = it.message,
                    value = it.invalidValue,
                    type = it.constraintDescriptor.annotation.annotationClass.qualifiedName!!
            )
        }.sortedWith(compareBy(Violation::field, Violation::type))

        return Problem(
                type = URI("tag:grahamcox.co.uk,2018,problems/invalid-request"),
                title = "The incoming request was invalid",
                statusCode = HttpStatus.BAD_REQUEST,
                extraData = mapOf(
                        "violations" to violations
                )
        )
    }
}
