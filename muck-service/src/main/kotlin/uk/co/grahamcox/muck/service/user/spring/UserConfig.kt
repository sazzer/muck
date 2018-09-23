package uk.co.grahamcox.muck.service.user.spring

import org.springframework.context.annotation.Configuration
import org.springframework.context.support.GenericApplicationContext
import org.springframework.context.support.beans
import uk.co.grahamcox.muck.service.user.UserService
import uk.co.grahamcox.muck.service.user.ValidatingUserService
import uk.co.grahamcox.muck.service.user.dao.UserServiceImpl
import uk.co.grahamcox.muck.service.user.rest.UserController

/**
 * Spring configuration for working with Users
 */
@Configuration
class UserConfig(context: GenericApplicationContext) {
    init {
        beans {
            bean<UserService> {
                ValidatingUserService(
                        UserServiceImpl(
                                ref(),
                                ref()
                        ),
                        ref()
                )
            }
            bean<UserController>()
        }.initialize(context)
    }
}
