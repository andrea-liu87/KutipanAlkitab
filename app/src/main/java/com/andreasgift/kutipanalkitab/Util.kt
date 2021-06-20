package com.andreasgift.kutipanalkitab

import android.content.SharedPreferences

object Util {
    val ALARM_LABEL_KEY = "ALARM_LABEL_KEY"
    val ALARM_ID = "alarm_id"

    fun getContent(prefs: SharedPreferences): String? {
        return prefs.getString(
            "CONTENT",
            "Serahkanlah segala kekuatiranmu kepada-Nya, sebab Ia yang memelihara kamu"
        )
    }

    fun getTitle(prefs: SharedPreferences): String? {
        return prefs.getString("TITLE", "1 Petrus 5:7")
    }

    fun saveContent(prefs: SharedPreferences, content: String) {
        prefs.edit().putString("CONTENT", content).apply()
    }

    fun saveTitle(prefs: SharedPreferences, title: String) {
        prefs.edit().putString("TITLE", title).apply()
    }

    fun getImage(prefs: SharedPreferences): String? {
        return prefs.getString(
            "IMAGE",
            "https://cdn.pixabay.com/photo/2016/11/08/05/18/hot-air-balloons-1807521_960_720.jpg"
        )
    }

    fun getCredit(prefs: SharedPreferences) = prefs.getString("CREDIT", "")

    fun saveImage(prefs: SharedPreferences, image: String) {
        prefs.edit().putString("IMAGE", image).apply()
    }

    fun saveCredit(prefs: SharedPreferences, credit: String) {
        prefs.edit().putString("CREDIT", credit).apply()
    }
}