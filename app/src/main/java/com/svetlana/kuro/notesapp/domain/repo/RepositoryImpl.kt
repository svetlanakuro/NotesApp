package com.svetlana.kuro.notesapp.domain.repo

import com.svetlana.kuro.notesapp.domain.NoteEntity
import java.util.*

class RepositoryImpl : Repository {
    override fun getNoteFromLocalStorage(): List<NoteEntity> {
        return listOf(
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
    }

    override fun getNoteFromServer(): List<NoteEntity> {
        TODO("Not yet implemented")
    }
}