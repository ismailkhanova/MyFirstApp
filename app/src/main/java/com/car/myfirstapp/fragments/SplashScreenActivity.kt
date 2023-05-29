package com.car.myfirstapp.fragments

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.car.myfirstapp.MainActivity
import com.car.myfirstapp.R

class SplashScreenActivity : AppCompatActivity() {

    lateinit var handler: Handler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        handler = Handler(Looper.getMainLooper())
        handler.postDelayed({

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        } , 2000)
    }
}
