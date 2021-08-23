package com.example.nagwatask.di.module

import com.example.nagwatask.network.ServiceInterface
import com.example.nagwatask.utilities.Constants
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
class NetworkModule @Inject constructor(private val okHttpClient : OkHttpClient){

    @Singleton
    @Provides
    fun provideServiceInterface(): ServiceInterface {
        return Retrofit.Builder().baseUrl(Constants.baseUrl)
            .client(okHttpClient.getOkHttpClient())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(ServiceInterface::class.java)
    }
}