package com.example.nagwatask.ui

import android.app.Application
import com.example.nagwatask.di.component.DaggerApplicationComponent

open class MyApplication : Application() {
    val appComponent = DaggerApplicationComponent.create()!!
}