package com.example.smartreader.data.remote

import com.example.smartreader.data.entities.Book
import com.example.smartreader.data.entities.LogInCredentials
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
    @GET("books/{id}")
    suspend fun  getBookById(@Path("id") id:String) : Response<Book>

    @POST("books/createNewBook")
    suspend fun createBook(@Body book: Book): Response<Book>

    @PUT("books/updateBookByUser/{id}")
    suspend fun editBook(@Path("id") id:String, @Body book: Book) : Response<Book>

    @DELETE("books/removeBookByUser/{id}")
    suspend fun deleteBook(@Path("id") id: String) : Response<Book>

    /**NOTES*/


    /**USER*/
    @GET("user/myDetails")
    suspend fun getMyDetails() : Response<User>
}