package com.rrlabs

import com.rrlabs.plugins.*
import io.ktor.server.application.*
import io.ktor.server.locations.*

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    createDatabase()
    configureSerialization()
    configureSecurity()
    configureRouting()
}
