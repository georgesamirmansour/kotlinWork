package com.example.nagwatask.data.mian

import androidx.lifecycle.MediatorLiveData
import com.example.nagwatask.di.module.NetworkModule
import com.example.nagwatask.models.*
import com.example.nagwatask.models.file.DownloadMapper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import zlc.season.rxdownload4.download
import zlc.season.rxdownload4.file
import javax.inject.Inject


class DownloadFileRepository @Inject constructor(private val networkModel: NetworkModule) {

    private val mediatorResponse: MediatorLiveData<ApiState<DownloadMapper>> = MediatorLiveData()

    private var disposable: Disposable? = null
    fun downloadFile(url: String): MediatorLiveData<ApiState<DownloadMapper>> {
        mediatorResponse.value = LoadingState()
        disposable = url.download()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    mediatorResponse.value = ProgressState(it.percent().toString(), DownloadMapper(null))
                },
                onComplete = {
                    mediatorResponse.value = SuccessState("", DownloadMapper(url.file()))
                },
                onError = {
                    mediatorResponse.value = ErrorState(it.message)
                }
            )
        return mediatorResponse
    }


}