package com.example.nagwatask.di.module

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
@Module
class OkHttpClient {

    @Provides
    @Singleton
    fun getOkHttpClient(): OkHttpClient {
        val requestTimeOut = 90
        val builder = OkHttpClient.Builder()
            .readTimeout(requestTimeOut.toLong(), TimeUnit.SECONDS)
            .connectTimeout(requestTimeOut.toLong(), TimeUnit.SECONDS)
            .writeTimeout(requestTimeOut.toLong(), TimeUnit.SECONDS)
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        builder.addInterceptor(httpLoggingInterceptor)
        builder.addInterceptor { chain ->
            val original = chain.request()
            val requestBuilder = original.newBuilder()
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")
            val request = requestBuilder.build()
            chain.proceed(request)
        }
        return builder.build()
    }
}