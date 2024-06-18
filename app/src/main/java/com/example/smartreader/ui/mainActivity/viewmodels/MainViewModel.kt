package com.example.smartreader.ui.mainActivity.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartreader.data.entities.Book
import com.example.smartreader.data.entities.BookState
import com.example.smartreader.data.entities.Note
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
    val note = MutableLiveData<Resource<Note>>()
    val deletedNote = MutableLiveData<Resource<Note>>()
    val editedNote = MutableLiveData<Resource<Note>>()
    val createdNote= MutableLiveData<Resource<Note>>()
    val notesFromBook = MutableLiveData<Resource<List<Note>>>()

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
    fun editPublicBook(id: String, bookState: BookState){
        viewModelScope.launch {
            val result = repository.remote.editPublicBook(id, bookState)
            editedBook.postValue(result)
        }
    }
    fun deleteBook(id: String){
        viewModelScope.launch {
            val result = repository.remote.deleteBook(id)
            deletedBook.postValue(result)
        }
    }

    fun getNoteById(id : String ){
        viewModelScope.launch {
            val result = repository.remote.getNoteById(id)
            note.postValue(result)
        }
    }
    fun getMyNotesFromBook(bookId : String){
        viewModelScope.launch {
            val result = repository.remote.getMyNotesFromBook(bookId)
            notesFromBook.postValue(result)
        }
    }

    fun createNote(note:Note){
        viewModelScope.launch {
            val result = repository.remote.createNote(note)
            createdNote.postValue(result)
        }
    }

    fun deleteNote(id: String){
        viewModelScope.launch {
            val result = repository.remote.deleteNote(id)
            deletedNote.postValue(result)
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
        note.value = Resource.loading(null)
        deletedNote.value = Resource.loading(null)
        editedNote.value = Resource.loading(null)
        notesFromBook.value = Resource.loading(null)
        createdNote.value = Resource.loading(null)
        notesFromBook.value = Resource.loading(null)
        currentPhotoPath.value = null
    }

}