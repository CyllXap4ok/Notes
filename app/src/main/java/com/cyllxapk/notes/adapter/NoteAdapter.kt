package com.cyllxapk.notes.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cyllxapk.notes.R
import com.cyllxapk.notes.databinding.SingleNoteBinding

class NoteAdapter(private val clickOn: OnClick): RecyclerView.Adapter<NoteAdapter.NoteHolder>() {
    val noteList = arrayListOf<Note>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteHolder {
        val item = LayoutInflater.from(parent.context).inflate(R.layout.single_note, parent, false)
        return NoteHolder(item)
    }

    override fun onBindViewHolder(holder: NoteHolder, position: Int) {
        holder.bind(noteList[position], clickOn)
    }

    override fun getItemCount(): Int {
        return noteList.count()
    }

    class NoteHolder(item: View): RecyclerView.ViewHolder(item) {
        private val noteView = SingleNoteBinding.bind(item)

        fun bind(note: Note, clickOn: OnClick) {
            noteView.tTitle.text = note.titleText

            noteView.cView.setOnClickListener {
                clickOn.open(note)
            }

            noteView.bEdit.setOnClickListener {
                clickOn.edit(note)
            }

            noteView.bDelete.setOnClickListener {
                clickOn.delete(note)
            }
        }
    }

    fun addNote(note: Note) {
        noteList.add(note)
        notifyDataSetChanged()
    }

    fun editNote(newTitleText: String, newNoteText: String, position: Int) {
        noteList[position].titleText = newTitleText
        noteList[position].noteText = newNoteText
        notifyItemChanged(position)
    }

    fun deleteNote(position: Int) {
        noteList.removeAt(position)
        notifyItemRemoved(position)
    }

    interface OnClick {
        fun open(note: Note)
        fun edit(note: Note)
        fun delete(note: Note)
    }
}