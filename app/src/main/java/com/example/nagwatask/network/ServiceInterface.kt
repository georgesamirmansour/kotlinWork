package com.example.nagwatask.network

import com.example.nagwatask.models.file.FileResponse
import io.reactivex.Flowable
import retrofit2.http.GET


interface ServiceInterface {

    @GET("movies")
    fun getAllFiles(): Flowable<List<FileResponse>>

}