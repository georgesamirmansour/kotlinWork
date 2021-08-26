package com.example.nagwatask.ui

import android.app.Application
import android.os.StrictMode
import com.example.nagwatask.di.component.DaggerApplicationComponent

open class MyApplication : Application() {
    val appComponent = DaggerApplicationComponent.create()!!

    override fun onCreate() {
        super.onCreate()
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        builder.detectFileUriExposure()
    }
}