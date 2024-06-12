package com.example.smartreader.ui.mainActivity.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartreader.data.entities.Book
import com.example.smartreader.data.entities.User
import com.example.smartreader.data.repository.AppRepository
import com.example.smartreader.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val repository : AppRepository
) : ViewModel() {
    val userLibrary = MutableLiveData<Resource<List<Book>>>()
    val book = MutableLiveData<Resource<Book>>()
    val userDetails = MutableLiveData<Resource<User>>()
    val createdBook = MutableLiveData<Resource<Book>>()

    fun getMyBooks(){
        viewModelScope.launch {
            val result = repository.remote.getMyBooks()
            userLibrary.postValue(result)
        }
    }

    fun getBookById(id : String ){
        viewModelScope.launch {
            val result = repository.remote.getBookById(id)
            book.postValue(result)
        }
    }

    fun createBook(book: Book){
        viewModelScope.launch {
            val result = repository.remote.createBook(book)
            createdBook.postValue(result)
        }
    }

    fun getMyDetails(){
        viewModelScope.launch {
            val result = repository.remote.getMyDetails()
            userDetails.postValue(result)
        }
    }

}