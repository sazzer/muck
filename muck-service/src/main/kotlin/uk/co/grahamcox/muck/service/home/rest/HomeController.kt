package uk.co.grahamcox.muck.service.home.rest

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import uk.co.grahamcox.muck.service.authentication.external.rest.ExternalAuthenticationController
import uk.co.grahamcox.muck.service.rest.hal.Link
import uk.co.grahamcox.muck.service.rest.hal.buildUri

/**
 * Controller to get the Home Document
 */
@RestController
@RequestMapping("/api")
class HomeController {
    /**
     * Get the Home Document
     */
    @RequestMapping(method = [RequestMethod.GET])
    fun getHomeDocument(): HomeModel {
        return HomeModel(
                links = HomeLinks(
                        self = Link(
                                ::getHomeDocument.buildUri()
                        ),
                        externalAuthenticationServices = Link(
                                ExternalAuthenticationController::getProviders.buildUri()
                        )
                )
        )
    }
}
