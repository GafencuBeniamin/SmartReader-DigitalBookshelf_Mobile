package com.example.smartreader.ui.mainActivity.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartreader.data.entities.Book
import com.example.smartreader.data.entities.BookState
import com.example.smartreader.data.entities.BookStatus
import com.example.smartreader.data.entities.Note
import com.example.smartreader.data.entities.User
import com.example.smartreader.data.entities.UserRole
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
    val pendingBooks = MutableLiveData<Resource<List<Book>>>()
    val publicBooks = MutableLiveData<Resource<List<Book>>>()
    val book = MutableLiveData<Resource<Book>>()
    val userDetails = MutableLiveData<Resource<User>>()
    val userEdited = MutableLiveData<Resource<User>>()
    val createdBook = MutableLiveData<Resource<Book>>()
    val addedBook = MutableLiveData<Resource<Book>>()
    val editedBook = MutableLiveData<Resource<Book>>()
    val deletedBook = MutableLiveData<Resource<Book>>()
    val currentPhotoPath = MutableLiveData<String?>()
    val note = MutableLiveData<Resource<Note>>()
    val deletedNote = MutableLiveData<Resource<Note>>()
    val editedNote = MutableLiveData<Resource<Note>>()
    val createdNote= MutableLiveData<Resource<Note>>()
    val notesFromBook = MutableLiveData<Resource<List<Note>>>()
    val bookWithChangedStatus = MutableLiveData<Resource<Book>>()

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

    fun getPendingBooks(){
        viewModelScope.launch {
            val result = repository.remote.getPendingBooks()
            pendingBooks.postValue(result)
        }
    }
    fun searchPublicBooks(keyword: String){
        viewModelScope.launch {
            val result = repository.remote.searchPublicBooks(keyword)
            publicBooks.postValue(result)
        }
    }

    fun createBook(book: Book){
        viewModelScope.launch {
            val result = repository.remote.createBook(book)
            createdBook.postValue(result)
        }
    }
    fun addPublicBookToLibrary(id: String){
        viewModelScope.launch {
            val result = repository.remote.addPublicBookToLibrary(id)
            addedBook.postValue(result)
        }
    }
    fun removeBookFromLibrary(id: String){
        viewModelScope.launch {
            val result = repository.remote.removeBookFromLibrary(id)
            deletedBook.postValue(result)
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
    fun changeBookStatus(id: String, bookStatus: BookStatus){
        viewModelScope.launch{
            val result = repository.remote.changeBookStatus(id, bookStatus)
            bookWithChangedStatus.postValue(result)
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
    fun editNote(id:String, note:Note){
        viewModelScope.launch {
            val result = repository.remote.editNote(id,note)
            editedNote.postValue(result)
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
    fun changeUserRole(username: String, role: UserRole){
        viewModelScope.launch {
            val result = repository.remote.changeUserRole(username,role)
            userEdited.postValue(result)
        }
    }
    fun resetState() {
        userLibrary.value = Resource.loading(null)
        pendingBooks.value = Resource.loading(null)
        publicBooks.value = Resource.loading(null)
        book.value = Resource.loading(null)
        userDetails.value = Resource.loading(null)
        userEdited.value = Resource.loading(null)
        createdBook.value = Resource.loading(null)
        addedBook. value = Resource.loading(null)
        editedBook.value = Resource.loading(null)
        deletedBook.value = Resource.loading(null)
        currentPhotoPath.value = null
        note.value = Resource.loading(null)
        deletedNote.value = Resource.loading(null)
        editedNote.value = Resource.loading(null)
        createdNote.value = Resource.loading(null)
        notesFromBook.value = Resource.loading(null)
        bookWithChangedStatus.value = Resource.loading(null)
    }

    fun resetBookWithChangedStatus(){
        bookWithChangedStatus.value = Resource.loading(null)
    }

}