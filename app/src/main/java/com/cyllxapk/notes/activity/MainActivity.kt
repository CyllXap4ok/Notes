package com.cyllxapk.notes.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.cyllxapk.notes.Keys
import com.cyllxapk.notes.R
import com.cyllxapk.notes.adapter.Note
import com.cyllxapk.notes.adapter.NoteAdapter
import com.cyllxapk.notes.databinding.ActivityMainBinding
import com.cyllxapk.notes.fragment.ConfirmFragment
import com.cyllxapk.notes.fragment.DataModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class MainActivity : AppCompatActivity(), NoteAdapter.OnClick {
    private lateinit var mainView: ActivityMainBinding
    private lateinit var editLauncher: ActivityResultLauncher<Intent>
    private val adapter = NoteAdapter(this)
    private val dataModel: DataModel by viewModels()

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainView = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainView.root)
        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        lifecycleScope.launch {
            loadSavedNotes(applicationContext, adapter)
        }

        editLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Keys.RESULT_EDITED) {
                adapter.editNote(
                    newTitleText = result.data?.getStringExtra(Keys.TITLE_TEXT_KEY)!!,
                    newNoteText = result.data?.getStringExtra(Keys.NOTE_TEXT_KEY)!!,
                    position = result.data?.getIntExtra(Keys.POSITION_KEY, -1)!!
                )
            } else if (result.resultCode == RESULT_OK) {
                adapter.addNote(
                    Note(
                        titleText = result.data?.getStringExtra(Keys.TITLE_TEXT_KEY)!!,
                        noteText = result.data?.getStringExtra(Keys.NOTE_TEXT_KEY)!!
                    )
                )
            }
            lifecycleScope.launch {
                saveFile(applicationContext, adapter)
            }
        }

        dataModel.isConfirmed.observe(this) {
            if (dataModel.isConfirmed.value!!) {
                adapter.deleteNote(dataModel.position.value!!)

                lifecycleScope.launch {
                    saveFile(applicationContext, adapter)
                }
            }
        }

        mainView.rcView.adapter = adapter
    }

    fun onClickAdd(view: View) {
        editLauncher.launch(Intent(this, EditActivity::class.java))
    }

    override fun open(note: Note) {
        val intent = Intent(this, NoteActivity::class.java)
        intent.putExtra(Keys.TITLE_TEXT_KEY, note.titleText)
        intent.putExtra(Keys.NOTE_TEXT_KEY, note.noteText)

        startActivity(intent)
    }

    override fun edit(note: Note) {
        val position = adapter.noteList.indexOfFirst { it == note }

        val intent = Intent(this, EditActivity::class.java)
        intent.putExtra(Keys.TITLE_TEXT_KEY, note.titleText)
        intent.putExtra(Keys.NOTE_TEXT_KEY, note.noteText)
        intent.putExtra(Keys.POSITION_KEY, position)

        editLauncher.launch(intent)
    }

    override fun delete(note: Note) {
        val position = adapter.noteList.indexOfFirst { it == note }

        dataModel.noteTitle.value = "\"${note.titleText}\""
        dataModel.position.value = position

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.confirmFragmentHolder, ConfirmFragment.newInstance())
            .addToBackStack(null)
            .commit()
    }
}

private suspend fun saveFile(context: Context, adapter: NoteAdapter) {

    var savedText = ""

    adapter.noteList.forEach { savedText += it.toString() + "\n"}

    withContext(Dispatchers.IO) {
        context.openFileOutput("saved_notes.txt", Context.MODE_PRIVATE).use {
            it.write(savedText.toByteArray())
        }
    }

}

private suspend fun loadSavedNotes(
    context: Context,
    adapter: NoteAdapter
) = withContext(Dispatchers.IO) {
    val readText = try {
        context.openFileInput("saved_notes.txt").bufferedReader().useLines { lines ->
            lines.fold("") { acc, s ->
                "$acc\n$s"
            }
        }
    } catch (e: IOException) {
        null
    }

    if (readText != null) {
        val list = readText.split("\n")
            .filterNot { it.isEmpty() }
            .map { it.split("៙") }

        val notes = arrayListOf<Note>()

        list.forEach {
            notes.add(Note(
                titleText = it[0],
                noteText = it[1].replace("¤", "\n")
            ))
        }

        adapter.noteList.clear()
        adapter.noteList.addAll(notes)
        adapter.notifyDataSetChanged()
    }
}