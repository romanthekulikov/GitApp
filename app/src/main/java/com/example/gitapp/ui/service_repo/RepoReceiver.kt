package com.example.gitapp.ui.service_repo

import android.annotation.SuppressLint
import android.app.ForegroundServiceStartNotAllowedException
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat

class RepoReceiver : BroadcastReceiver() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("MissingPermission")
    override fun onReceive(context: Context, intent: Intent?) {
        Log.i("repo_service", "push")

        val serviceIntent = Intent(context, RepoService::class.java)
        try {
            ContextCompat.startForegroundService(context, serviceIntent)
        } catch (e: ForegroundServiceStartNotAllowedException) {
            RepoAlarmHelper.setAlarm(context, startAfterSec = 60)
        }
    }
}