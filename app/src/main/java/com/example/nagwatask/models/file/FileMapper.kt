package com.example.nagwatask.models.file

class FileMapper constructor(fileResponse: FileResponse){
    var id: Int? = fileResponse.id
    var type: String = fileResponse.type
    var url: String = fileResponse.url
    var name: String = fileResponse.name
    var downloaded: Boolean = false
    var downloading: Boolean = false

    override fun equals(other: Any?): Boolean {
        if(javaClass != other?.javaClass)
            return false;
        other as FileMapper
        if(id !=other.id)
            return false
        if(downloading !=other.downloading)
            return false
        if(downloaded !=other.downloaded)
            return false
        if(url !=other.url)
            return false
        if(type !=other.type)
            return false
        return true
    }
}