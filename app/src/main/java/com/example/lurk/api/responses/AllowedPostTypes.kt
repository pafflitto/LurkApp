package com.example.lurk.api.responses
import com.google.gson.annotations.SerializedName

data class AllowedPostTypes(
    @SerializedName("images")
    var images: Boolean = false,
    @SerializedName("links")
    var links: Boolean = false,
    @SerializedName("spoilers")
    var spoilers: Boolean = false,
    @SerializedName("text")
    var text: Boolean = false,
    @SerializedName("videos")
    var videos: Boolean = false
)