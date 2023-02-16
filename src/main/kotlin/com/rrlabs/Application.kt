package com.rrlabs

import io.ktor.server.application.*
import com.rrlabs.plugins.*

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    createDatabase()
    configureSerialization()
    configureSecurity()
    configureRouting()
}
