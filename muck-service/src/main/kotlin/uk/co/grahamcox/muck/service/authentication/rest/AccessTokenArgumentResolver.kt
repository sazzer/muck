package uk.co.grahamcox.muck.service.authentication.rest

import org.springframework.core.MethodParameter
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import uk.co.grahamcox.muck.service.authentication.AccessToken
import uk.co.grahamcox.muck.service.user.UserId
import kotlin.reflect.jvm.kotlinFunction

/**
 * Argument Resolver that makes the Access Token or User Id available to a Controller method
 * @property accessTokenHolder The access token holder to get the current request access token
 */
class AccessTokenArgumentResolver(
        private val accessTokenHolder: AccessTokenHolder
) : HandlerMethodArgumentResolver {
    /**
     * Whether the given [method parameter][MethodParameter] is
     * supported by this resolver.
     * @param parameter the method parameter to check
     * @return `true` if this resolver supports the supplied parameter;
     * `false` otherwise
     */
    override fun supportsParameter(parameter: MethodParameter) =
            parameter.parameterType == AccessToken::class.java ||
                    parameter.parameterType == UserId::class.java

    /**
     * Resolves a method parameter into an argument value from a given request.
     * A [ModelAndViewContainer] provides access to the model for the
     * request. A [WebDataBinderFactory] provides a way to create
     * a [WebDataBinder] instance when needed for data binding and
     * type conversion purposes.
     * @param parameter the method parameter to resolve. This parameter must
     * have previously been passed to [.supportsParameter] which must
     * have returned `true`.
     * @param mavContainer the ModelAndViewContainer for the current request
     * @param webRequest the current request
     * @param binderFactory a factory for creating [WebDataBinder] instances
     * @return the resolved argument value, or `null` if not resolvable
     * @throws Exception in case of errors with the preparation of argument values
     */
    override fun resolveArgument(parameter: MethodParameter,
                                 mavContainer: ModelAndViewContainer?,
                                 webRequest: NativeWebRequest,
                                 binderFactory: WebDataBinderFactory?): Any? {
        val parameterIndex = parameter.parameterIndex
        val nullable = parameter.method?.kotlinFunction!!.parameters[parameterIndex + 1].type.isMarkedNullable

        val accessToken = accessTokenHolder.accessToken
        if (!nullable && accessToken == null) {
            throw MissingAccessTokenException()
        }

        return when(parameter.parameterType) {
            AccessToken::class.java -> accessToken
            UserId::class.java -> accessToken?.user
            else -> null
        }
    }
}
