package uk.co.grahamcox.muck.service.rest.hal

import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder
import java.net.URI
import kotlin.reflect.KFunction
import kotlin.reflect.jvm.javaMethod

/**
 * Helper to build a URI to an MVC Controller
 * @param args The controller arguments
 * @return the URI
 */
fun <R> KFunction<R>.buildUri(vararg args: Pair<String, Any?>) = this.buildUri(args.toMap())

/**
 * Helper to build a URI to an MVC Controller
 * @param args The controller arguments
 * @return the URI
 */
fun <R> KFunction<R>.buildUri(args: Map<String, Any?>): String {
    val method = this.javaMethod!!
    val methodClass = method.declaringClass
    val methodArgs = method.parameters.map { param ->
        if (args.containsKey(param.name)) {
            args[param.name]
        } else if (param.isAnnotationPresent(PathVariable::class.java)) {
            val annotation = param.getAnnotation(PathVariable::class.java)
            "{${annotation.value}}"
        } else {
            null
        }

    }

    return MvcUriComponentsBuilder.fromMethod(methodClass, method, *methodArgs.toTypedArray())
            .build(false)
            .toUriString()
}
