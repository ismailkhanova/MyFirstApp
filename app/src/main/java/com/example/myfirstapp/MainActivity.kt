package com.example.myfirstapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myfirstapp.BuildConfig.GOOGLE_MAPS_API_KEY

import com.example.myfirstapp.databinding.ActivityMainBinding
import com.example.myfirstapp.fragments.MainFragment
import com.google.android.libraries.places.api.Places


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Places.initialize(applicationContext, GOOGLE_MAPS_API_KEY)
        setContentView(R.layout.activity_main)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.placeholder, MainFragment.newInstance())
            .commit()

        Toast.makeText(this,"Welcome",Toast.LENGTH_SHORT).show()
    }
}
