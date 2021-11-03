package com.svetlana.kuro.notesapp.domain.repo

import com.svetlana.kuro.notesapp.domain.NoteEntity

interface Repository {
    fun getNoteFromLocalStorage(): List<NoteEntity>
    fun getNoteFromServer(): List<NoteEntity>
}