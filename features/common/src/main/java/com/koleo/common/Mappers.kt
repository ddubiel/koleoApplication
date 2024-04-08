package com.koleo.common

import com.koleo.cache.room.entities.StationEntity
import com.koleo.cache.room.entities.StationKeywordEntity
import com.koleo.common.model.StationKeywordsModel
import com.koleo.common.model.StationModel

fun StationKeywordEntity.asDomain() =
    StationKeywordsModel(
        id = id, keyword = keyword, stationId = stationId
    )

fun StationEntity.asDomain() =
    StationModel(
        id = id, name = name, latitude = latitude, longitude = longitude, hits = hits
    )
