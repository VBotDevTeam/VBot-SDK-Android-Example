package com.vpmedia.vbotsdksample

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessaging
import com.vpmedia.vbotsdksample.api.ApiClient
import com.vpmedia.vbotsdksample.databinding.ActivityMainBinding
import com.vpmedia.vbotsdksample.model.PushToken
import com.vpmedia.vbotsdksample.model.ResponseApi
import com.vpmedia.vbotsdksample.model.SDKMember
import com.vpmedia.vbotsdksample.view.SelectAccount
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.util.UUID


class MainActivity : AppCompatActivity(), SelectAccount.ListenerBottomSheet {

    private lateinit var binding: ActivityMainBinding

    private var tokenFirebase: String = ""
    private var accountLogin: SDKMember? = null
    private var accountCall: SDKMember? = null
    private var isConnect = false

    private fun getRequiredPermissions(): Array<String> = mutableListOf(
        Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_PHONE_STATE
    ).apply {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) add(Manifest.permission.POST_NOTIFICATIONS)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) add(Manifest.permission.BLUETOOTH_CONNECT)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            add(Manifest.permission.FOREGROUND_SERVICE_MICROPHONE)
            add(Manifest.permission.FOREGROUND_SERVICE_PHONE_CALL)
        }
    }.toTypedArray()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        MyApplication.initClient(this, "")


        binding.btnLoginAccount.setOnSingleClickListener {
            Timber.d("binding.btnLoginAccount")
            SelectAccount().apply {
                show(supportFragmentManager, tag)
                setListener(this@MainActivity, false)
            }
        }

        binding.btnCallAccount.setOnSingleClickListener {
            Timber.d("binding.btnCallAccount")
            if (accountLogin != null) {
                SelectAccount().apply {
                    show(supportFragmentManager, tag)
                    setListener(this@MainActivity, true, accountLogin?.name)
                }
            } else {
                Toast.makeText(this, "Chưa chọn tài khoản để gọi", Toast.LENGTH_SHORT).show()
            }

        }

        binding.btnConnectToggle.setOnSingleClickListener {
            if (!isConnect) {
                when {
                    tokenFirebase.isEmpty() -> {
                        Toast.makeText(this, "Chưa lấy được token firebase", Toast.LENGTH_SHORT).show()
                        getTokenFirebase()
                    }

                    accountLogin == null -> {
                        Toast.makeText(this, "Chưa chọn tài khoản", Toast.LENGTH_SHORT).show()
                    }

                    else -> {
                        updateToggleConnect(true)

                        pushToken(accountLogin?.userId ?: "", accountLogin?.name ?: "", accountLogin?.code ?: "", tokenFirebase,
                            onSuccess = {
                                isConnect = true
                                updateStatus("Đã kết nối: ${accountLogin?.name}")
                                saveAccount(accountLogin?.name ?: "")
                                updateToggleConnect(false)
                            }, onFailure = { code, message ->
                                isConnect = false
                                updateStatus("Lỗi kết nối: $code - $message")
                                updateToggleConnect(false)
                            })
                    }
                }
            } else {
                updateToggleConnect(true)
                MyApplication.client.destroy(onSuccess = {
                    isConnect = false
                    updateStatus("Đã ngắt kết nối")
                    updateToggleConnect(false)
                    clearData()
                }, onFailure = { code, error ->
                    updateStatus("Lỗi ngắt kết nối: $code - $error")
                    updateToggleConnect(false)
                })
            }
        }

        binding.btnCall.setOnSingleClickListener {
            Timber.d("binding.btnCall")
            call()
        }

        val name = getNameAccount()
        if (name.isNotEmpty()) {
            accountLogin = SDKMember.getByName(name)
            if (accountLogin != null) {
                isConnect = true
                updateStatus("Đã kết nối: ${accountLogin?.name}")
            } else {
                isConnect = false
            }
        } else {
            isConnect = false
        }
        updateToggleConnect(false)

        getTokenFirebase()

        requestPermission()
    }

    private fun updateStatus(string: String) {
        binding.tvStatus.text = string
        if (isConnect) {
            binding.tvStatus.setTextColor(ContextCompat.getColor(this, com.vpmedia.vbotphonesdk.R.color.colorMain))
        } else {
            binding.tvStatus.setTextColor(ContextCompat.getColor(this, com.vpmedia.vbotphonesdk.R.color.red))
        }
    }

    private fun updateToggleConnect(isClick: Boolean) {
        binding.btnConnectToggle.text = if (isConnect) "Xoá" else "Lưu"

        if (isClick) {
            binding.btnConnectToggle.isEnabled = false
            binding.btnLoginAccount.setBackgroundResource(R.drawable.bg_main_10_op50)
            if (isConnect) {
                binding.btnConnectToggle.setBackgroundResource(R.drawable.bg_red_op20)
            } else {
                binding.btnConnectToggle.setBackgroundResource(R.drawable.bg_blue_10_op50)
            }
        } else {
            binding.btnConnectToggle.isEnabled = true
            if (isConnect) {
                binding.btnConnectToggle.setBackgroundResource(R.drawable.bg_red_op50)
                binding.btnLoginAccount.isEnabled = false
                binding.btnLoginAccount.setBackgroundResource(R.drawable.bg_main_10_op50)
            } else {
                binding.btnConnectToggle.setBackgroundResource(R.drawable.bg_blue_10)
                binding.btnLoginAccount.isEnabled = true
                binding.btnLoginAccount.setBackgroundResource(R.drawable.bg_main_10)
            }
        }
    }

    private fun requestPermission() {
        if (!Settings.canDrawOverlays(this)) {
            if (isMiuiWithApi28OrMore()) {
                goToXiaomiPermissions(this)
            } else {
                val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName"))
                @Suppress("DEPRECATION")
                startActivityForResult(intent, 23052)
            }
        }

        if (!hasPermissions(this, getRequiredPermissions())) {
            requestPermissions(getRequiredPermissions(), 100)
        }
    }

    private fun getTokenFirebase() {
        if (tokenFirebase.isEmpty()) {
            Timber.d("getTokenFirebase()")
            Thread {
                FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                    try {
                        if (task.isSuccessful) {
                            Timber.d(" task.isSuccessful")
                            val token = task.result
                            if (!token.isNullOrEmpty()) {
                                tokenFirebase = token
                                Timber.d("Token: $token")
                            } else {
                                Timber.d(" token.isNullOrEmpty()")
                            }
                        } else {
                            Timber.d("Error fetching token: ${task.exception}")
                        }
                    } catch (e: Exception) {
                        Timber.d("Exception: ${e.message}")
                        e.printStackTrace()
                    }
                }
            }.start()
        } else {
            Timber.d("Token: $tokenFirebase")
        }
    }

    private fun pushToken(
        userId: String,
        deviceName: String, deviceId: String, token: String,
        onSuccess: (() -> Unit)? = null,
        onFailure: ((code: Int, message: String?) -> Unit)? = null
    ) {
        val request = PushToken(
            deviceName = deviceName,
            deviceId = deviceId,
            userId = userId,
            token = token
        )

        val call = ApiClient.apiService.pushToken(request)
        call.enqueue(object : Callback<ResponseApi> {
            override fun onResponse(call: Call<ResponseApi>, responseApi: Response<ResponseApi>) {
                if (responseApi.isSuccessful) {
                    onSuccess?.invoke()

                    Timber.d("Success: ${responseApi.body()?.msg}")
                } else {
                    Timber.d("Error: ${responseApi.errorBody()?.string()}")
                    onFailure?.invoke(responseApi.code(), responseApi.message())
                }
            }

            override fun onFailure(call: Call<ResponseApi>, t: Throwable) {
                Timber.d("Failed: ${t.message}")
            }
        })
    }

    private fun hasPermissions(context: Context, permission: Array<String>): Boolean {
        for (i in permission) {
            if (context.checkSelfPermission(i) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
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

    private fun View.setOnSingleClickListener(interval: Long = 1000, onClick: (View) -> Unit) {
        this.setOnClickListener {
            this.isEnabled = false // Vô hiệu hóa nút
            onClick(it)
            this.postDelayed({ this.isEnabled = true }, interval) // Kích hoạt lại sau khoảng thời gian
        }
    }

    override fun onClickAccount(model: SDKMember, isCall: Boolean) {
        if (isCall) {
            accountCall = model
            binding.btnCallAccount.text = model.name
        } else {
            accountLogin = model
            binding.btnLoginAccount.text = "Đã chọn: ${model.name}"
        }
    }

    private fun call() {
        if (accountCall != null && accountLogin != null) {
            val to = accountCall?.userId?.trim()
            val uuid = UUID.randomUUID().toString()
            val checkSum = "${accountCall!!.code}-${accountLogin?.code}-$uuid"
            Timber.d("checkSum: $checkSum  - to: $to - accountCall: ${accountCall?.name}")
            if (!to.isNullOrEmpty()) {
                MyApplication.client.startOutgoingCall(
                    callerId = accountLogin?.userId ?: "",
                    callerAvatar = "",
                    callerName = accountLogin?.name ?: "",
                    calleeId = to,
                    calleeAvatar = "",
                    calleeName = accountCall?.name ?: "",
                    checkSum = checkSum
                )
            } else {
                Toast.makeText(this, "Không lấy được số điện thoại", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Chưa chọn tài khoản kết nối hoặc chưa chọn tài khoản để gọi", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveAccount(name: String) {
        val sharedPreferences = getSharedPreferences("my_prefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("NAME_LOGIN", name)
        editor.apply()
    }

    private fun getNameAccount(): String {
        val sharedPreferences = getSharedPreferences("my_prefs", MODE_PRIVATE)
        val savedData = sharedPreferences.getString("NAME_LOGIN", "") ?: ""
        return savedData
    }

    private fun clearData() {
        val sharedPreferences = getSharedPreferences("my_prefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear() // Xoá toàn bộ dữ liệu
        editor.apply() // Hoặc editor.commit()
    }
}