package uk.co.grahamcox.muck.service.authentication.rest

import org.springframework.web.context.request.RequestAttributes
import org.springframework.web.context.request.RequestContextHolder
import uk.co.grahamcox.muck.service.authentication.AccessToken

/**
 * Read/Write store in which the Access Tokens for a request can be persisted
 */
class AccessTokenStore : AccessTokenHolder {
    companion object {
        /** The attribute name to use */
        private val ATTRIBUTE_NAME = AccessTokenStore::class.java.canonicalName
    }

    /** The access token if there is one */
    override var accessToken: AccessToken?
        get() {
            return RequestContextHolder.currentRequestAttributes().getAttribute(
                    ATTRIBUTE_NAME,
                    RequestAttributes.SCOPE_REQUEST) as AccessToken?
        }
        set(value) {
            if (value != null) {
                RequestContextHolder.currentRequestAttributes().setAttribute(
                        ATTRIBUTE_NAME,
                        value,
                        RequestAttributes.SCOPE_REQUEST)
            } else {
                RequestContextHolder.currentRequestAttributes().removeAttribute(ATTRIBUTE_NAME, RequestAttributes.SCOPE_REQUEST)
            }
        }

    /**
     * Reset the access token for the current request
     */
    fun reset() {
        RequestContextHolder.currentRequestAttributes().removeAttribute(ATTRIBUTE_NAME, RequestAttributes.SCOPE_REQUEST)
    }
}
