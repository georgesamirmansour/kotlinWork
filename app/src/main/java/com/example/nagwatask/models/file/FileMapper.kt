package com.example.nagwatask.models.file

import java.io.File

class FileMapper constructor(fileResponse: FileResponse){
    var id: Int? = fileResponse.id
    var type: String = fileResponse.type
    var url: String = fileResponse.url
    var name: String = fileResponse.name
    var downloadState: DownloadState = DownloadState.NotStartedYet
    var progressDownload: Int = 0
    var file: File? = null

    override fun equals(other: Any?): Boolean {
        if(javaClass != other?.javaClass)
            return false;
        other as FileMapper
        return when {
            id !=other.id -> false
            downloadState !=other.downloadState -> false
            url !=other.url -> false
            type !=other.type -> false
            else -> true
        }
    }
    enum class DownloadState{
        Downloaded, Downloading, NotStartedYet
    }
}