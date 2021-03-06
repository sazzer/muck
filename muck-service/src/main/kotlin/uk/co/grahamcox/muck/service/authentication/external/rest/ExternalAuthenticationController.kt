package uk.co.grahamcox.muck.service.authentication.external.rest

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView
import uk.co.grahamcox.muck.service.authentication.AccessTokenGenerator
import uk.co.grahamcox.muck.service.authentication.external.AuthenticationService
import uk.co.grahamcox.muck.service.authentication.rest.AccessTokenSerializer
import uk.co.grahamcox.muck.service.rest.Problem
import java.net.URI

/**
 * Controller for managing authentication by a third-party provider
 */
@Controller
@RequestMapping("/api/authentication/external")
@Transactional
class ExternalAuthenticationController(
        private val authenticationServices: Collection<AuthenticationService>,
        private val accessTokenGenerator: AccessTokenGenerator,
        private val accessTokenSerializer: AccessTokenSerializer
) {
    /**
     * Handle when a request comes in for an unknown authentication service
     */
    @ExceptionHandler(UnknownAuthenticationServiceException::class)
    @ResponseBody
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
    @ResponseBody
    fun getProviders(): ExternalAuthenticationServicesModel {
        val result = ExternalAuthenticationServicesModel(
                services = authenticationServices
                        .map { it.id }
                        .sorted()
        )

        return result
    }

    /**
     * Start authentication by the named service
     */
    @RequestMapping(value = "/{service}/start", method = [RequestMethod.GET])
    fun startAuthentication(@PathVariable("service") service: String): ResponseEntity<Unit> {
        val authenticationService = authenticationServices.find { it.id == service }
                ?: throw UnknownAuthenticationServiceException(service)

        val redirect = authenticationService.buildRedirectUri()

        return ResponseEntity.status(HttpStatus.FOUND)
                .location(redirect)
                .build<Unit>()
    }

    /**
     * Finish authentication by the named service
     */
    @RequestMapping(value = "/{service}/callback", method = [RequestMethod.GET])
    fun finishAuthentication(@PathVariable("service") service: String,
                             @RequestParam queryParams: Map<String, String>): ModelAndView {
        val authenticationService = authenticationServices.find { it.id == service }
                ?: throw UnknownAuthenticationServiceException(service)

        val user = authenticationService.completeAuthentication(queryParams)

        val accessToken = accessTokenGenerator.generate(user.identity.id)
        val bearerToken = accessTokenSerializer.serialize(accessToken)

        val result = ModelAndView("authenticated")
        result.addObject("user", user)
        result.addObject("accessToken", accessToken)
        result.addObject("bearerToken", bearerToken)

        return result
    }
}
