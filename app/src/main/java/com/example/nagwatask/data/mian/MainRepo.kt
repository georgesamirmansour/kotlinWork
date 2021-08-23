package com.example.nagwatask.data.mian

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import com.example.nagwatask.di.module.NetworkModule
import com.example.nagwatask.di.module.OkHttpClient
import com.example.nagwatask.models.ApiState
import com.example.nagwatask.models.ErrorState
import com.example.nagwatask.models.LoadingState
import com.example.nagwatask.models.SuccessState
import com.example.nagwatask.models.file.FileMapper
import com.example.nagwatask.models.file.FileResponse
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MainRepo @Inject constructor(private val networkModel: NetworkModule){

    private val mediatorResponse: MediatorLiveData<ApiState<List<FileMapper>>> = MediatorLiveData()

    fun getFile(): MediatorLiveData<ApiState<List<FileMapper>>> {
        mediatorResponse.value = LoadingState()
        val source: LiveData<ApiState<List<FileMapper>>> = LiveDataReactiveStreams.fromPublisher(
            networkModel.provideServiceInterface()
                .getAllFiles().doOnError { handleError(it) }.map { handleResponse(it) }
                .subscribeOn(Schedulers.io())
        )
        mediatorResponse.addSource(source, Observer {
            mediatorResponse.value = it
            mediatorResponse.removeSource(source)
        })
        return mediatorResponse
    }

    private fun handleError(throwable: Throwable?): List<FileResponse> {
        Log.e(MainRepo::class.simpleName, "handleError: ", throwable)
        mediatorResponse.value = ErrorState(throwable?.message, ArrayList())
        return ArrayList()
    }

    private fun handleResponse(response: List<FileResponse>): ApiState<List<FileMapper>> {
        val responseList = mutableListOf<FileMapper>()
        response.forEach {
            responseList.add(FileMapper(it))
        }
        return SuccessState("", responseList)
    }
}


