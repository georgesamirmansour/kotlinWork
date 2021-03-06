package com.example.nagwatask.di.module

import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient()

    @Singleton
    @Provides
    fun provideNetworkModel(): NetworkModule = NetworkModule(provideOkHttpClient())


}