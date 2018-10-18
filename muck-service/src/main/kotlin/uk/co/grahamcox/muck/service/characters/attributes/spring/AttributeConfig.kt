package uk.co.grahamcox.muck.service.characters.attributes.spring

import org.springframework.context.annotation.Configuration
import org.springframework.context.support.GenericApplicationContext
import org.springframework.context.support.beans
import uk.co.grahamcox.muck.service.characters.attributes.dao.AttributeServiceImpl
import uk.co.grahamcox.muck.service.characters.attributes.rest.AttributeController

/**
 * Spring configuration for working with Attributes
 */
@Configuration
class AttributeConfig(context : GenericApplicationContext) {
    init {
        beans {
            bean<AttributeServiceImpl>()
            bean<AttributeController>()
        }.initialize(context)
    }
}
