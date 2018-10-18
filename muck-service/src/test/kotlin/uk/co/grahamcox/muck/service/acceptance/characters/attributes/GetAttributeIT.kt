package uk.co.grahamcox.muck.service.acceptance.characters.attributes

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import uk.co.grahamcox.muck.service.acceptance.AcceptanceTestBase
import uk.co.grahamcox.muck.service.acceptance.seeder.DatabaseSeeder
import uk.co.grahamcox.muck.service.acceptance.user.GetUserIT
import java.util.*

/**
 * Acceptance tests for getting attribute details
 */
class GetAttributeIT : AcceptanceTestBase() {
    companion object {
        /** The ID of the attribute to get */
        val ATTRIBUTE_ID = UUID.randomUUID()!!
    }
    
    /** The means to seed attribute records */
    @Autowired
    private lateinit var attributeSeeder: DatabaseSeeder
    
    /**
     * Test getting the details of a attribute that doesn't exist
     */
    @Test
    fun getUnknownAttribute() {
        val response = requester.get("/api/attributes/$ATTRIBUTE_ID")

        Assertions.assertAll(
                Executable { Assertions.assertEquals(HttpStatus.NOT_FOUND, response.statusCode) },
                Executable { Assertions.assertEquals(MediaType.valueOf("application/problem+json"), response.headers.contentType) },

                Executable { Assertions.assertEquals(convertFromJson("""{
                    "type": "tag:grahamcox.co.uk,2018,problems/attributes/unknown-attribute",
                    "title": "The requested attribute was not found",
                    "status": 404
                }"""), response.getValue("/body")) }
        )
    }

    /**
     * Test getting the details of a attribute that does exist
     */
    @Test
    fun getKnownAttribute() {
        attributeSeeder.seed(mapOf(
                "id" to ATTRIBUTE_ID.toString(),
                "name" to "Strength",
                "description" to "My Strength is boundless"
        ))

        val response = requester.get("/api/attributes/$ATTRIBUTE_ID")

        Assertions.assertAll(
                Executable { Assertions.assertEquals(HttpStatus.OK, response.statusCode) },
                Executable { Assertions.assertTrue(response.headers.contentType!!.isCompatibleWith(MediaType.valueOf("application/json"))) },

                Executable { Assertions.assertEquals(convertFromJson("""{
                    "id": "$ATTRIBUTE_ID",
                    "name": "Strength",
                    "description": "My Strength is boundless"
                }"""), response.getValue("/body")) }
        )
    }

}
