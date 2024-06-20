package com.example.smartreader.data.remote

import com.example.smartreader.data.entities.Book
import com.example.smartreader.data.entities.BookState
import com.example.smartreader.data.entities.BookStatus
import com.example.smartreader.data.entities.LogInCredentials
import com.example.smartreader.data.entities.Note
import com.example.smartreader.data.entities.SignUpCredentials
import com.example.smartreader.data.entities.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface AppService {
    /**AUTH*/
    @POST("login")
    suspend fun logInUser(@Body logInCredentials: LogInCredentials): Response<User>
    @POST("register")
    suspend fun signUpUser(@Body signUpCredentials: SignUpCredentials): Response<User>

    /**BOOKS*/
    @GET("books/myBooks")
    suspend fun getMyBooks(): Response<List<Book>>
    @GET("books/getBookByIdByUser/{id}")
    suspend fun  getBookById(@Path("id") id:String) : Response<Book>
    @POST("books/createNewBook")
    suspend fun createBook(@Body book: Book): Response<Book>
    @PUT("books/updateBookByUser/{id}")
    suspend fun editBook(@Path("id") id:String, @Body book: Book) : Response<Book>
    @PUT("books/updatePublicBookByUser/{id}")
    suspend fun editPublicBook(@Path("id") id:String, @Body bookState: BookState) : Response<Book>
    @PUT("books/changeStatus/{id}")
    suspend fun changeBookStatus(@Path("id") id:String, @Body bookStatus: BookStatus): Response<Book>
    @DELETE("books/removeBookByUser/{id}")
    suspend fun deleteBook(@Path("id") id: String) : Response<Book>

    /**NOTES*/
    @GET("notes/{id}")
    suspend fun getNoteById(@Path("id") id:String) : Response<Note>
    @GET("notes/myNotesFromBook/{bookId}")
    suspend fun getMyNotesFromBook(@Path("bookId") bookId:String) : Response<List<Note>>
    @POST("notes/createNewNote")
    suspend fun createNote(@Body note: Note): Response<Note>
    @PUT("notes/updateNoteByUser/{id}")
    suspend fun editNote(@Path("id") id: String, @Body note: Note) : Response<Note>
    @DELETE("notes/removeNoteByUser/{id}")
    suspend fun deleteNote(@Path("id") id: String) : Response<Note>

    /**USER*/
    @GET("user/myDetails")
    suspend fun getMyDetails() : Response<User>
}