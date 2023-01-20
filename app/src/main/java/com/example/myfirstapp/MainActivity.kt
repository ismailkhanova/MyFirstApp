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

const val API_KEY = "f09fa315f1404595a9e123021223110"

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