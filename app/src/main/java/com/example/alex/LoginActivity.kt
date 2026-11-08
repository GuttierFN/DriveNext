package com.example.alex

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText

class LoginActivity : AppCompatActivity() {

    private val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.(com|ru)$")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val etEmail = findViewById<TextInputEditText>(R.id.etLoginEmail)
        val etPassword = findViewById<TextInputEditText>(R.id.etLoginPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnGoogle = findViewById<Button>(R.id.btnGoogleLogin)
        val tvGoToSignUp = findViewById<TextView>(R.id.tvGoToSignUp)

        val watcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                btnLogin.isEnabled = !etEmail.text.isNullOrBlank() && !etPassword.text.isNullOrBlank()
            }
            override fun afterTextChanged(s: Editable?) {}
        }

        etEmail.addTextChangedListener(watcher)
        etPassword.addTextChangedListener(watcher)

        btnLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString()

            if (!emailRegex.matches(email)) {
                Toast.makeText(this, "Введите корректный адрес электронной почты.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val prefs = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
            val savedEmail = prefs.getString("registered_email", null)
            val savedPassword = prefs.getString("registered_password", null)

            if (savedEmail == null || email != savedEmail || password != savedPassword) {
                Toast.makeText(this, "Ошибка: неверная почта или пароль (или аккаунт не зарегистрирован).", Toast.LENGTH_SHORT).show()
            } else {
                saveSessionAndOpenMain(email)
            }
        }

        btnGoogle.setOnClickListener {
            saveSessionAndOpenMain("google_user@gmail.com")
        }

        tvGoToSignUp.setOnClickListener {
            startActivity(Intent(this, SignUpStep1Activity::class.java))
        }
    }

    private fun saveSessionAndOpenMain(email: String) {
        val prefs = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        prefs.edit()
            .putBoolean("isLoggedIn", true)
            .putString("user_email", email)
            .putString("access_token", "token_${System.currentTimeMillis()}")
            .apply()

        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}