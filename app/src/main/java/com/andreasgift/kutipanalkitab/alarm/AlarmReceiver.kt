package com.andreasgift.kutipanalkitab.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.PowerManager
import android.util.Log
import com.andreasgift.kutipanalkitab.MainActivity
import com.andreasgift.kutipanalkitab.Util.ALARM_ID
import com.andreasgift.kutipanalkitab.Util.ALARM_LABEL_KEY

class AlarmReceiver : BroadcastReceiver() {
    private var label: String? = ""

    private lateinit var soundUri: Uri
    private var alarmId: Int = 0

    override fun onReceive(context: Context, intent: Intent) {
        label = intent.getStringExtra(ALARM_LABEL_KEY) ?: ""
        soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        alarmId = intent.getIntExtra(ALARM_ID, 0)

        val mPowerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        Log.d("ALARM", "Alarm is received")
        Intent(context, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(this)
        }
    }
}