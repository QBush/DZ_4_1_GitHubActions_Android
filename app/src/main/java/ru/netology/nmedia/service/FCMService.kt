package ru.netology.nmedia.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import ru.netology.nmedia.R
import kotlin.random.Random


class FCMService : FirebaseMessagingService() {

    // переводит данные в текстовый формат
    private val gson = Gson()

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_remote_name)
            val descriptionText = getString(R.string.channel_remote_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    //действия при получении сообщения
    override fun onMessageReceived(message: RemoteMessage) {
        Log.d("onMessageReceived", gson.toJson(message.data))
        //считываем данные
        val data = message.data
        //проверяем, что пришел action
        val serializedAction = data[Action.KEY] ?: return
        //находим, какой именно action пришел
        val action = Action.values().find { it.key == serializedAction } ?: return

        when (action) {
            Action.Like -> handleLikeAction(data[CONTENT_KEY] ?: return)
        }
    }

    // выводим токен в консоль
    override fun onNewToken(token: String) {
        Log.d("onNewToken", token)
    }


    private fun handleLikeAction(serializedContent: String) {
        val likeContent = gson.fromJson(serializedContent, Like::class.java)

        // формируем уведомление
        // конструируемый стринг из строковых ресурсов
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
                // загружаем иконку, должна быть монохромная
            .setSmallIcon(R.drawable.ic_notification)
                //создаем текст уведомления
            .setContentTitle(
                getString(
                    R.string.notification_user_liked,
                    likeContent.userName,
                    likeContent.postAuthor
                )
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        // показываем уведомление со случайным номером (не заморачиваемся)
        NotificationManagerCompat.from(this)
            .notify(Random.nextInt(100_000),notification)
    }

    private companion object {
        const val CONTENT_KEY = "content"
        const val CHANNEL_ID = "remote"
    }


}