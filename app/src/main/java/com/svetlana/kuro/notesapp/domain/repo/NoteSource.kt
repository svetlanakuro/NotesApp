package com.svetlana.kuro.notesapp.domain.repo

import com.svetlana.kuro.notesapp.domain.NoteEntity

interface NoteSource {

    fun init(): MutableList<NoteEntity>

    fun getNoteData(position: Int): NoteEntity?

    fun deleteNoteData(position: Int): NoteEntity?
    fun updateNoteData(position: Int, noteData: NoteEntity)
    fun addNoteData(noteData: NoteEntity)
    fun clearNoteData()
    fun size(): Int
}