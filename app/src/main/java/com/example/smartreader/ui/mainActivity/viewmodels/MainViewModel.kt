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
    val editedBook = MutableLiveData<Resource<Book>>()
    val deletedBook = MutableLiveData<Resource<Book>>()
    val currentPhotoPath = MutableLiveData<String?>()

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

    fun editBook(id: String, book: Book){
        viewModelScope.launch {
            val result = repository.remote.editBook(id,book)
            editedBook.postValue(result)
        }

    }
    fun deleteBook(id: String){
        viewModelScope.launch {
            val result = repository.remote.deleteBook(id)
            deletedBook.postValue(result)
        }
    }

    fun getMyDetails(){
        viewModelScope.launch {
            val result = repository.remote.getMyDetails()
            userDetails.postValue(result)
        }
    }
    fun resetState() {
        userLibrary.value = Resource.loading(null)
        book.value = Resource.loading(null)
        userDetails.value = Resource.loading(null)
        createdBook.value = Resource.loading(null)
        editedBook.value = Resource.loading(null)
        deletedBook.value = Resource.loading(null)
        currentPhotoPath.value = null
    }

}