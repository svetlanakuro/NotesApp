package com.svetlana.kuro.notesapp.ui.pages

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.svetlana.kuro.notesapp.R
import com.svetlana.kuro.notesapp.databinding.ActivityAddNoteBinding
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.MalformedURLException
import java.net.URL
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection

class AddEditNoteActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_ID = "EXTRA_ID"
        const val EXTRA_TITLE = "EXTRA_TITLE"
        const val EXTRA_DESCRIPTION = "EXTRA_DESCRIPTION"
        const val EXTRA_LINK = "EXTRA_LINK"
    }

    private val binding: ActivityAddNoteBinding by lazy {
        ActivityAddNoteBinding.inflate(
            layoutInflater
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close)

        if (intent.hasExtra(EXTRA_ID)) {
            title = "Edit Note"
            binding.editTextTitle.setText(intent.getStringExtra(EXTRA_TITLE))
            binding.editTextDescription.setText(intent.getStringExtra(EXTRA_DESCRIPTION))
            binding.editTextLink.setText(intent.getStringExtra(EXTRA_LINK))
            linkPreview()
        } else {
            title = "Add Note"
        }

    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun linkPreview() {
        try {
            val uri = URL(binding.editTextLink.text.toString())
            val handler = Handler()
            Thread {
                var urlConnection: HttpsURLConnection? = null
                try {
                    urlConnection = uri.openConnection() as HttpsURLConnection
                    urlConnection.requestMethod = "GET"
                    urlConnection.readTimeout = 10000
                    val reader =
                        BufferedReader(InputStreamReader(urlConnection.inputStream))
                    val result = getLines(reader)

                    handler.post {
                        binding.webView.loadData(result, "text/html; charset=utf-8", "utf-8")
                    }
                } catch (e: Exception) {
                    Log.e("", "Fail connection", e)
                    e.printStackTrace()
                } finally {
                    urlConnection?.disconnect()
                }
            }.start()
        } catch (e: MalformedURLException) {
            Log.e("", "Fail URI", e)
            e.printStackTrace()
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getLines(reader: BufferedReader): String {
        return reader.lines().collect(Collectors.joining("\n"))
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
            return
        }

        val data = Intent().apply {
            putExtra(EXTRA_TITLE, binding.editTextTitle.text.toString())
            putExtra(EXTRA_DESCRIPTION, binding.editTextDescription.text.toString())
            putExtra(EXTRA_LINK, binding.editTextLink.text.toString())
            if (intent.getIntExtra(EXTRA_ID, -1) != -1) {
                putExtra(EXTRA_ID, intent.getIntExtra(EXTRA_ID, -1))
            }
        }

        setResult(Activity.RESULT_OK, data)
        finish()
    }
}