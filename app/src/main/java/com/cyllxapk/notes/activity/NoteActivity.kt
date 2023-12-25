package com.cyllxapk.notes.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.cyllxapk.notes.Keys
import com.cyllxapk.notes.databinding.ActivityNoteBinding

class NoteActivity : AppCompatActivity() {
    private lateinit var noteView: ActivityNoteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        noteView = ActivityNoteBinding.inflate(layoutInflater)
        setContentView(noteView.root)
        val titleText = "\"${intent.getStringExtra(Keys.TITLE_TEXT.key)}\""
        noteView.titleTextView.text = titleText
        noteView.noteTextView.text = intent.getStringExtra(Keys.NOTE_TEXT.key)
    }
}