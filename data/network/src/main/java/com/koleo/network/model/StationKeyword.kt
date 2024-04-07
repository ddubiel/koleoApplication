package com.koleo.network.model

import com.google.gson.annotations.SerializedName

data class StationKeyword(
    @SerializedName("id"         ) var id        : Long?    = null,
    @SerializedName("keyword"    ) var keyword   : String? = null,
    @SerializedName("station_id" ) var stationId : Long?    = null
)
