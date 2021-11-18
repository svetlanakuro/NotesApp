package com.svetlana.kuro.notesapp.domain.repo

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import com.svetlana.kuro.notesapp.domain.NoteEntity

class NoteRepository(application: Application) : NoteDao {

    private var noteDao: NoteDao

    private var allNotes: LiveData<List<NoteEntity>>

    init {
        val database: NoteDatabase = NoteDatabase.getInstance(
            application.applicationContext
        )!!
        noteDao = database.noteDao()
        allNotes = noteDao.getAllNotes()
    }

    override fun insert(note: NoteEntity) {
        val insertNoteAsyncTask = InsertNoteAsyncTask(noteDao).execute(note)
    }

    override fun update(note: NoteEntity) {
        val updateNoteAsyncTask = UpdateNoteAsyncTask(noteDao).execute(note)
    }


    override fun delete(note: NoteEntity) {
        val deleteNoteAsyncTask = DeleteNoteAsyncTask(noteDao).execute(note)
    }

    override fun deleteAllNotes() {
        val deleteAllNotesAsyncTask = DeleteAllNotesAsyncTask(
            noteDao
        ).execute()
    }

    override fun getAllNotes(): LiveData<List<NoteEntity>> {
        return allNotes
    }

    companion object {
        private class InsertNoteAsyncTask(noteDao: NoteDao) : AsyncTask<NoteEntity, Unit, Unit>() {
            val noteDao = noteDao

            override fun doInBackground(vararg p0: NoteEntity?) {
                noteDao.insert(p0[0]!!)
            }
        }

        private class UpdateNoteAsyncTask(noteDao: NoteDao) : AsyncTask<NoteEntity, Unit, Unit>() {
            val noteDao = noteDao

            override fun doInBackground(vararg p0: NoteEntity?) {
                noteDao.update(p0[0]!!)
            }
        }

        private class DeleteNoteAsyncTask(noteDao: NoteDao) : AsyncTask<NoteEntity, Unit, Unit>() {
            val noteDao = noteDao

            override fun doInBackground(vararg p0: NoteEntity?) {
                noteDao.delete(p0[0]!!)
            }
        }

        private class DeleteAllNotesAsyncTask(noteDao: NoteDao) : AsyncTask<Unit, Unit, Unit>() {
            val noteDao = noteDao

            override fun doInBackground(vararg p0: Unit?) {
                noteDao.deleteAllNotes()
            }
        }
    }
}