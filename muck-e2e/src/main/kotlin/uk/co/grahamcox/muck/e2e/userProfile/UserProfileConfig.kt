package uk.co.grahamcox.muck.e2e.userProfile

import org.springframework.context.annotation.Configuration
import org.springframework.context.support.GenericApplicationContext
import org.springframework.context.support.beans
import uk.co.grahamcox.muck.e2e.beans.BeanMatcher
import uk.co.grahamcox.muck.e2e.beans.BeanPopulator
import uk.co.grahamcox.muck.e2e.pages.userProfile.UserProfileData

/**
 * Spring Config for testing User Profiles
 */
@Configuration
class UserProfileConfig(context: GenericApplicationContext) {
    init {
        beans {
            bean("userProfileDetailsMatcher") {
                BeanMatcher(
                        mapOf(
                                "Name" to BeanMatcher.BeanField(
                                        getter = UserProfileData::displayName
                                ),
                                "Email Address" to BeanMatcher.BeanField(
                                        getter = UserProfileData::email
                                )
                        )
                )
            }

            bean("userProfileDetailsPopulator") {
                BeanPopulator(
                        mapOf(
                                "Name" to BeanPopulator.BeanField(
                                        setter = UserProfileData::displayName
                                ),
                                "Email Address" to BeanPopulator.BeanField(
                                        setter = UserProfileData::email
                                )
                        )
                )
            }
        }.initialize(context)
    }
}
