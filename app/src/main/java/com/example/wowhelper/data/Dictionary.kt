package com.example.wowhelper.data

import android.content.Context

class Dictionary(private val context: Context) {
    private val dictionary = ArrayList<String>()
    private val defaultLanguage = "pt-PT"

    fun loadLanguage(language: String? = null): Boolean {
        val lang = if (language !== null) language else defaultLanguage
        try {
            val words = context.assets.open("dictionaries/$lang")
                .bufferedReader().readLines()
            dictionary.addAll(words)
        } catch (e: NullPointerException) {
            e.printStackTrace()
            return false
        }
        return true
    }

    fun findWords(letters: String): List<String> = findWords(letters.toCharArray().toList())

    fun findWords(letters: List<Char>): List<String> {
        // Normalize letters
        val normalizedLetters = letters.map { it.toLowerCase() }
        val words = ArrayList<String> ()

        for (word in dictionary) {
            val normalizedWord = word.trim()
            if (normalizedWord.length > normalizedLetters.size) {
                continue
            }

            var availableLetters = normalizedLetters.toMutableList()
            var isMatch = true
            for (i in normalizedWord.indices) {
                val letter = normalizedWord[i]
                if (letter in availableLetters) {
                    availableLetters.remove(letter)
                } else {
                    isMatch = false
                    break
                }
            }

            if (isMatch) {
                words.add(normalizedWord)
            }
        }

        return words
    }
}