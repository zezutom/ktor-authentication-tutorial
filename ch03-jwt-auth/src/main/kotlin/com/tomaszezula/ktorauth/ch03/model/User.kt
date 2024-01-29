package com.tomaszezula.ktorauth.ch03.model

data class User(val name: String, val role: String, val password: String = "")