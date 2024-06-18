package com.example.smartreader.data.entities

data class Book (
    val id: Int? = null,
    val bookStates: Map<String,BookState>? = null,
    val author: Set<String>? = null,
    val title: String? = null,
    val noOfPages: Int? = null,
    val language: String? = null,
    val image: String? = null,
    val genre: String? = null,
    val isPublic: BookStatus? = null,
    val createdBy: Int? = null,
    val editure: String? = null
)