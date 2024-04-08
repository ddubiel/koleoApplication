package com.koleo.search

import com.koleo.cache.datasource.CacheStationKeywordsDataSource
import com.koleo.cache.datasource.CacheStationsDataSource
import com.koleo.common.asDomain
import com.koleo.common.model.StationKeywordsModel
import com.koleo.common.model.StationModel
import javax.inject.Inject

class SearchRepository @Inject constructor(
    private val stationKeywordsDataSource: CacheStationKeywordsDataSource,
    private val stationsDataSource: CacheStationsDataSource,
) {
    suspend fun getStations(): List<StationModel> =
        stationsDataSource.getStations().map { it.asDomain() }

    suspend fun getStationKeywords(): List<StationKeywordsModel> =
        stationKeywordsDataSource.getStationKeywords().map { it.asDomain() }

}
