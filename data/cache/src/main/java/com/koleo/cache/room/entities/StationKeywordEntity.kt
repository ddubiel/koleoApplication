package com.koleo.cache.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "station_keyword_entity")
data class StationKeywordEntity(
    @PrimaryKey
    @ColumnInfo(name = "id") val id: Long,
    @ColumnInfo(name = "keyword") val keyword: String,
    @ColumnInfo(name = "station_id") val stationId: Long,
)