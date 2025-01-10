package com.vpmedia.vbotsdksample

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.json.JSONObject
import kotlin.random.Random


class FirebaseService : FirebaseMessagingService() {

    //nhận notify
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val hashMap = HashMap<String, String>()
        for (key in remoteMessage.data.keys) {
            hashMap[key] = remoteMessage.data[key].toString()
        }
        Log.d("LogApp", hashMap.toString())
        if (remoteMessage.notification != null) {
            val title = remoteMessage.notification!!.title
            val message = remoteMessage.notification!!.body
            sendNotification("$title", "$message")
        } else {
            val metaData = hashMap["metaData"] ?: ""
            if (metaData.isNotEmpty()) {
                val metaDataJson = JSONObject(metaData)
                val type = metaDataJson.optString("type", "")

                println("Type: $type")
                try {
                    when (type) {
                        "call" -> {
                            val checkSum = metaDataJson.optString("check_sum", "")
                            val callerId = hashMap["callerId"] ?: ""
                            val callerAvatar = hashMap["callerAvatar"] ?: ""
                            val callerName = hashMap["callerName"] ?: ""
                            val calleeId = hashMap["calleeId"] ?: ""
                            val calleeAvatar = hashMap["calleeAvatar"] ?: ""
                            val calleeName = hashMap["calleeName"] ?: ""
                            MyApplication.initClient(this@FirebaseService)
                            MyApplication.client.startIncomingCall(callerId, callerAvatar, callerName, calleeId, calleeAvatar, calleeName, checkSum)
                        }
                    }
                } catch (ex: IllegalStateException) {
                    ex.printStackTrace()
                }
            }
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }


    //tạo notify trên điện thoại
    private fun sendNotification(title: String, message: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent: PendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_MUTABLE)
        } else {
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        }
        val channelId = "vbot_sdk"
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder: NotificationCompat.Builder =
            NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.ic_va_logo_notification))
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(NotificationManager.IMPORTANCE_HIGH)
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, getString(R.string.app_name), NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(Random.nextInt(0, 9999), notificationBuilder.build())
    }
}

