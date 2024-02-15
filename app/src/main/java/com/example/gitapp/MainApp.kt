package com.example.gitapp

import android.app.Application
import android.content.Context
import com.example.gitapp.injection.AppComponent
import com.example.gitapp.injection.DaggerAppComponent

class MainApp : Application() {
    lateinit var appComponent: AppComponent
    override fun onCreate() {
        appComponent = DaggerAppComponent.create()
        super.onCreate()
    }
}

@Suppress("RecursivePropertyAccessor")
val Context.appComponent: AppComponent
    get() = when(this) {
        is MainApp -> appComponent
        else -> this.applicationContext.appComponent
    }