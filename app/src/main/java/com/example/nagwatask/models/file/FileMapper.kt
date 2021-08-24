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
        return when {
            id !=other.id -> false
            downloading !=other.downloading -> false
            downloaded !=other.downloaded -> false
            url !=other.url -> false
            type !=other.type -> false
            else -> true
        }
    }
}