package com.example.nagwatask.models.file

import com.google.gson.annotations.SerializedName

data class FileResponse(
    @SerializedName("id") val id: Int, @SerializedName("type") val type: String,
    @SerializedName("url") val url: String, @SerializedName("name") val name: String
)