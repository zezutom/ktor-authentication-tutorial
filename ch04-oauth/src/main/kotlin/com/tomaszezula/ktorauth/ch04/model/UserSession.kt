package com.tomaszezula.ktorauth.ch04.model

data class UserSession(val state: String, val token: String)