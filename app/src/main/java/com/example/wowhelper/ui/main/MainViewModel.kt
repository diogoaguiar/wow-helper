package com.example.wowhelper.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    private val _words = MutableLiveData<List<String>>()
    val words: LiveData<List<String>>
        get() {
            return _words
        }
    private val _letters = MutableLiveData<String>()
    val letters: LiveData<String>
        get() {
            return _letters
        }

    fun setWords(words: List<String>) {
        _words.value = words
    }

    fun setLetters(letters: String) {
        _letters.value = letters
    }
}
