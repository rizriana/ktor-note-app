package com.rrlabs.plugins

import com.rrlabs.repository.DatabaseFactory

fun createDatabase() {
    DatabaseFactory.init()
}