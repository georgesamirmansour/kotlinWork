package com.example.nagwatask.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.example.nagwatask.data.mian.DownloadFileRepository
import com.example.nagwatask.data.mian.GetFileRepository
import com.example.nagwatask.models.ApiState
import com.example.nagwatask.models.file.DownloadMapper
import com.example.nagwatask.models.file.FileMapper
import okhttp3.ResponseBody
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val getFileRepository: GetFileRepository,
    private val downloadFileRepository: DownloadFileRepository
) : ViewModel() {

    private lateinit var fileMediatorMediator: MediatorLiveData<ApiState<List<FileMapper>>>
    private lateinit var downloadFileMediator: MediatorLiveData<ApiState<DownloadMapper>>

    fun setFileRequest() {
        fileMediatorMediator = getFileRepository.getFile()
    }

    fun getFileApi(): LiveData<ApiState<List<FileMapper>>> {
        return fileMediatorMediator
    }

    fun setDownloadFile(url: String) {
        downloadFileMediator = downloadFileRepository.downloadFile(url)
    }

    fun getDownloadFile(): LiveData<ApiState<DownloadMapper>> {
        return downloadFileMediator
    }
}