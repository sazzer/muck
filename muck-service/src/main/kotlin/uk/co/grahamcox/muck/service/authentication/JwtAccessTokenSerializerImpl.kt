package uk.co.grahamcox.muck.service.authentication

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.exceptions.TokenExpiredException
import org.slf4j.LoggerFactory
import uk.co.grahamcox.muck.service.user.UserId
import java.util.*

/**
 * Mechanism to seraialize an Access Token into a JWT
 */
class JwtAccessTokenSerializerImpl(
        private val signingAlgorithm: Algorithm
) : AccessTokenSerializer {
    companion object {
        /** The logger to use */
        private val LOG = LoggerFactory.getLogger(JwtAccessTokenSerializerImpl::class.java)
    }

    /**
     * Serialize an access token into a String
     * @param accessToken The access token
     * @return the string
     */
    override fun serialize(accessToken: AccessToken): String {
        val jwt = JWT.create()
                .withJWTId(accessToken.id.id)
                .withIssuer(JwtAccessTokenSerializerImpl::class.qualifiedName)
                .withAudience(JwtAccessTokenSerializerImpl::class.qualifiedName)
                .withSubject(accessToken.user.id.toString())
                .withIssuedAt(Date.from(accessToken.created))
                .withExpiresAt(Date.from(accessToken.expires))
                .sign(signingAlgorithm)

        LOG.debug("Generated JWT {} for Access Token {}", jwt, accessToken)
        return jwt
    }

    /**
     * Deserialize an access token from a string
     * @param accessToken The string
     * @return the acceess token
     */
    override fun deserialize(accessToken: String): AccessToken {
        val decoded = try {
            JWT.require(signingAlgorithm)
                    .withIssuer(JwtAccessTokenSerializerImpl::class.qualifiedName)
                    .withAudience(JwtAccessTokenSerializerImpl::class.qualifiedName)
                    .build()
                    .verify(accessToken)
        } catch (e: TokenExpiredException) {
            LOG.info("Access token has already expired", e)
            throw InvalidAccessTokenException("Access token has already expired")
        } catch (e: JWTVerificationException) {
            LOG.warn("Invalid access token provided", e)
            throw InvalidAccessTokenException("Invalid access token provided")
        }

        val result = AccessToken(
                id = AccessTokenId(decoded.id),
                user = UserId(UUID.fromString(decoded.subject)),
                created = decoded.issuedAt.toInstant(),
                expires = decoded.expiresAt.toInstant()
        )

        LOG.debug("Generated Access Token {} from JWT {}", result, accessToken)
        return result
    }
}
