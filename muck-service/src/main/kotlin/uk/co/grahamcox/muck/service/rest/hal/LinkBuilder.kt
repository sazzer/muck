package uk.co.grahamcox.muck.service.rest.hal

import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder
import java.net.URI
import kotlin.reflect.KFunction
import kotlin.reflect.jvm.javaMethod

/**
 * Helper to build a URI to an MVC Controller
 * @param args The controller arguments
 * @return the URI
 */
fun <R> KFunction<R>.buildUri(vararg args: Any?): URI {
    val method = this.javaMethod!!
    val methodClass = method.declaringClass

    return MvcUriComponentsBuilder.fromMethod(methodClass, method, *args)
            .build()
            .toUri()
}
