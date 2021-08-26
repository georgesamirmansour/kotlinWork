package com.example.nagwatask.data.mian

import android.util.Log
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

    private lateinit var mediatorResponse: MediatorLiveData<ApiState<DownloadMapper>>
    private var disposable: Disposable? = null

    fun downloadFile(url: String): MediatorLiveData<ApiState<DownloadMapper>> {
        mediatorResponse = MediatorLiveData()
        mediatorResponse.value = ProgressState("0.0", DownloadMapper(null))
        disposable = url.download()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    val percent = (it.downloadSize / it.totalSize) *100
                    //mediatorResponse.value = ProgressState(percent.toString(), DownloadMapper(null))

                    Log.d("Progress", percent.toString())
                },
                onComplete = {
//                    Log.d(DownloadFileRepository::javaClass.name, "file downloaded")
                    mediatorResponse.value = SuccessState("", DownloadMapper(url.file()))
                    Log.d("File", url.file().absolutePath)
                },
                onError = {
//                    Log.d(DownloadFileRepository::javaClass.name, "file not downloaded")
                    mediatorResponse.value = ErrorState(it.message)
                }
            )
        return mediatorResponse
    }


}