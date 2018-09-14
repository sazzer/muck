package uk.co.grahamcox.muck.service.user.rest

import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import uk.co.grahamcox.muck.service.authorization.Authorizer
import uk.co.grahamcox.muck.service.database.ResourceNotFoundException
import uk.co.grahamcox.muck.service.rest.Problem
import uk.co.grahamcox.muck.service.rest.hal.Link
import uk.co.grahamcox.muck.service.rest.hal.buildUri
import uk.co.grahamcox.muck.service.user.UserId
import uk.co.grahamcox.muck.service.user.UserService
import java.net.URI
import java.util.*

/**
 * Controller for interacting with user records
 */
@RestController
@RequestMapping("/api/users")
class UserController(private val userService: UserService) {
    /**
     * Handle interacting with an unknown user
     */
    @ExceptionHandler(ResourceNotFoundException::class)
    fun handleUnknownUser() =
            Problem(
                    type = URI("tag:grahamcox.co.uk,2018,problems/users/unknown-user"),
                    title = "The requested user was not found",
                    statusCode = HttpStatus.NOT_FOUND
            )

    /**
     * Get the details of a user by their unique ID
     */
    @RequestMapping("/{id}", method = [RequestMethod.GET])
    fun getUser(@PathVariable("id") rawUserId: UUID,
                currentUser: UserId,
                authorizer: Authorizer) : ResponseEntity<UserModel> {
        val userId = UserId(rawUserId)

        authorizer {
            isUser(userId)
        }

        val user = userService.getById(userId)

        val result = UserModel(
                email = user.data.email,
                displayName = user.data.displayName,
                logins = user.data.logins
                        .map { UserLoginModel(
                                provider = it.provider,
                                providerId = it.providerId,
                                displayName = it.displayName
                        ) }
                        .sortedWith(compareBy(UserLoginModel::provider, UserLoginModel::displayName, UserLoginModel::providerId)),
                links = UserLinks(
                        self = Link(
                                href = ::getUser.buildUri(rawUserId, null, null)
                        )
                )
        )
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf("application/hal+json"))
                .body(result)
    }
}
