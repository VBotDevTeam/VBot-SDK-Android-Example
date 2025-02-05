package com.vpmedia.vbotsdksample

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.util.Log
import com.google.firebase.FirebaseApp
import com.vpmedia.vbotphonesdk.VBotPhone

class MyApplication : Application() {
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var client: VBotPhone

        // kiểm tra khởi tạo VBotClient
        fun clientExists(): Boolean {
            return ::client.isInitialized
        }

        //        //Khởi tạo VBotClient
        fun initClient(context: Context, token: String) {
            if (clientExists()) {
                Log.d("MainActivity", "Skipping Client creation")
                return
            }
            Log.d("MainActivity", "startClient")
            client = VBotPhone.setup(context, token)
        }
    }

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }

}