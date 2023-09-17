package com.example.bookstore.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.bookstore.R

class SplashScreenActivity : AppCompatActivity() {
    lateinit var gif:ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        gif = findViewById(R.id.gifimage)
        Glide.with(this).load(R.drawable.book).into(gif)
        val handler = Handler()
        handler.postDelayed({
            val intent = Intent(this@SplashScreenActivity,MainActivity::class.java)
            startActivity(intent)
        },1000)
        }

    override fun onPause() {
        finish()
        super.onPause()
    }
}