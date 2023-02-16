package com.rrlabs.repository

import com.rrlabs.data.model.Note
import com.rrlabs.data.model.User
import com.rrlabs.data.table.NoteTable
import com.rrlabs.data.table.UserTable
import com.rrlabs.repository.DatabaseFactory.dbQuery
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

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

    suspend fun findUserByEmail(email: String) = dbQuery {
        UserTable.select { UserTable.email.eq(email) }
            .map { rowToUser(it) }
            .singleOrNull()
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

    suspend fun addNote(note: Note, email: String) {
        dbQuery {
            NoteTable.insert { nt ->
                nt[NoteTable.id] = note.id
                nt[NoteTable.userEmail] = email
                nt[NoteTable.noteTitle] = note.noteTitle
                nt[NoteTable.description] = note.description
                nt[NoteTable.date] = note.date
            }
        }
    }

    suspend fun getAllNotes(email: String): List<Note> = dbQuery {
        NoteTable.select {
            NoteTable.userEmail.eq(email)
        }.mapNotNull { rowToNote(it) }
    }

    suspend fun updateNote(note: Note, email: String) {
        dbQuery {
            NoteTable.update(
                where = {
                    NoteTable.userEmail.eq(email) and NoteTable.id.eq(note.id)
                }
            ) { nt ->
                nt[NoteTable.noteTitle] = note.noteTitle
                nt[NoteTable.description] = note.description
                nt[NoteTable.date] = note.date
            }
        }
    }

    suspend fun deleteNote(id: String, email: String) {
        dbQuery {
            NoteTable.deleteWhere { NoteTable.userEmail.eq(email) and NoteTable.id.eq(id) }
        }
    }

    private fun rowToNote(row: ResultRow?): Note? {
        if (row == null) {
            return null
        }

        return Note(
            id = row[NoteTable.id],
            noteTitle = row[NoteTable.noteTitle],
            description = row[NoteTable.description],
            date = row[NoteTable.date],
        )
    }
}