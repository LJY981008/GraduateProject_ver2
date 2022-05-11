package com.example.schoollifeproject

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.example.schoollifeproject.model.APIS

class ForecdTerminationService : Service() {
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onTaskRemoved(rootIntent: Intent) { //핸들링 하는 부분
        stopSelf() //서비스 종료
    }
}