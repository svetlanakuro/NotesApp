package com.svetlana.kuro.notesapp.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.svetlana.kuro.notesapp.R
import com.svetlana.kuro.notesapp.databinding.NoteItemBinding
import com.svetlana.kuro.notesapp.domain.NoteEntity

class ItemAdapter : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    var noteData: List<NoteEntity>? = null
    var listener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        noteData?.get(position)?.let { holder.bind(it) }
    }

    override fun getItemCount(): Int = noteData?.size ?: 0

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = NoteItemBinding.bind(itemView)
        fun bind(note: NoteEntity) {
            binding.apply {
                title.text = note.title
                description.text = note.description
                date.text = note.date.toString()
                completed.isChecked = note.isCompleted

                itemView.setOnClickListener {
                    listener?.onItemClick(note)
                }
            }
        }
    }

    fun interface OnItemClickListener {
        fun onItemClick(note: NoteEntity)
    }
}