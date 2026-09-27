package com.example.alex

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2

class OnboardingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        val viewPager = findViewById<ViewPager2>(R.id.viewPagerOnboarding)
        val btnNext = findViewById<Button>(R.id.btnNext)
        val tvSkip = findViewById<TextView>(R.id.tvSkip)

        val onboardingItems = listOf(
            OnboardingItem(
                R.drawable.img_slide1,
                "Аренда автомобилей",
                "Открой для себя удобный и доступный способ передвижения"
            ),
            OnboardingItem(
                R.drawable.img_slide2,
                "Безопасно и удобно",
                "Арендуй автомобиль и наслаждайся его удобством"
            ),
            OnboardingItem(
                R.drawable.img_slide3,
                "Лучшие предложения",
                "Выбирай понравившееся среди сотен доступных автомобилей"
            )
        )

        val adapter = ViewPagerAdapter(onboardingItems)
        viewPager.adapter = adapter

        tvSkip.setOnClickListener { finishOnboarding() }

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position == adapter.itemCount - 1) {
                    btnNext.text = "Поехали"
                } else {
                    btnNext.text = "Далее"
                }
            }
        })

        btnNext.setOnClickListener {
            if (viewPager.currentItem < adapter.itemCount - 1) {
                viewPager.currentItem += 1
            } else {
                finishOnboarding()
            }
        }
    }

    private fun finishOnboarding() {
        val prefs = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        prefs.edit().putBoolean("isFirstLaunch", false).apply()

        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}