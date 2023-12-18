package com.cyllxapk.notes.fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class DataModel: ViewModel() {

    val noteTitle: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val isConfirmed: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    val position: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }
}