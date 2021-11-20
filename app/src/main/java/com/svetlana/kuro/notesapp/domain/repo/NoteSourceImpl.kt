package com.svetlana.kuro.notesapp.domain.repo

import com.svetlana.kuro.notesapp.domain.NoteEntity
import java.util.*

class NoteSourceImpl : NoteSource {

    private var notes: MutableList<NoteEntity>? = null

    override fun init(): MutableList<NoteEntity> {
        notes = mutableListOf(
            NoteEntity(
                title = "Note one",
                description = "Description Note first",
                isCompleted = false,
                date = GregorianCalendar.getInstance().time
            ),
            NoteEntity(
                title = "Note two",
                description = "Description Note second",
                isCompleted = false,
                date = GregorianCalendar.getInstance().time
            ),
            NoteEntity(
                title = "Note three",
                description = "Description Note third",
                isCompleted = false,
                date = GregorianCalendar.getInstance().time
            ),
            NoteEntity(
                title = "Note four",
                description = "Description Note fourth",
                isCompleted = false,
                date = GregorianCalendar.getInstance().time
            ),
            NoteEntity(
                title = "Note five",
                description = "Description Note fifth",
                isCompleted = false,
                date = GregorianCalendar.getInstance().time
            ),
            NoteEntity(
                title = "Note six",
                description = "Description Note sixth",
                isCompleted = false,
                date = GregorianCalendar.getInstance().time
            )
        )

        return notes as MutableList<NoteEntity>
    }

    override fun getNoteData(position: Int): NoteEntity? {
        return notes?.get(position)
    }

    override fun deleteNoteData(position: Int): NoteEntity? {
        return notes?.removeAt(position)
    }

    override fun updateNoteData(position: Int, noteData: NoteEntity) {
        notes?.set(position, noteData)
    }

    override fun addNoteData(noteData: NoteEntity) {
        notes?.add(noteData)
    }

    override fun clearNoteData() {
        notes?.clear()
    }

    override fun size(): Int {
        return if (notes != null) {
            notes!!.size
        } else 0
    }
}