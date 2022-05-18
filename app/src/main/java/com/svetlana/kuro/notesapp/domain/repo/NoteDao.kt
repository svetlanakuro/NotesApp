package com.svetlana.kuro.notesapp.domain.repo

import androidx.lifecycle.LiveData
import androidx.room.*
import com.svetlana.kuro.notesapp.domain.NoteEntity

@Dao
interface NoteDao {

    @Insert
    fun insert(note: NoteEntity)

    @Update
    fun update(note: NoteEntity)

    @Delete
    fun delete(note: NoteEntity)

    @Query("DELETE FROM note_table")
    fun deleteAllNotes()

    @Query("SELECT * FROM note_table")
    fun getAllNotes(): LiveData<List<NoteEntity>>

}