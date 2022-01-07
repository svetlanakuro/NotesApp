package com.svetlana.kuro.notesapp.domain.repo

import android.content.Context
import android.os.AsyncTask
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.svetlana.kuro.notesapp.domain.NoteEntity

@Database(entities = [NoteEntity::class], version = 1, exportSchema = true)
abstract class NoteDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao

    companion object {
        private var instance: NoteDatabase? = null

        fun getInstance(context: Context): NoteDatabase? {
            if (instance == null) {
                synchronized(NoteDatabase::class) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        NoteDatabase::class.java, "note_database"
                    )
                        .fallbackToDestructiveMigration()
                        .addCallback(roomCallback)
                        .build()
                }
            }
            return instance
        }

        fun destroyInstance() {
            instance = null
        }

        private val roomCallback = object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                PopulateDbAsyncTask(instance)
                    .execute()
            }
        }
    }

    class PopulateDbAsyncTask(db: NoteDatabase?) : AsyncTask<Unit, Unit, Unit>() {
        private val noteDao = db?.noteDao()

        override fun doInBackground(vararg p0: Unit?) {
            noteDao?.insert(
                NoteEntity(
                    "Note one",
                    "Description Note first",
                    "Unknown Location"
                )
            )
            noteDao?.insert(
                NoteEntity(
                    "Note two",
                    "Description Note second",
                    "Unknown Location"
                )
            )
            noteDao?.insert(
                NoteEntity(
                    "Note three",
                    "Description Note third",
                    "Unknown Location"
                )
            )
            noteDao?.insert(
                NoteEntity(
                    "Note four",
                    "Description Note fourth",
                    "Unknown Location"
                )
            )
            noteDao?.insert(
                NoteEntity(
                    "Note five",
                    "Description Note fifth",
                    "Unknown Location"
                )
            )
            noteDao?.insert(
                NoteEntity(
                    "Note six",
                    "Description Note sixth",
                    "Unknown Location"
                )
            )
        }
    }

}