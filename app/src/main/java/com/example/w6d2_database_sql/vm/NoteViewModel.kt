package com.example.w6d2_database_sql.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.w6d2_database_sql.DB.NoteDatabase
import com.example.w6d2_database_sql.dao.NoteDao
import com.example.w6d2_database_sql.model.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoteViewModel(application: Application) : AndroidViewModel(application) {
    private val noteDao: NoteDao
    val allNotes: LiveData<List<Note>>

    init {
        val database = NoteDatabase.getDatabase(application)
        noteDao = database.noteDao()
        allNotes = noteDao.getAllNotes()
    }

    // Run database operations on IO thread
    fun insert(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            noteDao.insert(note)
        }
    }

    fun update(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            noteDao.update(note)
        }
    }

    fun delete(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            noteDao.delete(note)
        }
    }
}
