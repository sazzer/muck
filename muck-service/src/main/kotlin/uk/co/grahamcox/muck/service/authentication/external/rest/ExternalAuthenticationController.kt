package uk.co.grahamcox.muck.service.authentication.external.rest

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import uk.co.grahamcox.muck.service.authentication.external.AuthenticationService

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
    fun getProviders(): List<String> {
        return authenticationServices.map { it.id }
    }
}
