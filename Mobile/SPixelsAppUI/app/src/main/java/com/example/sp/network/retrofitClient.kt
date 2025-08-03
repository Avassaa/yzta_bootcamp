package com.example.sp.network

import ApiService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit
object RetrofitClient {
    val logging = HttpLoggingInterceptor()
    private const val BASE_URL = "http://10.0.2.2:8000"
    val instance: ApiService by lazy {
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        val httpClient = OkHttpClient.Builder()
            .addInterceptor(logging) // Add the logger
            .connectTimeout(60, TimeUnit.SECONDS) // Increase connect timeout
            .readTimeout(30, TimeUnit.SECONDS)    // Increase read timeout
            .writeTimeout(30, TimeUnit.SECONDS)   // Increase write timeout
            .build()

        // 3. Build Retrofit using the custom OkHttpClient
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient) // <-- Use our new client with the logger and timeouts
            .build()

        retrofit.create(ApiService::class.java)
    }
}