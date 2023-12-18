package com.cyllxapk.notes.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.cyllxapk.notes.R
import com.cyllxapk.notes.databinding.FragmentConfirmBinding

class ConfirmFragment : Fragment() {
    private lateinit var fragmentView: FragmentConfirmBinding
    private val dataModel: DataModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentView = FragmentConfirmBinding.inflate(inflater)
        return fragmentView.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val message = getString(R.string.fragment_message_1) +
                dataModel.noteTitle.value +
                getString(R.string.fragment_message_2)
        
        fragmentView.confirmTextView.text = message

        fragmentView.btCancel.setOnClickListener {
            dataModel.isConfirmed.value = false
            parentFragmentManager.beginTransaction().remove(this).commit()
        }

        fragmentView.btDelete.setOnClickListener {
            dataModel.isConfirmed.value = true
            parentFragmentManager.beginTransaction().remove(this).commit()
        }

    }

    companion object {
        @JvmStatic
        fun newInstance() = ConfirmFragment()
    }
}