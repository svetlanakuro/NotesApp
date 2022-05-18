package com.svetlana.kuro.notesapp.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.svetlana.kuro.notesapp.R
import com.svetlana.kuro.notesapp.domain.NoteEntity
import kotlinx.android.synthetic.main.note_item.view.*

class NoteAdapter : ListAdapter<NoteEntity, NoteAdapter.ViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<NoteEntity>() {
            override fun areItemsTheSame(oldItem: NoteEntity, newItem: NoteEntity): Boolean {
                return oldItem.noteId == newItem.noteId
            }

            override fun areContentsTheSame(oldItem: NoteEntity, newItem: NoteEntity): Boolean {
                return oldItem.noteTitle == newItem.noteTitle && oldItem.noteDescription == newItem.noteDescription
            }
        }
    }

    private var listener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentNote: NoteEntity = getItem(position)

        holder.textViewTitle.text = currentNote.noteTitle
        holder.textViewDescription.text = currentNote.noteDescription
    }

    fun getNoteAt(position: Int): NoteEntity {
        return getItem(position)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener?.onItemClick(getItem(position))
                }
            }
        }

        var textViewTitle: TextView = itemView.text_view_title
        var textViewDescription: TextView = itemView.text_view_description
    }

    interface OnItemClickListener {
        fun onItemClick(note: NoteEntity)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }
}