package com.cyllxapk.notes.adapter

class Note(
    var titleText: String,
    var noteText: String
) {
    override fun toString(): String {
        return "$titleText៙${noteText.replace("\n", "¤")}"
    }
}
