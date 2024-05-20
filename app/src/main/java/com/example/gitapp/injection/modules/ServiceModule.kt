package com.example.gitapp.injection.modules

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.gitapp.background.RepoNotificationsCreator
import dagger.Module
import dagger.Provides

@Module
class ServiceModule(private val context: Context) {

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @Provides
    fun getRepoNotificationsCreator(): RepoNotificationsCreator {
        return RepoNotificationsCreator(context = context)
    }
}