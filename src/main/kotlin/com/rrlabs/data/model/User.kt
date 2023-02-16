package com.rrlabs.data.model

import io.ktor.server.auth.*

data class User(
    val email: String,
    val name: String,
    val hashPassword: String
): Principal
