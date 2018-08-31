package uk.co.grahamcox.muck.service.acceptance.authentication

import org.springframework.context.annotation.Configuration
import org.springframework.context.support.GenericApplicationContext
import org.springframework.context.support.beans
import uk.co.grahamcox.muck.service.acceptance.requester.ListResponseMatcher
import uk.co.grahamcox.muck.service.acceptance.requester.ResponseFieldConfig
import uk.co.grahamcox.muck.service.acceptance.requester.ResponseMatcher

/**
 * Spring Config for authentication steps
 */
@Configuration
class AuthenticationConfig(context: GenericApplicationContext) {
    init {
        beans {
            bean("authenticationServicesMatcher") {
                ListResponseMatcher(
                        responseMatcher = ResponseMatcher(
                                ref(),
                                mapOf(
                                        "Name" to ResponseFieldConfig(
                                                fieldPath = "/name"
                                        ),
                                        "Redirect URI" to ResponseFieldConfig(
                                                fieldPath = "/href"
                                        )
                                )
                        ),
                        preIndexPath = "/body/_links/services[",
                        postIndexPath = "]"
                )
            }
        }.initialize(context)
    }
}
