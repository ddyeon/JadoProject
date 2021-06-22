package com.example.jadoproject

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

class JadoApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}