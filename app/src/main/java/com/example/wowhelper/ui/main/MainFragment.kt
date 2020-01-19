package com.example.wowhelper.ui.main

import android.content.Context
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wowhelper.R
import com.example.wowhelper.data.Dictionary
import kotlinx.android.synthetic.main.main_fragment.*

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val dictionary = Dictionary(activity!!.baseContext)
        dictionary.loadLanguage("pt-PT")

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        val wordsAdapter = WordsAdapter()
        rv_words.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = wordsAdapter
            addItemDecoration(
                DividerItemDecoration(
                    context,
                    LinearLayoutManager.VERTICAL
                )
            )
        }

        ib_confirm.setOnClickListener {
            val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)
            tie_letters.clearFocus()
            viewModel.setLetters(tie_letters.text.toString())
        }

        viewModel.letters.observe(this, Observer {
            val words = dictionary.findWords(it)
            viewModel.setWords(words)
        })

        viewModel.words.observe(this, Observer {
            wordsAdapter.setData(it)
        })
    }

}
