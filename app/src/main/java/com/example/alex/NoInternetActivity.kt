package com.example.alex

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class NoInternetActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_no_internet)

        findViewById<Button>(R.id.btnRetry).setOnClickListener {
            startActivity(Intent(this, SplashActivity::class.java))
            finish()
        }
    }
}