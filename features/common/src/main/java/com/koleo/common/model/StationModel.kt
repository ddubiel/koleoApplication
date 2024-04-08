package com.koleo.common.model

data class StationModel(
    val id: Long,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val hits: Int,
)
