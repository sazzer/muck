package uk.co.grahamcox.muck.service.user.spring

import org.springframework.context.annotation.Configuration
import org.springframework.context.support.GenericApplicationContext
import org.springframework.context.support.beans
import uk.co.grahamcox.muck.service.user.dao.Neo4jUserDaoImpl

/**
 * Spring configuration for working with Users
 */
@Configuration
class UserConfig(context: GenericApplicationContext) {
    init {
        beans {
            bean<Neo4jUserDaoImpl>()
        }.initialize(context)
    }
}
