package com.andreasgift.kutipanalkitab.data

import android.util.Log
import com.andreasgift.kutipanalkitab.model.AyatAlkitab
import com.andreasgift.kutipanalkitab.model.Background
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

object FirebaseService {

    suspend fun getAyat(firestore: FirebaseFirestore): AyatAlkitab? {
        try {
            val querySnapshot = firestore.collection("ayat").get().await()
            val doc = querySnapshot.documents.random()
            return AyatAlkitab(
                doc.id,
                doc.data!!.get("content").toString(),
                doc.data!!.get("title").toString()
            )
        } catch (e: Exception) {
            Log.d("TAG", e.toString())
            return null
        }
    }

    suspend fun getBackground(firestore: FirebaseFirestore): Background? {
        try {
            val querySnapshot = firestore.collection("background").get().await()
            val document = querySnapshot.documents.random()
            return Background(
                document.id,
                document.data!!.get("image").toString(),
                document.data!!.get("credit").toString()
            )
        } catch (e: Exception) {
            Log.d("TAG", e.toString())
            return null
        }
    }
}