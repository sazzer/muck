package uk.co.grahamcox.muck.e2e.userProfile

import org.springframework.context.annotation.Configuration
import org.springframework.context.support.GenericApplicationContext
import org.springframework.context.support.beans
import uk.co.grahamcox.muck.e2e.matcher.BeanField
import uk.co.grahamcox.muck.e2e.matcher.BeanMatcher
import uk.co.grahamcox.muck.e2e.pages.userProfile.UserProfileData

/**
 * Spring Config for testing User Profiles
 */
@Configuration
class UserProfileConfig(context: GenericApplicationContext) {
    init {
        beans {
            bean("userProfileDetailsMatcher") {
                BeanMatcher<UserProfileData>(
                        mapOf(
                                "Name" to BeanField(
                                        getter = UserProfileData::displayName
                                ),
                                "Email Address" to BeanField(
                                        getter = UserProfileData::email
                                )
                        )
                )
            }
        }.initialize(context)
    }
}
