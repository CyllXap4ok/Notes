package com.cyllxapk.notes.activity

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import com.cyllxapk.notes.Keys
import com.cyllxapk.notes.R
import com.cyllxapk.notes.ResultCodes
import com.cyllxapk.notes.databinding.ActivityEditBinding

class EditActivity : AppCompatActivity() {
    private lateinit var editView: ActivityEditBinding
    private var isEditInstance: Boolean = false

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        editView = ActivityEditBinding.inflate(layoutInflater)
        setContentView(editView.root)
        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        if (!intent.getStringExtra(Keys.TITLE_TEXT.key).isNullOrEmpty()) {

            isEditInstance = true
            editView.titleText.text!!.append(intent.getStringExtra(Keys.TITLE_TEXT.key))
            editView.noteText.text!!.append(intent.getStringExtra(Keys.NOTE_TEXT.key))

        }

        editView.titleText.addTextChangedListener {

            if (editView.titleText.text?.length!! > 14) editView.textInputLayout2.error = getString(R.string.error)
            else editView.textInputLayout2.isErrorEnabled = false

        }
    }

    fun onClickDone(view: View) {
        if (editView.titleText.text?.isNotEmpty() == true && editView.titleText.text?.length!! <= 14) {
            intent.putExtra(Keys.TITLE_TEXT.key, editView.titleText.text.toString())
            intent.putExtra(Keys.NOTE_TEXT.key, editView.noteText.text.toString())

            if (isEditInstance) setResult(ResultCodes.RESULT_EDITED.value, intent)
            else setResult(RESULT_OK, intent)

            finish()
        }
    }
}