package com.koleo.network.model

import com.google.gson.annotations.SerializedName


data class Station(
    @SerializedName("id"                ) var id               : Long?     = null,
    @SerializedName("name"              ) var name             : String?  = null,
    @SerializedName("name_slug"         ) var nameSlug         : String?  = null,
    @SerializedName("latitude"          ) var latitude         : Double?  = null,
    @SerializedName("longitude"         ) var longitude        : Double?  = null,
    @SerializedName("hits"              ) var hits             : Int?     = null,
    @SerializedName("ibnr"              ) var ibnr             : Int?     = null,
    @SerializedName("city"              ) var city             : String?  = null,
    @SerializedName("region"            ) var region           : String?  = null,
    @SerializedName("country"           ) var country          : String?  = null,
    @SerializedName("localised_name"    ) var localisedName    : String?  = null,
    @SerializedName("is_group"          ) var isGroup          : Boolean? = null,
    @SerializedName("has_announcements" ) var hasAnnouncements : Boolean? = null
)
