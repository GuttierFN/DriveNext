package com.example.alex

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tvStatus = findViewById<TextView>(R.id.tvInternetStatus)

        val isConnected = intent.getBooleanExtra("INTERNET_STATUS", true)
        if (tvStatus != null) {
            if (isConnected) {
                tvStatus.text = "Главный экран DriveNext"
            } else {
                tvStatus.text = "Интернет: Отсутствует"
            }
        }
    }
}