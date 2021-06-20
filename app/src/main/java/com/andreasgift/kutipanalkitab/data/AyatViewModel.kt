package com.andreasgift.kutipanalkitab.data

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andreasgift.kutipanalkitab.Util
import com.andreasgift.kutipanalkitab.model.AyatAlkitab
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AyatViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val prefs: SharedPreferences
) : ViewModel() {
    private var content: String
    private var title: String

    init {
        content = Util.getContent(prefs)!!
        title = Util.getTitle(prefs)!!
    }

    var ayatLiveData = AyatAlkitab("01", content, title)


    suspend fun downloadNewData() {
        viewModelScope.launch {
            val value = FirebaseService.getAyat(firestore)
            value?.let {
                content = value.content
                Util.saveContent(prefs, value.content)
                title = value.title
                Util.saveTitle(prefs, value.title)
                ayatLiveData = AyatAlkitab("02", content, title)
            }
        }
    }
}