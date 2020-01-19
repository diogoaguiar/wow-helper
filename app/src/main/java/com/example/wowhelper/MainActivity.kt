package com.example.wowhelper

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProviders
import com.example.wowhelper.data.Dictionary
import com.example.wowhelper.ui.main.MainFragment
import com.example.wowhelper.ui.main.MainViewModel

class MainActivity : AppCompatActivity() {

    companion object {
        const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }

        val dictionary = Dictionary(baseContext)
        dictionary.loadLanguage("pt-PT")
    }

}
