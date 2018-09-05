package uk.co.grahamcox.muck.service.authentication.external.rest

import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import uk.co.grahamcox.muck.service.authentication.external.AuthenticationService
import uk.co.grahamcox.muck.service.rest.Problem
import uk.co.grahamcox.muck.service.rest.hal.Link
import uk.co.grahamcox.muck.service.rest.hal.buildUri
import uk.co.grahamcox.muck.service.user.UserResource
import java.net.URI

/**
 * Controller for managing authentication by a third-party provider
 */
@RestController
@RequestMapping("/api/authentication/external")
@Transactional
class ExternalAuthenticationController(
        private val authenticationServices: Collection<AuthenticationService>
) {
    /**
     * Handle when a request comes in for an unknown authentication service
     */
    @ExceptionHandler(UnknownAuthenticationServiceException::class)
    fun handleUnknownAuthenticationService(e: UnknownAuthenticationServiceException) =
            Problem(
                    type = URI("tag:grahamcox.co.uk,2018,problems/unknown-authentication-service"),
                    title = "The requested Authentication Service was unknown",
                    statusCode = HttpStatus.NOT_FOUND,
                    extraData = mapOf(
                            "authenticationService" to e.authenticationService
                    )
            )

    /**
     * Get the list of providers that we can support
     * @return the list of supported providers
     */
    @RequestMapping(method = [RequestMethod.GET])
    fun getProviders(): ResponseEntity<ExternalAuthenticationServicesModel> {
        val result = ExternalAuthenticationServicesModel(
                links = ExternalAthenticationServicesLinks(
                        self = Link(
                                href = ::getProviders.buildUri()
                        ),
                        services = authenticationServices
                                .map { it.id }
                                .sorted()
                                .map {
                                    Link(
                                            href = ::startAuthentication.buildUri(it),
                                            name = it,
                                            type = null
                                    )
                                }
                )
        )

        return ResponseEntity.ok()
                .contentType(MediaType.valueOf("application/hal+json"))
                .body(result)
    }

    /**
     * Start authentication by the named service
     */
    @RequestMapping(value = "/{service}/start", method = [RequestMethod.GET])
    fun startAuthentication(@PathVariable("service") service: String): ResponseEntity<Unit> {
        val service = authenticationServices.find { it.id == service }
                ?: throw UnknownAuthenticationServiceException(service)

        val redirect = service.buildRedirectUri()

        return ResponseEntity.status(HttpStatus.FOUND)
                .location(redirect)
                .build<Unit>()
    }

    /**
     * Finish authentication by the named service
     */
    @RequestMapping(value = "/{service}/callback", method = [RequestMethod.GET])
    fun finishAuthentication(@PathVariable("service") service: String,
                             @RequestParam queryParams: Map<String, String>): UserResource {
        val service = authenticationServices.find { it.id == service }
                ?: throw UnknownAuthenticationServiceException(service)

        val user = service.completeAuthentication(queryParams)

        return user
    }
}
