package com.koleo.cache.datasource

import com.koleo.cache.room.dao.StationKeywordDao
import com.koleo.cache.room.entities.StationKeywordEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CacheStationKeywordsDataSource @Inject constructor(
    private val stationKeywordDao: StationKeywordDao
) {

    fun getStationKeywordsFlow(): Flow<List<StationKeywordEntity>?> =
        stationKeywordDao.getStationKeywordsFlow()

    suspend fun getStationKeywords(): List<StationKeywordEntity> =
        stationKeywordDao.getStationKeywords()

    suspend fun saveStationKeywords(stationKeywords: List<StationKeywordEntity>) =
        stationKeywordDao.insert(stationKeywords)
}
