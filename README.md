# VBot-SDK-Android-Example

## Code demo

[https://github.com/VBotDevTeam/VBot-SDK-Android-Example](https://github.com/VBotDevTeam/VBot-SDK-Android-Example)

---

## Yêu cầu

Android SDK version 23 trở lên

---

## Cấu hình SDK

Trong file **app** → **libs** của dự án thêm **SDKVBot.aar**

Trong file **app** → **build.gradle** thêm

```kotlin
dependencies {
    implementation files('libs/SDKVBot.aar')
}
```

Thêm **google-services.json** vào thư mục **app**

Trong file **build.gradle(Module :app)**

Ở mục **dependencies**

Thêm 2 dòng sau:

```kotlin
implementation platform('com.google.firebase:firebase-bom:32.4.0')
implementation 'com.google.firebase:firebase-messaging-ktx:23.3.1'
```

Ở mục **plugins**

Thêm dòng sau:

```kotlin
plugins {
    id 'com.android.application'
    id 'org.jetbrains.kotlin.android'
    id 'com.google.gms.google-services'
}
```

Trong file **build.gradle(Project)**

Ở mục **plugins**

Thêm dòng sau:

```kotlin
id 'com.google.gms.google-services' version '4.4.0' apply false
```

Trong file **manifests** ở thư mục **application**

Thêm những dòng sau:

```kotlin
<service
	android:name=".FirebaseService"
	android:exported="false"
	android:stopWithTask="false">
	<intent-filter>
		<action android:name="com.google.firebase.MESSAGING_EVENT" />
	</intent-filter>
</service>
```

Tạo class để đón thông báo cuộc gọi

```kotlin
class FirebaseService : FirebaseMessagingService() {
	// Nhận thông báo cuộc gọi
	override fun onMessageReceived(remoteMessage: RemoteMessage) {
	}
}
```

---

## Quyền

Danh sách các quyền cần thiết để SDK hoạt động:

1. **Quyền truy cập điện thoại**

- Kiểm tra trạng thái thiết bị (cuộc gọi đến/đi, đang bận, hoặc rảnh).

- Thực hiện và quản lý cuộc gọi VoIP.

- **Lưu ý**: Đây là quyền **bắt buộc** để SDK hoạt động.

2. **Quyền truy cập micro**

- Thu âm giọng nói để gửi trong các cuộc gọi VoIP.

- **Lưu ý**: Đây là quyền **bắt buộc**.

3. **Quyền truy cập thiết bị ở gần**

- Kết nối với các thiết bị Bluetooth như tai nghe hoặc loa ngoài để sử dụng trong cuộc gọi.

4. **Quyền thông báo**

- Hiển thị thông báo cuộc gọi đến hoặc các sự kiện quan trọng từ SDK.

5. **Quyền hiển thị trên ứng dụng khác**

- Hiển thị thông báo quan trọng (ví dụ: thông báo cuộc gọi đến) dưới dạng “màn hình nổi” hoặc “pop-up” ngay cả khi ứng dụng đang ở chế độ nền hoặc màn hình khóa.

## Sử dụng SDK

---

### Khởi tạo

```kotlin
var client = VBotClient(context)
```

Chạy hàm setup để khởi động sdk

```kotlin
fun setup(vBotConfig: VBotConfig)
```

Truyền cofig vào hàm setup

```kotlin
VBotConfig(
	accountType: AccountType,
	supportDarkMode: Boolean = true
)
```

---

### Kết nối

Hàm này chỉ cần gọi duy nhất một lần khi người dùng đăng nhập vào app của bạn

Sau khi connect thành công thì dữ liệu sẽ được lưu vào SDK

Sau khi người dùng tắt app và mở lại thì **client.setup()** là đủ để SDK hoạt động

```kotlin
fun connect(token: String, tokenFirebase: String?)
```

- token: Token SDK của tài khoản VBot
- tokenFirebase: Có thể truyền hoặc không

---

### Ngắt kết nối

Hàm này sẽ cần gọi thì người dùng đăng xuất khỏi ứng dụng

Tất cả dữ liệu SDK sẽ bị xóa

Sau khi người dùng tắt app và mở lại thì cần gọi **connect** với **token**

```kotlin
fun disconnect(): Boolean
```

---

### Lấy tên hiển thị

```kotlin
fun getAccountUsername(): String
```

---

### Lắng nghe sự kiện

```kotlin
fun addListener(listener: ClientListener)
```

---

### Xoá lắng nghe sự kiện

```kotlin
fun removeListener(listener: ClientListener)
```

---

### Lấy danh sách Hotline

```kotlin
suspend fun getHotlines(): List<Hotline>?
```

Model Hotline

```kotlin
data class Hotline(
    @SerializedName("name")
    @Expose
    val name: String = "",

    @SerializedName("phoneNumber")
    @Expose
    val phoneNumber: String = ""
)
```

---

### Gọi đi

```kotlin
fun startOutgoingCall(
		callerId: String,
		callerAvatar: String? = null,
		callerName: String,
		calleeId: String,
		calleeAvatar: String? = null,
		calleeName: String,
		checkSum: String
)
```

---

### Gọi đến

Tạo cuộc gọi đến
Hàm được sử dụng để xử lý và thông báo về các thông tin cuộc gọi nhận được.

```kotlin
fun startIncomingCall(
    callerId: String,
    callerAvatar: String? = null,
    callerName: String,
    calleeId: String,
    calleeAvatar: String? = null,
    calleeName: String,
    checkSum: String
)
```

Hàm này có thể được gọi khi có thông báo về cuộc gọi đến, cho phép ứng dụng xử lý thông tin, hiển thị thông báo cho người dùng

---

### Đa ngôn ngữ

VBot SDK mặc định chỉ hỗ trợ Tiếng Việt

Khi app thay đổi ngôn ngữ, hãy dùng hàm **setLocalizationStrings(strings)** để thay đổi ngôn ngữ cho VBot SDK

```kotlin
fun setLocalizationStrings(values: Map<String, String?>)
```
