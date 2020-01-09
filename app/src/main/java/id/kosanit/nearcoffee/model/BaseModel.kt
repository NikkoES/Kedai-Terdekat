package id.kosanit.nearcoffee.model

import com.google.gson.annotations.SerializedName

data class BaseModel(
    @SerializedName("status") val status: String?,
    @SerializedName("data") val data: String?
)