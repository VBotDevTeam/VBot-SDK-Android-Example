# Android SDK

---

## Code demo

https://github.com/VBotDevTeam/VBot-SDK-Android-Example

---

## Yêu cầu

Android SDK version 23 trở lên

---

## Cấu hình SDK

### Cách 1

Trong file **app** → **build.gradle** thêm 

Cần thêm các thư viện cần thiết để SDK hoạt động 

```kotlin
dependencies {
    //các thư viện cần thiết để SDK hoạt động
    implementation("io.reactivex.rxjava2:rxjava:2.2.21")
    implementation("com.google.code.gson:gson:2.11.0")
    implementation("com.squareup.retrofit2:adapter-rxjava2:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.okio:okio:3.9.0")
    implementation("org.reactivestreams:reactive-streams:1.0.4")
    implementation("com.squareup.okhttp3:okhttp:5.0.0-alpha.14")
    implementation("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.14")
    implementation("com.jakewharton.timber:timber:5.0.1")

    //Thêm SDK
    implementation 'com.github.VBotDevTeam:VBotPhoneSDKPrivate:1.0.0'
}
```
Trong file **settings.gradle** thêm 
```kotlin
dependencyResolutionManagement {
	repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
	repositories {
		mavenCentral()
		maven { url 'https://jitpack.io' }
	}
}
```
### Cách 2
- Vào trang https://jitpack.io/
- Trong vào ô *'Git repo url'* nhập **VBotDevTeam/VBotPhoneSDKPrivate**
- Nhấn **Look up**
- Chọn version và nhấn **Get it**
- Làm theo hướng dẫn trên trang web
---

## Quyền

Danh sách các quyền cần thiết để SDK hoạt động:

1.	**Quyền truy cập điện thoại**

•	Kiểm tra trạng thái thiết bị (cuộc gọi đến/đi, đang bận, hoặc rảnh).

•	Thực hiện và quản lý cuộc gọi VoIP.

•	**Lưu ý**: Đây là quyền **bắt buộc** để SDK hoạt động.

2.	**Quyền truy cập micro** 

•	Thu âm giọng nói để gửi trong các cuộc gọi VoIP.

•	**Lưu ý**: Đây là quyền **bắt buộc** để SDK hoạt động.

3.	**Quyền truy cập thiết bị ở gần** 

•	Kết nối với các thiết bị Bluetooth như tai nghe hoặc loa ngoài để sử dụng trong cuộc gọi.

4.	**Quyền thông báo** 

•	Hiển thị thông báo cuộc gọi đến hoặc các sự kiện quan trọng từ SDK.

5.	**Quyền hiển thị trên ứng dụng khác** 

•	Hiển thị thông báo quan trọng (thông báo cuộc gọi đến) dưới dạng “màn hình nổi” hoặc “pop-up” ngay cả khi ứng dụng đang ở chế độ nền hoặc màn hình khóa.

## Proguard

VBotPhoneSDK yêu cầu một bộ quy tắc ProGuard. Các đoạn mã sau đây cung cấp các quy tắc ProGuard phù hợp dựa trên phiên bản được ứng dụng của bạn sử dụng.

```kotlin
-keep class com.vpmedia.vbotphonesdk.** {*;}

-keep class org.linphone.** { *; }
-keepclassmembers class org.linphone.** { *; }
```

## Sử dụng SDK

---

### Khởi tạo


```kotlin
fun setup(context: Context, token: String)
```

Trong đó:
- **token** là App Token, đại diện cho ứng dụng của bạn được dùng để xác thực với máy chủ VBot 
- **config** là cấu hình tùy chọn cho SDK

---
### Lắng nghe sự kiện

```kotlin
fun addListener(listener: VBotListener)
```

```kotlin
class VBotListener {

    override fun onCallState(state: CallState) {
    }

    override fun onErrorCode(erCode: Int, message: String) {
    }

    override fun onMessageButtonClick() {

    }
}
```
`onCallState` trả về trạng thái của cuộc gọi
`onErrorCode` trả về lỗi và mã lỗi
`onMessageButtonClick` trả về sự kiện khi người dùng nhấn vào nút nhắn tin

---

### Xoá lắng nghe sự kiện

```kotlin
fun removeListener(listener: VBotListener)
```

---

### Gọi đi

Để thực hiện cuộc gọi đi, hãy sử dụng hàm startOutgoingCall
```kotlin
fun startOutgoingCall(
		 callerId: String, <Mã người gọi>
    callerAvatar: String? = null, <Avatar người gọi>
    callerName: String,<Tên người gọi>
    calleeId: String, <Mã người nghe>
    calleeAvatar: String? = null, <Avatar người nghe>
    calleeName: String,<Tên người nghe>
    checkSum: String,<Mã xác thực cuộc gọi>
)
```

---

### Gọi đến

Để nhận cuộc gọi đến
Trong hàm onMessageReceived kế thừa từ FirebaseMessagingService hãy sử dụng hàm startIncomingCall

```kotlin
fun startIncomingCall(
    callerId: String, <Mã người gọi>
    callerAvatar: String? = null, <Avatar người gọi>
    callerName: String,<Tên người gọi>
    calleeId: String, <Mã người nghe>
    calleeAvatar: String? = null, <Avatar người nghe>
    calleeName: String,<Tên người nghe>
    checkSum: String,<Mã xác thực cuộc gọi>
    metadata: HashMap<String, String> <MetaData của cuộc gọi>
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
Truyền dữ liệu qua metadata khi gọi hàm startIncomingCall
