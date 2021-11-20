package com.svetlana.kuro.notesapp.domain

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "note_table")
data class NoteEntity(
    var title: String?,
    var description: String?
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}