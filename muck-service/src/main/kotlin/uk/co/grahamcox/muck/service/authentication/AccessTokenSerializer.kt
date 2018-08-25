package uk.co.grahamcox.muck.service.authentication

/**
 * Mechanism to serialize an Access Token to a string and back
 */
interface AccessTokenSerializer {
    /**
     * Serialize an access token into a String
     * @param accessToken The access token
     * @return the string
     */
    fun serialize(accessToken: AccessToken) : String

    /**
     * Deserialize an access token from a string
     * @param accessToken The string
     * @return the acceess token
     */
    fun deserialize(accessToken: String) : AccessToken
}
