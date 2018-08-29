package uk.co.grahamcox.muck.service.authentication.external.rest

import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import uk.co.grahamcox.muck.service.authentication.external.AuthenticationService
import uk.co.grahamcox.muck.service.rest.hal.Link
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
                                href = URI("/api/authentication/external")
                        ),
                        services = authenticationServices
                                .map { it.id }
                                .sorted()
                                .map {
                                    Link(
                                            href = URI("/api/authentication/external/$it"),
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
}
