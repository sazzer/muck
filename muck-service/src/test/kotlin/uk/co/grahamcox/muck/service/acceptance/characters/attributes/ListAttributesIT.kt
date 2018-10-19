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
 * Acceptance tests for listing attribute details
 */
class ListAttributesIT : AcceptanceTestBase() {

    /** The means to seed attribute records */
    @Autowired
    private lateinit var attributeSeeder: DatabaseSeeder

    /**
     * Test listing attributes when none exist
     */
    @Test
    fun getZeroAttributes() {
        val response = requester.get("/api/attributes")

        Assertions.assertAll(
                Executable { Assertions.assertEquals(HttpStatus.OK, response.statusCode) },
                Executable { Assertions.assertTrue(response.headers.contentType!!.isCompatibleWith(MediaType.valueOf("application/json"))) },

                Executable { Assertions.assertEquals(convertFromJson("""{
                    "data": [],
                    "offset": 0,
                    "total": 0,
                    "hasPrevious": false,
                    "hasNext": false
                }"""), response.getValue("/body")) }
        )
    }

    /**
     * Test listing attributes when one exists
     */
    @Test
    fun getOneAttribute() {
        val strength = attributeSeeder.seed(mapOf(
                "id" to UUID.randomUUID().toString(),
                "name" to "Strength",
                "description" to "My Strength is boundless"
        ))

        val response = requester.get("/api/attributes")

        Assertions.assertAll(
                Executable { Assertions.assertEquals(HttpStatus.OK, response.statusCode) },
                Executable { Assertions.assertTrue(response.headers.contentType!!.isCompatibleWith(MediaType.valueOf("application/json"))) },

                Executable { Assertions.assertEquals(convertFromJson("""{
                    "data": [
                        {
                            "id": "${strength["id"]}",
                            "name": "${strength["name"]}",
                            "description": "${strength["description"]}"
                        }
                    ],
                    "offset": 0,
                    "total": 1,
                    "hasPrevious": false,
                    "hasNext": false
                }"""), response.getValue("/body")) }
        )
    }

    /**
     * Test listing attributes when one exists
     */
    @Test
    fun getTwoAttributesNoParams() {
        val strength = attributeSeeder.seed(mapOf(
                "name" to "Strength",
                "description" to "My Strength is boundless"
        ))

        val dexterity = attributeSeeder.seed(mapOf(
                "name" to "Dexterity",
                "description" to "My Dexterity is legendary"
        ))

        val response = requester.get("/api/attributes")

        Assertions.assertAll(
                Executable { Assertions.assertEquals(HttpStatus.OK, response.statusCode) },
                Executable { Assertions.assertTrue(response.headers.contentType!!.isCompatibleWith(MediaType.valueOf("application/json"))) },

                Executable { Assertions.assertEquals(convertFromJson("""{
                    "data": [
                        {
                            "id": "${dexterity["id"]}",
                            "name": "${dexterity["name"]}",
                            "description": "${dexterity["description"]}"
                        }, {
                            "id": "${strength["id"]}",
                            "name": "${strength["name"]}",
                            "description": "${strength["description"]}"
                        }
                    ],
                    "offset": 0,
                    "total": 2,
                    "hasPrevious": false,
                    "hasNext": false
                }"""), response.getValue("/body")) }
        )
    }
}
