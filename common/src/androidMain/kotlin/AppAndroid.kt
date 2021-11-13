package ru.alexmaryin.simschecklist

import android.app.Application

class AppAndroid : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object{
        private lateinit var instance: AppAndroid
        fun instance() = instance
    }
}
