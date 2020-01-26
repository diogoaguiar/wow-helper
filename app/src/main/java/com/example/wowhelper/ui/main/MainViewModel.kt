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
    private val _filter = MutableLiveData<String>()
    val filter: LiveData<String>
        get() {
            return _filter
        }
    private val _showFilters = MutableLiveData<Boolean>()
    val showFilters: LiveData<Boolean>
        get() {
            return _showFilters
        }
    val availableFilterLetters: List<Char>
        get() {
            val value = ArrayList<Char>()
            val filterString = filter.value ?: ""
            val letters = this.letters.value?.toMutableList() ?: return listOf()
            for (l in letters) {
                val countAvailable = letters.count { it.equals(l, true) }
                val countUsed = filterString.filter { it != '*' }
                    .count { it.equals(l, true) }

                if (countAvailable > countUsed && !value.contains(l)) {
                    value.add(l)
                }
            }

            return value
        }

    init {
        _filter.value = ""
    }

    fun setWords(words: List<String>) {
        _words.value = words
    }

    fun setLetters(letters: String) {
        _letters.value = letters
    }

    fun addToFilter(part: String) {
        _filter.value += part
    }

    fun clearFilter() {
        _filter.value = ""
    }

    fun showFilters(value: Boolean) {
        _showFilters.value = value
    }
}
