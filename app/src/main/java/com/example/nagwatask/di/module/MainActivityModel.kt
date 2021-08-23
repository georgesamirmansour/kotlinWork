package com.example.nagwatask.di.module

import com.example.nagwatask.data.mian.MainRepo
import dagger.Module
import dagger.Provides
import javax.inject.Inject
import javax.inject.Singleton

@Module(includes = [NetworkModule::class])
class MainActivityModel @Inject constructor(private val networkModule: NetworkModule){

    @Singleton
    @Provides
    fun provideMainRepo(): MainRepo = MainRepo(networkModule)
}