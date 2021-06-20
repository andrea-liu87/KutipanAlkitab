package com.andreasgift.kutipanalkitab.data

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andreasgift.kutipanalkitab.Util
import com.andreasgift.kutipanalkitab.model.Background
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BackgroundViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val prefs: SharedPreferences
) : ViewModel() {

    private var image: String
    private var credit: String

    init {
        image = Util.getImage(prefs)!!
        credit = Util.getCredit(prefs)!!
    }

    var backgroundLiveData = Background("01", image, credit)

    suspend fun downloadNewBG() {
        viewModelScope.launch {
            val value = FirebaseService.getBackground(firestore)
            value?.let {
                image = value.image
                Util.saveImage(prefs, image)
                credit = value.credit
                Util.saveCredit(prefs, credit)
                backgroundLiveData = Background("02", image, credit)
            }
        }
    }

}