package com.svetlana.kuro.notesapp.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.svetlana.kuro.notesapp.domain.NoteEntity
import com.svetlana.kuro.notesapp.domain.repo.NoteRepository

class NoteViewModel(application: Application) : AndroidViewModel(application) {
    private var repository: NoteRepository =
        NoteRepository(application)
    private var allNotes: LiveData<List<NoteEntity>> = repository.getAllNotes()

    fun insert(note: NoteEntity) {
        repository.insert(note)
    }

    fun update(note: NoteEntity) {
        repository.update(note)
    }

    fun delete(note: NoteEntity) {
        repository.delete(note)
    }

    fun deleteAllNotes() {
        repository.deleteAllNotes()
    }

    fun getAllNotes(): LiveData<List<NoteEntity>> {
        return allNotes
    }
}