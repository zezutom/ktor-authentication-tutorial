package com.tomaszezula.ktorauth.ch04.model

import kotlinx.serialization.Serializable

@Serializable
data class UserInfo(
    val id: String,
    val name: String,
    val locale: String
)
