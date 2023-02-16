package com.rrlabs.repository

import com.rrlabs.data.model.User
import com.rrlabs.data.table.UserTable
import com.rrlabs.repository.DatabaseFactory.dbQuery
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select

class Repo {

    suspend fun addUser(user: User) {
        dbQuery {
            UserTable.insert { userTable ->
                userTable[UserTable.email] = user.email
                userTable[UserTable.name] = user.name
                userTable[UserTable.hashPassword] = user.hashPassword
            }
        }
    }

    suspend fun findUserByEmail(email: String) {
        dbQuery {
            UserTable.select {
                UserTable.email.eq(email)
            }
                .map { rowToUser(it) }
                .singleOrNull()
        }
    }

    private fun rowToUser(row: ResultRow?): User? {
        if (row == null) {
            return null
        }

        return User(
            email = row[UserTable.email],
            name = row[UserTable.name],
            hashPassword = row[UserTable.hashPassword],
        )
    }
}