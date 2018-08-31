package uk.co.grahamcox.muck.service.authentication.external.rest

import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import uk.co.grahamcox.muck.service.authentication.external.AuthenticationService
import uk.co.grahamcox.muck.service.rest.hal.Link
import uk.co.grahamcox.muck.service.rest.hal.buildUri
import java.net.URI

/**
 * Controller for managing authentication by a third-party provider
 */
@RestController
@RequestMapping("/api/authentication/external")
class ExternalAuthenticationController(
        private val authenticationServices: Collection<AuthenticationService>
) {
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
        val service = authenticationServices.find { it.id == service } ?: throw IllegalArgumentException()
        val redirect = service.buildRedirectUri()

        return ResponseEntity.status(HttpStatus.FOUND)
                .location(redirect)
                .build<Unit>()
    }
}
