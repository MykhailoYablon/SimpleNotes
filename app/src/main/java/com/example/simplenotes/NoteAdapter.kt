package com.example.simplenotes

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NoteAdapter(
    private var notes: List<Note>,
    private val onNoteClick: (Note) -> Unit
) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val viewColor: View = itemView.findViewById(R.id.viewColor)
        val textTitle: TextView = itemView.findViewById(R.id.textTitle)
        val textPreview: TextView = itemView.findViewById(R.id.textPreview)
        val textDate: TextView = itemView.findViewById(R.id.textDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes[position]
        holder.textTitle.text = note.title
        holder.textPreview.text = note.text
        holder.textDate.text = android.text.format.DateFormat.format("yyyy-MM-dd HH:mm", note.dateAdded)
        try {
            holder.viewColor.setBackgroundColor(Color.parseColor(note.color))
        } catch (e: Exception) {
            holder.viewColor.setBackgroundColor(Color.parseColor("#A5D6A7")) // fallback gentle green
        }
        holder.itemView.setOnClickListener { onNoteClick(note) }
    }

    override fun getItemCount(): Int = notes.size

    fun setNotes(newNotes: List<Note>) {
        this.notes = newNotes
        notifyDataSetChanged()
    }
} 