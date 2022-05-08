package com.example.lurk.api.responses
import com.google.gson.annotations.SerializedName

data class Subreddit(
    @SerializedName("allowedPostTypes")
    var allowedPostTypes: AllowedPostTypes = AllowedPostTypes(),
    @SerializedName("communityIcon")
    var communityIcon: String = "",
    @SerializedName("icon")
    var icon: String = "",
    @SerializedName("id")
    var id: String = "",
    @SerializedName("name")
    var name: String = "",
    @SerializedName("numSubscribers")
    var numSubscribers: Int = 0,
    @SerializedName("primaryColor")
    var primaryColor: String = ""
)