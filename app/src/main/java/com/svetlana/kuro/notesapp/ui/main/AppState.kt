package com.svetlana.kuro.notesapp.ui.main

import com.svetlana.kuro.notesapp.domain.NoteEntity

sealed class AppState {
    data class Success(val notes: List<NoteEntity>) : AppState()
    data class Error(val error: Throwable) : AppState()
    object Loading : AppState()
}