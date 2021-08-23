package com.example.nagwatask.di.component

import com.example.nagwatask.di.module.AppModule
import com.example.nagwatask.ui.main.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
    ]
)
interface ApplicationComponent{
    fun inject(mainActivity: MainActivity)
}