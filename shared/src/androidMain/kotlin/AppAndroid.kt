package ru.alexmaryin.simschecklist

import android.app.Application
import di.apiModule
import di.dbModule
import di.realmModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class AppAndroid : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this

        startKoin {
            androidContext(this@AppAndroid)
            modules(dbModule, apiModule, realmModule)
        }
    }

    companion object{
        private lateinit var instance: AppAndroid
        fun instance() = instance
    }
}
