package com.example.smartreader.data.remote

import com.example.smartreader.data.entities.Book
import com.example.smartreader.data.entities.BookState
import com.example.smartreader.data.entities.BookStatus
import com.example.smartreader.data.entities.LogInCredentials
import com.example.smartreader.data.entities.Note
import com.example.smartreader.data.entities.SignUpCredentials
import com.example.smartreader.data.entities.User
import com.example.smartreader.data.entities.UserRole
import javax.inject.Inject

class AppRemoteDataSource @Inject constructor(
    private val appService: AppService
) : BaseDataSource() {
    suspend fun logInUser(logInCredentials: LogInCredentials) = getResult{ appService.logInUser(logInCredentials)}
    suspend fun signUpUser(signUpCredentials: SignUpCredentials) = getResult{ appService.signUpUser(signUpCredentials)}
    suspend fun getMyBooks() = getResult { appService.getMyBooks() }
    suspend fun getBookById(id: String) =  getResult { appService.getBookById(id) }
    suspend fun getPendingBooks() = getResult {appService.getPendingBooks()}
    suspend fun searchPublicBooks(keyword: String)= getResult { appService.searchPublicBooks(keyword) }
    suspend fun createBook(book: Book)= getResult { appService.createBook(book) }
    suspend fun addPublicBookToLibrary(id: String) = getResult { appService.addPublicBookToLibrary(id) }
    suspend fun removeBookFromLibrary(id: String) = getResult { appService.removeBookFromLibrary(id) }
    suspend fun editBook(id: String, book: Book)= getResult { appService.editBook(id, book) }
    suspend fun editPublicBook(id: String, bookState: BookState)= getResult { appService.editPublicBook(id, bookState) }
    suspend fun changeBookStatus(id: String, bookStatus: BookStatus)= getResult { appService.changeBookStatus(id, bookStatus) }
    suspend fun deleteBook(id: String)= getResult { appService.deleteBook(id) }
    suspend fun getNoteById(id: String) =  getResult { appService.getNoteById(id) }
    suspend fun getMyNotesFromBook(bookId : String)= getResult { appService.getMyNotesFromBook(bookId) }
    suspend fun createNote(note: Note) = getResult { appService.createNote(note) }
    suspend fun editNote(id:String, note:Note) = getResult { appService.editNote(id,note) }
    suspend fun deleteNote(id: String)= getResult { appService.deleteNote(id) }
    suspend fun getMyDetails() =  getResult { appService.getMyDetails() }
    suspend fun changeUserRole(username: String, role: UserRole) = getResult { appService.changeUserRole(username,role) }
    suspend fun updateMyDetails(user : User) = getResult { appService.updateMyDetails(user) }
}