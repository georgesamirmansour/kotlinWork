package com.example.nagwatask.di.module

import com.example.nagwatask.data.mian.DownloadFileRepository
import com.example.nagwatask.data.mian.GetFileRepository
import dagger.Module
import dagger.Provides
import javax.inject.Inject
import javax.inject.Singleton

@Module(includes = [NetworkModule::class])
class MainActivityModel @Inject constructor(private val networkModule: NetworkModule){

    @Singleton
    @Provides
    fun provideMainRepo(): GetFileRepository = GetFileRepository(networkModule)

    @Singleton
    @Provides
    fun provideDownloadFileRepo(): DownloadFileRepository = DownloadFileRepository(networkModule)
}