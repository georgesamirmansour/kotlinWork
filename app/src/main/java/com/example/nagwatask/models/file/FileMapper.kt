package com.example.nagwatask.models.file

class FileMapper constructor(fileResponse: FileResponse){
    var id: Int? = fileResponse.id
    var type: String = fileResponse.type
    var url: String = fileResponse.url
    var name: String = fileResponse.name
    var downloaded: Boolean = false

}