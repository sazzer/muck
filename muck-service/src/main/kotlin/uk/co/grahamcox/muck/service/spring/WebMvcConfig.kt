package uk.co.grahamcox.muck.service.spring

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import uk.co.grahamcox.muck.service.authentication.rest.AccessTokenArgumentResolver
import uk.co.grahamcox.muck.service.authentication.rest.AccessTokenInterceptor

/**
 * Web MVC Configuration for the system
 */
@Configuration
class WebMvcConfig : WebMvcConfigurer {
    /** Interceptor to add for handling Access Tokens */
    @Autowired
    private lateinit var accessTokenInterceptor: AccessTokenInterceptor

    /** Argument Resolver for accessing Access tokens */
    @Autowired
    private lateinit var accessTokenArgumentResolver: AccessTokenArgumentResolver

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(accessTokenInterceptor)
    }

    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(accessTokenArgumentResolver)
    }
    
    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/api/**")
                .allowedMethods(*HttpMethod.values().map { it.name }.toTypedArray())
                .allowedOrigins("*")
    }
}
