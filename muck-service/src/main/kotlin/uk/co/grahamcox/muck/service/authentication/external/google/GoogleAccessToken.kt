package uk.co.grahamcox.muck.service.authentication.external.google

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Representation of the Access Token from Google
 */
data class GoogleAccessToken(
        @JsonProperty("access_token") val accessToken: String,
        @JsonProperty("token_type") val tokenType: String,
        @JsonProperty("expires_in") val expiresIn: Int,
        @JsonProperty("id_token") val idToken: String
)
