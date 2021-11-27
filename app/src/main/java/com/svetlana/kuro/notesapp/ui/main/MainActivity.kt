package com.svetlana.kuro.notesapp.ui.main

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.svetlana.kuro.notesapp.R
import com.svetlana.kuro.notesapp.databinding.ActivityMainBinding
import com.svetlana.kuro.notesapp.domain.NoteEntity
import com.svetlana.kuro.notesapp.ui.pages.AddEditNoteActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        const val ADD_NOTE_REQUEST = 1
        const val EDIT_NOTE_REQUEST = 2
    }

    object NoteAnalytics {
        fun logEvent(context: Context, event: String) {
            context.startService(Intent(context, NoteAnalyticsService::class.java).apply {
                putExtra(NOTE_ANALYTICS_SERVICE_EXTRA, event)
            })
        }
    }

    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private val noteViewModel: NoteViewModel by lazy { ViewModelProvider(this)[NoteViewModel::class.java] }

    private val localMainReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.getStringExtra(NETWORK_STATUS)) {
                AVAILABLE_STATUS -> {
                    context?.let { onNetworkRestored(it) }
                }
                LOST_STATUS -> {
                    context?.let { onNetworkLost(it) }
                }
            }
        }
    }

    private fun onNetworkRestored(context: Context) {
        Toast.makeText(context, "Connection restored", Toast.LENGTH_LONG).show()
    }

    private fun onNetworkLost(context: Context) {
        Toast.makeText(context, "Connection lost", Toast.LENGTH_LONG).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.buttonAddNote.setOnClickListener {
            startActivityForResult(
                Intent(this, AddEditNoteActivity::class.java),
                ADD_NOTE_REQUEST
            )
        }

        binding.recyclerView.layoutManager = GridLayoutManager(this, 2)
        binding.recyclerView.setHasFixedSize(true)

        val adapter = NoteAdapter()

        binding.recyclerView.adapter = adapter

        noteViewModel.getAllNotes().observe(this, {
            adapter.submitList(it)
        })

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.LEFT.or(
                ItemTouchHelper.RIGHT
            )
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                noteViewModel.delete(adapter.getNoteAt(viewHolder.adapterPosition))
                Toast.makeText(baseContext, "Note Deleted!", Toast.LENGTH_SHORT).show()
                NoteAnalytics.logEvent(baseContext, "MainActivity: Note Deleted!")
            }
        }
        ).attachToRecyclerView(recycler_view)

        adapter.setOnItemClickListener(object : NoteAdapter.OnItemClickListener {
            override fun onItemClick(note: NoteEntity) {
                val intent = Intent(baseContext, AddEditNoteActivity::class.java)
                intent.putExtra(AddEditNoteActivity.EXTRA_ID, note.noteId)
                intent.putExtra(AddEditNoteActivity.EXTRA_TITLE, note.noteTitle)
                intent.putExtra(AddEditNoteActivity.EXTRA_DESCRIPTION, note.noteDescription)
                intent.putExtra(AddEditNoteActivity.EXTRA_LINK, note.noteLink)

                startActivityForResult(intent, EDIT_NOTE_REQUEST)
            }
        })

        LocalBroadcastManager.getInstance(this)
            .registerReceiver(localMainReceiver, IntentFilter(NETWORK_STATUS_INTENT_FILTER))
        NetworkMonitor(application).startNetworkCallback()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.delete_all_notes -> {
                noteViewModel.deleteAllNotes()
                Toast.makeText(this, "All notes deleted!", Toast.LENGTH_SHORT).show()
                NoteAnalytics.logEvent(this, "MainActivity: All notes deleted!")
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ADD_NOTE_REQUEST && resultCode == Activity.RESULT_OK) {
            val newNote = data?.let {
                NoteEntity(
                    data.getStringExtra(AddEditNoteActivity.EXTRA_TITLE),
                    data.getStringExtra(AddEditNoteActivity.EXTRA_DESCRIPTION),
                    data.getStringExtra(AddEditNoteActivity.EXTRA_LINK)
                )
            }
            if (newNote != null) {
                noteViewModel.insert(newNote)
            }

            Toast.makeText(this, "Note saved!", Toast.LENGTH_SHORT).show()
            NoteAnalytics.logEvent(this, "MainActivity: Note saved!")
        } else if (requestCode == EDIT_NOTE_REQUEST && resultCode == Activity.RESULT_OK) {
            val id = data?.getIntExtra(AddEditNoteActivity.EXTRA_ID, -1)

            if (id == -1) {
                Toast.makeText(this, "Could not update! Error!", Toast.LENGTH_SHORT).show()
                NoteAnalytics.logEvent(this, "MainActivity: Could not update! Error!")
            }

            val updateNote = NoteEntity(
                data!!.getStringExtra(AddEditNoteActivity.EXTRA_TITLE),
                data.getStringExtra(AddEditNoteActivity.EXTRA_DESCRIPTION),
                data.getStringExtra(AddEditNoteActivity.EXTRA_LINK)
            )
            updateNote.noteId = data.getIntExtra(AddEditNoteActivity.EXTRA_ID, -1)
            noteViewModel.update(updateNote)
        } else {
            Toast.makeText(this, "Note not saved!", Toast.LENGTH_SHORT).show()
            NoteAnalytics.logEvent(this, "MainActivity: Note not saved!")
        }

    }

    override fun onDestroy() {
        LocalBroadcastManager.getInstance(this)
            .unregisterReceiver(localMainReceiver)
        super.onDestroy()
    }
}