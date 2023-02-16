package com.rrlabs.plugins

import com.rrlabs.Routes.userRoutes
import com.rrlabs.auth.JwtService
import com.rrlabs.auth.hash
import com.rrlabs.data.model.User
import com.rrlabs.repository.Repo
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {

    val db = Repo()
    val jwtService = JwtService()
    val hashFunction = { s: String -> hash(s) }

    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        get("/token") {
            val email = call.request.queryParameters["email"] ?: ""
            val name = call.request.queryParameters["name"] ?: ""
            val password = call.request.queryParameters["password"] ?: ""

            val user = User(
                email, name, hashFunction(password)
            )
            call.respond(jwtService.generateToken(user))
        }

        userRoutes(db, jwtService, hashFunction)
    }
}
