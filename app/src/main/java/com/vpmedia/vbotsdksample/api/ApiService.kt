package com.vpmedia.vbotsdksample.api

import com.vpmedia.vbotsdksample.model.PushToken
import com.vpmedia.vbotsdksample.model.ResponseApi
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {
    @POST("example-sdk-xanhsm/sdk/push-token")
    @Headers("Content-Type: application/json")
    fun pushToken(
        @Body request: PushToken
    ): Call<ResponseApi>
}