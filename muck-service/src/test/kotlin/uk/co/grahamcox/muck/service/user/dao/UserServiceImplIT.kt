package uk.co.grahamcox.muck.service.user.dao

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import uk.co.grahamcox.muck.service.database.ResourceNotFoundException
import uk.co.grahamcox.muck.service.spring.SpringTestBase
import uk.co.grahamcox.muck.service.user.UserId
import java.util.*

/**
 * Integration test for the UserService
 */
internal class UserServiceImplIT : SpringTestBase() {
    /** The test subject */
    @Autowired
    private lateinit var userServiceImpl: UserServiceImpl

    /**
     * Test getting an unknown user by ID
     */
    @Test
    fun testGetUnknownById() {
        val userId = UserId(UUID.randomUUID())

        val e = Assertions.assertThrows(ResourceNotFoundException::class.java) {
            userServiceImpl.getById(userId)
        }

        Assertions.assertEquals(userId, e.id)
    }
    
}
