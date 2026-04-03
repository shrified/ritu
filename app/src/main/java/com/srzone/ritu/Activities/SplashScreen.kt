package com.srzone.ritu.Activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.srzone.ritu.R

class SplashScreen : AppCompatActivity() {
    public override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.activity_splash)
        Handler().postDelayed(object : Runnable {
            override fun run() {
                OpenNext1()
            }
        }, 5000)
    }

    fun OpenNext1() {
        startActivity(Intent(this, InputActivity::class.java))
        finish()
    }
}
