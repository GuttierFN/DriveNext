package com.example.alex

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText

class SignUpStep1Activity : AppCompatActivity() {

    private val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.(com|ru)$")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_step1)

        val btnBack = findViewById<TextView>(R.id.btnBackStep1)
        val etEmail = findViewById<TextInputEditText>(R.id.etRegEmail)
        val etPass1 = findViewById<TextInputEditText>(R.id.etRegPass1)
        val etPass2 = findViewById<TextInputEditText>(R.id.etRegPass2)
        val cbTerms = findViewById<CheckBox>(R.id.cbTerms)
        val btnNext = findViewById<Button>(R.id.btnStep1Next)

        btnBack.setOnClickListener { finish() }

        btnNext.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val pass1 = etPass1.text.toString()
            val pass2 = etPass2.text.toString()

            if (!emailRegex.matches(email)) {
                Toast.makeText(this, "Введите корректный адрес электронной почты.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (pass1.isEmpty() || pass1 != pass2) {
                Toast.makeText(this, "Пароли не совпадают.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!cbTerms.isChecked) {
                Toast.makeText(
                    this,
                    "Необходимо согласиться с условиями обслуживания и политикой конфиденциальности.",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            val prefs = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
            prefs.edit()
                .putString("registered_email", email)
                .putString("registered_password", pass1)
                .apply()

            startActivity(Intent(this, SignUpStep2Activity::class.java))
        }
    }
}