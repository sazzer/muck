package uk.co.grahamcox.muck.service.authentication.rest

import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import uk.co.grahamcox.muck.service.authentication.AccessToken
import uk.co.grahamcox.muck.service.authentication.AccessTokenGenerator
import uk.co.grahamcox.muck.service.user.UserId
import java.util.*

/**
 * Controller for some debug calls for the Authentication system
 */
@RestController
@RequestMapping("/debug/authentication")
class DebugController(
        private val accessTokenGenerator: AccessTokenGenerator,
        private val accessTokenSerializer: AccessTokenSerializer
) {
    /**
     * Generate an Access Token for the given User ID
     */
    @RequestMapping(value = "/generate/{user}", method = [RequestMethod.POST])
    fun generateAccessToken(@PathVariable("user") user: UUID): Map<String, Any> {
        val accessToken = accessTokenGenerator.generate(UserId(user))
        val token = accessTokenSerializer.serialize(accessToken)

        return mapOf(
                "accessToken" to accessToken,
                "token" to token
        )
    }

    @RequestMapping(value = "/token/optional", method = [RequestMethod.GET])
    fun getOptionalToken(token: AccessToken?) = token

    @RequestMapping(value = "/token/required", method = [RequestMethod.GET])
    fun getToken(token: AccessToken) = token

    @RequestMapping(value = "/user/optional", method = [RequestMethod.GET])
    fun getOptionalUserId(userId: UserId?) = userId

    @RequestMapping(value = "/user/required", method = [RequestMethod.GET])
    fun getUserId(userId: UserId) = userId
}
