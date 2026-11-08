package com.example.alex

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SignUpStep3Activity : AppCompatActivity() {

    private val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    private var avatarUri: Uri? = null
    private var licensePhotoUri: Uri? = null
    private var passportPhotoUri: Uri? = null
    private var pendingCameraUri: Uri? = null
    private var currentPhotoTarget: Int = 0

    private lateinit var ivProfileAvatar: ImageView
    private lateinit var btnUploadLicense: Button
    private lateinit var btnUploadPassport: Button
    private lateinit var etLicenseNumber: TextInputEditText
    private lateinit var etIssueDate: TextInputEditText
    private lateinit var btnStep3Next: Button

    private val pickOneImage = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            handleSelectedUri(uri)
        }
    }

    private val takeOnePhoto = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success: Boolean ->
        if (success && pendingCameraUri != null) {
            handleSelectedUri(pendingCameraUri!!)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_step3)

        val btnBack = findViewById<TextView>(R.id.btnBackStep3)
        val tilIssueDate = findViewById<TextInputLayout>(R.id.tilIssueDate)
        ivProfileAvatar = findViewById(R.id.ivProfileAvatar)
        btnUploadLicense = findViewById(R.id.btnUploadLicense)
        btnUploadPassport = findViewById(R.id.btnUploadPassport)
        etLicenseNumber = findViewById(R.id.etLicenseNumber)
        etIssueDate = findViewById(R.id.etIssueDate)
        btnStep3Next = findViewById(R.id.btnStep3Next)

        btnBack.setOnClickListener { finish() }

        val watcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                updateNextButtonState()
            }
            override fun afterTextChanged(s: Editable?) {}
        }

        etLicenseNumber.addTextChangedListener(watcher)
        etIssueDate.addTextChangedListener(watcher)

        fun openDatePicker() {
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Дата выдачи")
                .build()

            datePicker.addOnPositiveButtonClickListener { selectedDateUtcMillis ->
                etIssueDate.setText(dateFormatter.format(Date(selectedDateUtcMillis)))
            }
            datePicker.show(supportFragmentManager, "issue_date_picker")
        }

        tilIssueDate.setStartIconOnClickListener { openDatePicker() }

        ivProfileAvatar.setOnClickListener { showImageSourceDialog(1) }
        btnUploadLicense.setOnClickListener { showImageSourceDialog(2) }
        btnUploadPassport.setOnClickListener { showImageSourceDialog(3) }

        btnStep3Next.setOnClickListener {
            val license = etLicenseNumber.text.toString().trim()
            if (license.length != 10) {
                Toast.makeText(this, "Пожалуйста, заполните все обязательные поля.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (etIssueDate.text.isNullOrBlank()) {
                Toast.makeText(this, "Введите корректную дату выдачи.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (licensePhotoUri == null || passportPhotoUri == null) {
                Toast.makeText(this, "Пожалуйста, загрузите все необходимые фото.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            startActivity(Intent(this, SignUpSuccessActivity::class.java))
        }
    }

    private fun showImageSourceDialog(target: Int) {
        currentPhotoTarget = target
        val options = arrayOf("Выбрать из галереи", "Сделать фото с камеры")
        AlertDialog.Builder(this)
            .setTitle("Добавление фотографии")
            .setItems(options) { _, which ->
                if (which == 0) {
                    openGalleryForOneImage()
                } else {
                    val uri = createImageUri()
                    openCameraForOnePhoto(uri)
                }
            }
            .show()
    }

    private fun openGalleryForOneImage() {
        pickOneImage.launch(
            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
        )
    }

    private fun openCameraForOnePhoto(outputUri: Uri) {
        pendingCameraUri = outputUri
        takeOnePhoto.launch(outputUri)
    }

    private fun createImageUri(): Uri {
        val imageFile = File(cacheDir, "photo_${System.currentTimeMillis()}.jpg")
        return FileProvider.getUriForFile(
            this,
            "${applicationContext.packageName}.fileprovider",
            imageFile
        )
    }

    private fun handleSelectedUri(uri: Uri) {
        when (currentPhotoTarget) {
            1 -> {
                avatarUri = uri
                ivProfileAvatar.setImageURI(uri)
            }
            2 -> {
                licensePhotoUri = uri
                btnUploadLicense.text = "Фото ВУ загружено"
            }
            3 -> {
                passportPhotoUri = uri
                btnUploadPassport.text = "Фото паспорта загружено"
            }
        }
        updateNextButtonState()
    }

    private fun updateNextButtonState() {
        val hasRequiredFields = etLicenseNumber.text?.length == 10 &&
                !etIssueDate.text.isNullOrBlank() &&
                licensePhotoUri != null &&
                passportPhotoUri != null
        btnStep3Next.isEnabled = hasRequiredFields
    }
}