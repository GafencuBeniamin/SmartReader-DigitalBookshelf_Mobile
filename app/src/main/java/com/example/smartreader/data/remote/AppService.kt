package com.example.scd_proiect_mobile_android.data.remote

import com.example.smartreader.data.entities.LogInCredentials
import com.example.smartreader.data.entities.SignUpCredentials
import com.example.smartreader.data.entities.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AppService {
    @POST("login")
    suspend fun logInUser(@Body logInCredentials: LogInCredentials): Response<User>

    @POST("register")
    suspend fun signUpUser(@Body signUpCredentials: SignUpCredentials): Response<User>
}