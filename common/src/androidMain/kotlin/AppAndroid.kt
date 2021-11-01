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

    fun loadAircraft(filename: String) = assets.open(filename).bufferedReader().readText()

    fun loadPhoto(filename: String) = assets.open(filename).buffered()
}