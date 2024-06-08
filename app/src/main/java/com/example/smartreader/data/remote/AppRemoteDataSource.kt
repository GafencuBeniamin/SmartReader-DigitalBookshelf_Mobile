package com.example.scd_proiect_mobile_android.data.remote

import com.example.smartreader.data.entities.LogInCredentials
import com.example.smartreader.data.entities.SignUpCredentials
import javax.inject.Inject

class AppRemoteDataSource @Inject constructor(
    private val appService: AppService
) : BaseDataSource() {
    suspend fun logInUser(logInCredentials: LogInCredentials) = getResult{ appService.logInUser(logInCredentials)}
    suspend fun signUpUser(signUpCredentials: SignUpCredentials) = getResult{ appService.signUpUser(signUpCredentials)}
}