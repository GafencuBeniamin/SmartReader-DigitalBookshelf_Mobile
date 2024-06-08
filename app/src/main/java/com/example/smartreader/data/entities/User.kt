package com.example.smartreader.data.entities

data class User (
    val id: Int? = null,
    val username: String? = null,
    val email: String? = null,
    val role: UserRole? = null,
    val picture: String? = null,
    val friends: Set<User>? = null,
    val notes: Set<Note>? = null,
    val books: Set<Book>? = null,
    val token: String? = null
)