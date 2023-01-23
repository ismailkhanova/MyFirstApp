package com.example.myfirstapp

import android.app.DownloadManager.Request
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.myfirstapp.databinding.ActivityMainBinding
import com.example.myfirstapp.fragments.MainFragment

const val API_KEY = "a5e049379e664ccb9c685958232201"

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.placeholder, MainFragment.newInstance())
            .commit()

        Toast.makeText(this,"Welcome",Toast.LENGTH_SHORT).show()



    }
}