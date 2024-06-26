package com.example.gitapp

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.data.data.database.AppDatabase
import com.example.gitapp.injection.AppComponent
import com.example.gitapp.injection.modules.ServiceModule
import com.example.gitapp.injection.DaggerAppComponent


class GitApp : Application() {
    companion object {
        lateinit var appComponent: AppComponent
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate() {
        appComponent = DaggerAppComponent.builder().serviceModule(ServiceModule(applicationContext)).build()
        AppDatabase.initDb(applicationContext)
        super.onCreate()
    }
}