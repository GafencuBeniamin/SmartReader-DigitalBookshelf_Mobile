package com.example.smartreader.ui.loginActivity.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.SCD_proiect.data.repository.AppRepository
import com.example.smartreader.data.entities.LogInCredentials
import com.example.smartreader.data.entities.SignUpCredentials
import com.example.smartreader.data.entities.User
import com.example.smartreader.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    val repository : AppRepository
) : ViewModel() {
    val loggedInUser = MutableLiveData<Resource<User>>()
    val signedUpUser = MutableLiveData<Resource<User>>()

    fun logInUser(logInCredentials: LogInCredentials){
        viewModelScope.launch {
            val result = repository.remote.logInUser(logInCredentials)
            loggedInUser.postValue(result)
        }
    }
    fun signUpUser(signUpCredentials: SignUpCredentials){
        viewModelScope.launch {
            val result = repository.remote.signUpUser(signUpCredentials)
            signedUpUser.postValue(result)
        }
    }
}