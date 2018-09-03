package uk.co.grahamcox.muck.service.rest

import org.springframework.core.MethodParameter
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice

/**
 * Response Body Advice for some default handling for problem responses.
 * This ensures that if the response type is a Problem instance then we set the Status Code and Content Type correctly
 */
@ControllerAdvice
class ProblemResponseBodyAdvice : ResponseBodyAdvice<Any?> {
    /**
     * Whether this component supports the given controller method return type
     * and the selected `HttpMessageConverter` type.
     * @param returnType the return type
     * @param converterType the selected converter type
     * @return `true` if [.beforeBodyWrite] should be invoked;
     * `false` otherwise
     */
    override fun supports(returnType: MethodParameter, converterType: Class<out HttpMessageConverter<*>>) = true

    /**
     * Invoked after an `HttpMessageConverter` is selected and just before
     * its write method is invoked.
     * @param body the body to be written
     * @param returnType the return type of the controller method
     * @param selectedContentType the content type selected through content negotiation
     * @param selectedConverterType the converter type selected to write to the response
     * @param request the current request
     * @param response the current response
     * @return the body that was passed in or a modified (possibly new) instance
     */
    override fun beforeBodyWrite(body: Any?,
                                 returnType: MethodParameter,
                                 selectedContentType: MediaType,
                                 selectedConverterType: Class<out HttpMessageConverter<*>>,
                                 request: ServerHttpRequest,
                                 response: ServerHttpResponse): Any? {
        if (body is Problem) {
            response.setStatusCode(body.statusCode)
            response.headers.contentType = MediaType.valueOf("application/problem+json")
        }
        return body
    }
}
