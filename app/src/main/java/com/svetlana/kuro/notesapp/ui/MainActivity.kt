package com.svetlana.kuro.notesapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.svetlana.kuro.notesapp.R
import com.svetlana.kuro.notesapp.ui.main.MainFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.main_activity, MainFragment.newInstance())
                .commitNow()
        }
    }
}