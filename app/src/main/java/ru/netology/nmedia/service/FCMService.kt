package ru.netology.nmedia.service

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson


class FCMService: FirebaseMessagingService() {

    private val gson = Gson()

    override fun onMessageReceived(massage: RemoteMessage) {
        Log.d("onMessageReceived", gson.toJson(massage))

    }

    override fun onNewToken(token: String) {
        Log.d("onNewToken", token)

    }
}