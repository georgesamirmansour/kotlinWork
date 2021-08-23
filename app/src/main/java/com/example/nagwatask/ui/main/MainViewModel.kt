package com.example.nagwatask.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.nagwatask.data.mian.MainRepo
import com.example.nagwatask.di.module.NetworkModule
import com.example.nagwatask.models.ApiState
import com.example.nagwatask.models.file.FileMapper
import javax.inject.Inject

class MainViewModel @Inject constructor(private val mainRepo: MainRepo) : ViewModel() {



    private val fileMediatorLiveData: MediatorLiveData<ApiState<List<FileMapper>>> =
        MediatorLiveData()

    fun callApi() {
        fileMediatorLiveData.addSource(mainRepo.getFile(), Observer {
            it?.let { fileMediatorLiveData.value = it }
        })
    }

    fun getFileApi(): LiveData<ApiState<List<FileMapper>>> {
        return fileMediatorLiveData
    }
}