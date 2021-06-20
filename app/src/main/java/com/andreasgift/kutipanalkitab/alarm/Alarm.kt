package com.andreasgift.kutipanalkitab.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.andreasgift.kutipanalkitab.Util.ALARM_ID
import java.util.*

class Alarm {
    var id = 1
    var alarmHour: Int = 9
    var alarmMin: Int = 55
    var isAM: Boolean = false

    //var days: ArrayList<Int> =
    var isOn: Boolean = true

    fun setOneTimeAlarm(context: Context) {
        Log.d("ALARM", "Set one time alarm")
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val calendar = createCalendarAlarm()
        val alarmIntent = createAlarmPendingIntent(context, id)

        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            alarmIntent
        )
    }

    private fun createCalendarAlarm(dayOfWeek: Int? = null): Calendar {
        val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            if (dayOfWeek != null) {
                set(Calendar.DAY_OF_WEEK, dayOfWeek)
            }
            set(Calendar.HOUR_OF_DAY, alarmHour)
            set(Calendar.MINUTE, alarmMin)
            if (!isAM) {
                set(Calendar.AM_PM, Calendar.PM)
            }
        }
        return calendar
    }

    private fun createAlarmPendingIntent(context: Context, intentId: Int): PendingIntent {
        val alarmIntent = Intent(context, AlarmReceiver::class.java).let { intent ->
            intent.putExtra(ALARM_ID, id)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            PendingIntent.getBroadcast(
                context,
                intentId,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
        return alarmIntent
    }
}