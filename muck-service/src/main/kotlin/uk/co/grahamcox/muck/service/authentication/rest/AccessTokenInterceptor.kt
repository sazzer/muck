package uk.co.grahamcox.muck.service.authentication.rest

import org.apache.commons.lang3.StringUtils
import org.slf4j.LoggerFactory
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Interceptor to handle the Access Token details
 */
class AccessTokenInterceptor(
        private val accessTokenStore: AccessTokenStore,
        private val accessTokenSerializer: AccessTokenSerializer
) : HandlerInterceptorAdapter() {
    companion object {
        /** The logger to use */
        private val LOG = LoggerFactory.getLogger(AccessTokenInterceptor::class.java)

        /** The prefix on the Authorization header to indicate a Bearer token */
        private val HEADER_PREFIX = "Bearer "
    }

    /**
     * Check the incoming HTTP Request for an Authorization header, and if it's a Bearer token then parse it out
     * and store it for future requests to use
     */
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val authorization = request.getHeader("Authorization")
        if (StringUtils.isBlank(authorization)) {
            LOG.debug("No authorization header present")
        } else {
            LOG.trace("Authorization header: {}", authorization)
            if (!authorization.startsWith(HEADER_PREFIX, ignoreCase = false)) {
                LOG.debug("Authorization header is not a bearer token")
            } else {
                val token = authorization.substring(HEADER_PREFIX.length)
                LOG.trace("Token: {}", token)

                val accessToken = accessTokenSerializer.deserialize(token)
                LOG.debug("Access token {} read from request", accessToken)

                accessTokenStore.accessToken = accessToken
            }
        }

        return true
    }

    /**
     * After the handling of the requests, ensure that the Access Token is removed from the store
     */
    override fun afterCompletion(request: HttpServletRequest, response: HttpServletResponse, handler: Any, ex: Exception?) {
        accessTokenStore.reset()
    }
}
