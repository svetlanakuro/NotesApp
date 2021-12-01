package com.svetlana.kuro.notesapp.ui.pages

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import coil.api.load
import com.svetlana.kuro.notesapp.R
import com.svetlana.kuro.notesapp.databinding.ActivityAddNoteBinding
import com.svetlana.kuro.notesapp.domain.repo.MovieDBRepo
import com.svetlana.kuro.notesapp.domain.repo.RetrofitMovieDBRepoImpl
import com.svetlana.kuro.notesapp.ui.main.MainActivity

class AddEditNoteActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_ID = "EXTRA_ID"
        const val EXTRA_TITLE = "EXTRA_TITLE"
        const val EXTRA_DESCRIPTION = "EXTRA_DESCRIPTION"
    }

    private val binding: ActivityAddNoteBinding by lazy {
        ActivityAddNoteBinding.inflate(
            layoutInflater
        )
    }

    private val movieDBRepoEntity: MovieDBRepo by lazy { RetrofitMovieDBRepoImpl() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close)

        if (intent.hasExtra(EXTRA_ID)) {
            title = "Edit Note"
            binding.editTextTitle.setText(intent.getStringExtra(EXTRA_TITLE))
            binding.editTextDescription.setText(intent.getStringExtra(EXTRA_DESCRIPTION))

            movieDBRepoEntity.getMovieAsync(
                onSuccess = { movie ->
                    runOnUiThread {
                        binding.movieOfTheDay.text = getText(R.string.movie_of_the_day)
                        binding.resultMovieOriginalTitle.text =
                            movie.results?.let { movie.results[it.lastIndex].originalTitle }
                        binding.resultMovieOverview.text =
                            movie.results?.let { movie.results[it.lastIndex].overview }
                        binding.resultMoviePoster.load("https://image.tmdb.org/t/p/w500/" + movie.results?.let { movie.results[it.lastIndex].posterLink })
                    }
                },
                onError = {
                    runOnUiThread {
                        Toast.makeText(this, "Ошибка ${it.message}", Toast.LENGTH_SHORT).show()
                        MainActivity.NoteAnalytics.logEvent(this, "Ошибка ${it.message}")
                    }
                }
            )
            MainActivity.NoteAnalytics.logEvent(this, "AddEditNoteActivity: Edit Note")
        } else {
            title = "Add Note"
            MainActivity.NoteAnalytics.logEvent(this, "AddEditNoteActivity: Add Note")
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_note_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.save_note -> {
                saveNote()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun saveNote() {
        if (binding.editTextTitle.text.toString().trim()
                .isBlank() || binding.editTextDescription.text.toString().trim().isBlank()
        ) {
            Toast.makeText(this, "Can not insert empty note!", Toast.LENGTH_SHORT).show()
            MainActivity.NoteAnalytics.logEvent(
                this,
                "AddEditNoteActivity: Can not insert empty note!"
            )
            return
        }

        val data = Intent().apply {
            putExtra(EXTRA_TITLE, binding.editTextTitle.text.toString())
            putExtra(EXTRA_DESCRIPTION, binding.editTextDescription.text.toString())
            if (intent.getIntExtra(EXTRA_ID, -1) != -1) {
                putExtra(EXTRA_ID, intent.getIntExtra(EXTRA_ID, -1))
            }
        }

        setResult(Activity.RESULT_OK, data)
        finish()
    }
}