package com.example.w6d2_database_sql.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.w6d2_database_sql.model.Note


@Dao
interface NoteDao {
    @Insert
    suspend fun insert(note: Note)
    @Update
    suspend fun update(note:Note)
    @Delete
    suspend fun delete(note:Note)
    @Query("SELECT * FROM notes ")
    fun getAllNotes(): LiveData<List<Note>>
}