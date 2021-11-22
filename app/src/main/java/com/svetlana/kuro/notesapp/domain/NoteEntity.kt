package com.svetlana.kuro.notesapp.domain

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "note_table")
data class NoteEntity(
    var noteTitle: String?,
    var noteDescription: String?,
    var noteLink: String?
) {
    @PrimaryKey(autoGenerate = true)
    var noteId: Int = 0
}