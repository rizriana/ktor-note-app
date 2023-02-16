package com.rrlabs.Routes

import com.rrlabs.auth.JwtService
import com.rrlabs.data.model.LoginRequest
import com.rrlabs.data.model.RegisterRequest
import com.rrlabs.data.model.SimpleResponse
import com.rrlabs.data.model.User
import com.rrlabs.repository.Repo
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.io.IOException

fun Route.userRoutes(
    db: Repo,
    jwtService: JwtService,
    hashFunction: (String) -> String
) {
    post("/v1/users/register") {
        val registerRequest = try {
            call.receive<RegisterRequest>()
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "Missing Fields"))
            return@post
        }

        try {
            val user = User(registerRequest.email, registerRequest.name, hashFunction(registerRequest.password))
            db.addUser(user)
            call.respond(HttpStatusCode.OK, SimpleResponse(true, jwtService.generateToken(user)))
        } catch (e: Exception) {
            call.respond(HttpStatusCode.Conflict, SimpleResponse(false, e.message ?: "Some Problem Occurred!"))
        }
    }

    post("/v1/users/login") {
        val loginRequest = try {
            call.receive<LoginRequest>()
        } catch (e: IOException) {
            call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "Missing Fields"))
            return@post
        }

        try {
            val user = db.findUserByEmail(loginRequest.email)
            if (user == null) {
                call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "Wrong Email Id"))
            } else {
                if (user.hashPassword == hashFunction(loginRequest.password)) {
                    call.respond(HttpStatusCode.OK, SimpleResponse(true, jwtService.generateToken(user)))
                } else {
                    call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "Password Incorrect!"))
                }
            }
        } catch (e: Exception) {
            call.respond(HttpStatusCode.Conflict, SimpleResponse(false, e.message ?: "Some Problem Occurred!"))
        }
    }
}