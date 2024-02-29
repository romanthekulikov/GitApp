package com.example.gitapp.ui.service_repo

import android.annotation.SuppressLint
import android.app.ForegroundServiceStartNotAllowedException
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RepoReceiver : BroadcastReceiver() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("MissingPermission")
    override fun onReceive(context: Context?, p1: Intent?) {
        Log.i("repo_service", "push")
        val serviceIntent = Intent(context, RepoService::class.java)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                context?.startForegroundService(serviceIntent)
            } catch (e: ForegroundServiceStartNotAllowedException) {
                if (context != null) {
                    RepoAlarmHelper.setExactAlarm(context, startAfterSec = 60)
                }
            }
        }
    }
}