package com.vpmedia.vbotsdksample

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.vpmedia.sdkvbot.client.ClientListener
import com.vpmedia.sdkvbot.domain.pojo.mo.Hotline
import com.vpmedia.vbotsdksample.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.util.UUID

class MainActivity : AppCompatActivity(), ChooseHotline.ListenerBottomSheet {

    private lateinit var binding: ActivityMainBinding

    private var listener = object : ClientListener() {
        override fun onErrorCode(erCode: Int, message: String) {
            super.onErrorCode(erCode, message)
            updateView()
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        MyApplication.initClient(this)

        binding.llMain.setOnClickListener {
            closeKeyboard(this)
        }

        binding.btnDisconnect.setOnClickListener {
            binding.btnDisconnect.isEnabled = false
            binding.btnDisconnect.setBackgroundResource(R.drawable.bg_red_20)

            Handler(Looper.getMainLooper()).postDelayed({
                binding.btnDisconnect.isEnabled = true
                binding.btnDisconnect.setBackgroundResource(R.drawable.bg_red_50)
            }, 2000)
            MyApplication.client.disconnect()
            updateView()
        }

        binding.btnConnect.setOnClickListener {
            binding.btnConnect.setBackgroundResource(R.drawable.bg_main_10_op50)
            binding.btnConnect.isEnabled = false
            Handler(Looper.getMainLooper()).postDelayed({
                binding.btnConnect.isEnabled = true
                binding.btnConnect.setBackgroundResource(R.drawable.bg_main_10)
            }, 2000)
            MyApplication.client.connect(binding.etToken.text.toString(), MyApplication.tokenFirebase)
            updateView()
        }

//        click button select hotline
        binding.btnSelectHotline.setOnClickListener {
            //lấy list hotline
            CoroutineScope(Dispatchers.IO).launch {
                runBlocking {
                    val list = MyApplication.client.getHotlines()
                    if (list != null) {
                        val chooseHotlineCallBottomSheet = ChooseHotline()
                        chooseHotlineCallBottomSheet.show(
                            supportFragmentManager, "chooseHotlineCallBottomSheet"
                        )
                        chooseHotlineCallBottomSheet.setListener(this@MainActivity, list)
                    }
                }
            }
        }

//        click button call
        binding.btnCall.setOnClickListener {
            closeKeyboard(this)
            binding.btnCall.setBackgroundResource(R.drawable.bg_main_10_op50)
            binding.btnCall.isEnabled = false
            Handler(Looper.getMainLooper()).postDelayed({
                binding.btnCall.isEnabled = true
                binding.btnCall.setBackgroundResource(R.drawable.bg_main_10)
            }, 2000)

            //tạo call đi
            val hotline = binding.etHotline.text.toString().trim()
            val to = binding.etNumber.text.toString().trim()
            Log.d("LogApp", to)
            val avatar = ""
            val uuid = UUID.randomUUID().toString()
            val checkSum = when (to) {
                "112" -> {
                    "ios01$uuid"
                }

                "113" -> {
                    "ios02$uuid"
                }
                "116" -> {
                    "ios03$uuid"
                }

                "114" -> {
                    "android01$uuid"
                }

                "115" -> {
                    "android02$uuid"
                }
                "117" -> {
                    "android03$uuid"
                }
                else -> {
                    uuid
                }
            }
            Log.d("LogApp", "checkSum: $checkSum")
            MyApplication.client.startOutgoingCall(callerId = "", callerAvatar = avatar, callerName = MyApplication.client.getAccountUsername(), calleeId = to, calleeAvatar = avatar, calleeName = to, checkSum = checkSum)
        }

        updateView()

//        check quyền hiển thị trên ứng dụng khác
        if (!Settings.canDrawOverlays(this)) {
            if (isMiuiWithApi28OrMore()) {
                goToXiaomiPermissions(this)
            } else {
                val intent = Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:$packageName")
                )
                @Suppress("DEPRECATION")
                startActivityForResult(intent, 23052)
            }
        }

    }

    private fun updateView() {
        val name = MyApplication.client.getAccountUsername()
        binding.tvName.text = name
        Log.d("asjkhdkaj", name)
    }

    private fun isMiuiWithApi28OrMore(): Boolean {
        return isMiUi() && Build.VERSION.SDK_INT >= 26
    }

    private fun isMiUi(): Boolean {
        return getSystemProperty()?.isNotBlank() == true
    }

    private fun getSystemProperty(): String? {
        val line: String
        var input: BufferedReader? = null
        try {
            val p = Runtime.getRuntime().exec("getprop ro.miui.ui.version.name")
            input = BufferedReader(InputStreamReader(p.inputStream), 1024)
            line = input.readLine()
            input.close()
        } catch (ex: IOException) {
            return null
        } finally {
            if (input != null) {
                try {
                    input.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        return line
    }

    private fun goToXiaomiPermissions(context: Context) {
        val intent = Intent("miui.intent.action.APP_PERM_EDITOR")
        intent.setClassName(
            "com.miui.securitycenter",
            "com.miui.permcenter.permissions.PermissionsEditorActivity"
        )
        intent.putExtra("extra_pkgname", context.packageName)
        context.startActivity(intent)
    }

    override fun onClickHotline(hotline: Hotline) {
        //set số hotline
        binding.etHotline.setText(hotline.phoneNumber)
    }

    override fun onResume() {
        super.onResume()
        MyApplication.client.addListener(listener)
    }

    override fun onPause() {
        super.onPause()
        MyApplication.client.removeListener(listener)
    }

    private fun closeKeyboard(activity: Activity) {
        val view = activity.currentFocus
        if (view != null) {
            val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

}