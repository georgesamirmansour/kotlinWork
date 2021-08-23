package com.example.nagwatask.models

import android.util.Log
import java.lang.Exception


open class ApiState<T>(var message: String?, var data: T?)
class LoadingState<T>() : ApiState<T>(null, null)
class FailedState<T>(message: String?) : ApiState<T>(message, null) {
    init {
        Log.e(this.javaClass.name, "ErrorState() \t message: $message")
    }
}

class ErrorState<T>(message: String?, data: T?) : ApiState<T>(message, data) {
    init {
        val exception: Exception = data as Exception
        Log.e(this.javaClass.name, "ErrorState() \t message: $message \t ${exception.stackTrace}")
    }
}

class SuccessState<T>(message: String?, data: T?) : ApiState<T>(message, data)