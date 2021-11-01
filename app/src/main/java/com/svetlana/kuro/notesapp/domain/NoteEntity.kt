package com.svetlana.kuro.notesapp.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class NoteEntity(
    var id: String? = null,
    var title: String? = null,
    var description: String? = null,
    var isCompleted: Boolean = false,
    var date: Date? = null
) : Parcelable {

    constructor(title: String?, description: String?, completed: Boolean, date: Date?) : this() {
        this.title = title
        this.description = description
        isCompleted = completed
        this.date = date
    }
}