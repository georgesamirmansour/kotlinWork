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

    enum class DownloadState{
        Downloaded, Downloading, NotStartedYet
    }
}