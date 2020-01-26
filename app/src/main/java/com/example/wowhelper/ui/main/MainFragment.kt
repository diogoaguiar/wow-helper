package com.example.wowhelper.ui.main

import android.content.Context
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wowhelper.R
import com.example.wowhelper.data.Dictionary
import kotlinx.android.synthetic.main.main_fragment.*
import kotlinx.coroutines.runBlocking
import java.util.*

class MainFragment : Fragment() {

    companion object {
        const val TAG = "MainFragment"
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

        // Dictionary
        val dictionary = Dictionary(activity!!.baseContext)
        dictionary.loadLanguage("pt-PT")

        // Words list
        val wordsAdapter = WordsAdapter(tv_empty_state)
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

        // Update letters
        tie_letters.addTextChangedListener(object: TextWatcher {
            private var timer = Timer()
            private val delay: Long = 500 // milliseconds

            // Debounce
            override fun afterTextChanged(s: Editable?) {
                timer.cancel()
                timer = Timer()
                timer.schedule(object: TimerTask() {
                    override fun run() {
                        Handler(Looper.getMainLooper()).post {
                            viewModel.setLetters(tie_letters.text.toString())
                        }
                    }

                }, delay)

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        tie_letters.setOnEditorActionListener { v, actionId, _ ->
            var actionConsumed = false
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val imm = context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v.windowToken, 0)
                viewModel.setLetters(tie_letters.text.toString())
                v.clearFocus()
                actionConsumed = true
            }

            actionConsumed
        }

        // Clear filter
        ib_clear_filter.setOnClickListener {
            viewModel.clearFilter()
        }

        // Filters toggle
        tb_filters.setOnCheckedChangeListener { _, isChecked ->  viewModel.showFilters(isChecked) }

        // View model
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        viewModel.letters.observe(this, Observer {
            val words = dictionary.findWords(it)
            viewModel.setWords(words)


            // Update filter options
            gl_options.removeAllViews()
            addFilterButton("*")
            for (letter in viewModel.availableFilterLetters) {
                addFilterButton(letter.toString())
            }
        })

        viewModel.words.observe(this, Observer {
            wordsAdapter.setData(it)
        })

        viewModel.filter.observe(this, Observer {
            // Set adapter filters
            val pattern = if (it != "") "^"+it.replace("*", "\\w")+"$" else null
            wordsAdapter.setRegex(pattern)

            // Update filter options
            gl_options.removeAllViews()
            addFilterButton("*")
            for (letter in viewModel.availableFilterLetters) {
                addFilterButton(letter.toString())
            }

            // Update filter display
            tv_filter.text = it
        })

        viewModel.showFilters.observe(this, Observer {
            wordsAdapter.enableFilters(it)
            if (it) {
                filters.visibility = View.VISIBLE
            } else {
                filters.visibility = View.GONE
            }
        })

        // Set initial filters state
        viewModel.showFilters(tb_filters.isChecked)
    }

    private fun addFilterButton(letter : String) {
        val button = LayoutInflater.from(context)
            .inflate(R.layout.item_filter_option, gl_options, false) as Button

        button.text = letter

        button.setOnClickListener {
            viewModel.addToFilter(letter)
        }

        gl_options.addView(button)
    }

}
