package com.example.nagwatask.data.mian

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import com.example.nagwatask.di.module.NetworkModule
import com.example.nagwatask.models.ApiState
import com.example.nagwatask.models.ErrorState
import com.example.nagwatask.models.LoadingState
import com.example.nagwatask.models.SuccessState
import com.example.nagwatask.models.file.FileMapper
import com.example.nagwatask.models.file.FileResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class GetFileRepository @Inject constructor(private val networkModel: NetworkModule) {

    private val mediatorResponse: MediatorLiveData<ApiState<List<FileMapper>>> = MediatorLiveData()
    fun getFile(): MediatorLiveData<ApiState<List<FileMapper>>> {
        mediatorResponse.value = LoadingState()
        val source: LiveData<ApiState<List<FileMapper>>> = LiveDataReactiveStreams.fromPublisher(
            networkModel.provideServiceInterface()
                .getAllFiles().map { handleResponse(it) }.onErrorReturn { handleError(it) }
                .subscribeOn(Schedulers.io())
        )
        mediatorResponse.addSource(source, Observer {
            mediatorResponse.value = it
            mediatorResponse.removeSource(source)
        })
        return mediatorResponse
    }

    private fun handleError(it: Throwable): ApiState<List<FileMapper>> {
        return ErrorState("${it.localizedMessage}\nSwipe to refresh")
    }

    private fun handleResponse(response: List<FileResponse>): ApiState<List<FileMapper>> {
        val responseList = mutableListOf<FileMapper>()
        response.forEach {
            responseList.add(FileMapper(it))
        }
        return SuccessState("", responseList)
    }
}


