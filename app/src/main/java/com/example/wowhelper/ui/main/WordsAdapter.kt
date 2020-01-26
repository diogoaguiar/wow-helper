package com.example.wowhelper.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.wowhelper.R
import kotlinx.android.synthetic.main.item_word.view.*

class WordsAdapter(val emptyState: View): RecyclerView.Adapter<WordsAdapter.ViewHolder>() {
    private val defaultFilter = "^\\w+$".toRegex(RegexOption.IGNORE_CASE)

    private val data = ArrayList<String>()
    private var regex = defaultFilter
    private var filtersEnabled = false

    private val filteredData: List<String> get() {
        return if (filtersEnabled) data.filter { regex.matches(it) } else data
    }

    private fun showEmptyState(show: Boolean) {
        emptyState.visibility = if (show) View.VISIBLE else View.GONE
    }

    fun setData(words: List<String>) {
        data.clear()
        data.addAll(words)
        notifyDataSetChanged()
        showEmptyState(filteredData.isEmpty())
    }

    fun setRegex(pattern: String?) {
        regex = if (pattern === null) defaultFilter else pattern.toRegex(RegexOption.IGNORE_CASE)
        notifyDataSetChanged()
        showEmptyState(filteredData.isEmpty())
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_word, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return filteredData.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(filteredData[position])
    }

    fun enableFilters(enable: Boolean) {
        filtersEnabled = enable
        notifyDataSetChanged()
        showEmptyState(filteredData.isEmpty())
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val word: TextView = view.tv_word

        fun bind(word: String) {
            this.word.text = word
        }
    }
}