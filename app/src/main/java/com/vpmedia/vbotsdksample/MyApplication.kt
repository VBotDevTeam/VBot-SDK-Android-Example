package com.vpmedia.vbotsdksample

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import com.vpmedia.sdkvbot.client.VBotClient

class MyApplication : Application() {
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var client: VBotClient
        var tokenFirebase: String = ""

        // kiểm tra khởi tạo VBotClient
        fun clientExists(): Boolean {
            return ::client.isInitialized
        }

//        //Khởi tạo VBotClient
        fun initClient(context: Context) {
            if (clientExists()) {
                Log.d("LogApp", "Skipping Client creation")
                return
            }
            Log.d("LogApp", "startClient")
            client = VBotClient(context)
        }
    }

    override fun onCreate() {
        super.onCreate()
        //Lấy firebase Token
        getTokenFirebase()
    }

    private fun getTokenFirebase() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            try {
                if (task.isSuccessful) {
                    val token = task.result
                    if (!token.isNullOrEmpty()) {
                        tokenFirebase = token
                        Log.d("token", token)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}