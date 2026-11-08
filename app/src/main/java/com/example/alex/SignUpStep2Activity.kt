package com.example.alex

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SignUpStep2Activity : AppCompatActivity() {

    private val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_step2)

        val btnBack = findViewById<TextView>(R.id.btnBackStep2)
        val etLastName = findViewById<TextInputEditText>(R.id.etLastName)
        val etFirstName = findViewById<TextInputEditText>(R.id.etFirstName)
        val tilBirthDate = findViewById<TextInputLayout>(R.id.tilBirthDate)
        val etBirthDate = findViewById<TextInputEditText>(R.id.etBirthDate)
        val rgGender = findViewById<RadioGroup>(R.id.rgGender)
        val btnNext = findViewById<Button>(R.id.btnStep2Next)

        btnBack.setOnClickListener { finish() }

        fun validateFields() {
            val isReady = !etLastName.text.isNullOrBlank() &&
                    !etFirstName.text.isNullOrBlank() &&
                    !etBirthDate.text.isNullOrBlank() &&
                    rgGender.checkedRadioButtonId != -1
            btnNext.isEnabled = isReady
        }

        val watcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validateFields()
            }
            override fun afterTextChanged(s: Editable?) {}
        }

        etLastName.addTextChangedListener(watcher)
        etFirstName.addTextChangedListener(watcher)
        etBirthDate.addTextChangedListener(watcher)
        rgGender.setOnCheckedChangeListener { _, _ -> validateFields() }

        fun openDatePicker() {
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Дата рождения")
                .build()

            datePicker.addOnPositiveButtonClickListener { selectedDateUtcMillis ->
                etBirthDate.setText(dateFormatter.format(Date(selectedDateUtcMillis)))
            }
            datePicker.show(supportFragmentManager, "birth_date_picker")
        }

        tilBirthDate.setStartIconOnClickListener { openDatePicker() }

        btnNext.setOnClickListener {
            if (etLastName.text.isNullOrBlank() || etFirstName.text.isNullOrBlank() || rgGender.checkedRadioButtonId == -1) {
                Toast.makeText(this, "Пожалуйста, заполните все обязательные поля.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (etBirthDate.text.isNullOrBlank()) {
                Toast.makeText(this, "Введите корректную дату рождения.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            startActivity(Intent(this, SignUpStep3Activity::class.java))
        }
    }
}