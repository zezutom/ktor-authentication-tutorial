package com.tomaszezula.ktorauth.ch04.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserInfo(
    val id: String,
    val name: String,
    @SerialName("given_name")
    val givenName: String,
    @SerialName("family_name")
    val familyName: String,
    val picture: String,
    val locale: String
)
/**
 * {
 *   "id": "100267291539832074631",
 *   "name": "Tomas Zezula",
 *   "given_name": "Tomas",
 *   "family_name": "Zezula",
 *   "picture": "https://lh3.googleusercontent.com/a/ACg8ocKVRuMHgJ4Wvf91aaMTTx1wzDxyZJQ3eqyFl2RF_abDcY3l=s96-c",
 *   "locale": "en-GB"
 * }
 */
