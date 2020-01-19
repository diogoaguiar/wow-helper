package com.example.wowhelper.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.wowhelper.R
import kotlinx.android.synthetic.main.item_word.view.*

class WordsAdapter(): RecyclerView.Adapter<WordsAdapter.ViewHolder>() {
    private val data = ArrayList<String>()

    fun setData(words: List<String>) {
        data.clear()
        data.addAll(words)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_word, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val word: TextView = view.tv_word

        fun bind(word: String) {
            this.word.text = word
        }
    }
}