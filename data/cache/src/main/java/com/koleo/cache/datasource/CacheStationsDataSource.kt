package com.koleo.cache.datasource

import com.koleo.cache.room.dao.StationDao
import com.koleo.cache.room.entities.StationEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CacheStationsDataSource @Inject constructor(
    private val stationDao: StationDao
) {

    fun getStationsFlow(): Flow<List<StationEntity>?> = stationDao.getStationsFlow()
    suspend fun getStations(): List<StationEntity> = stationDao.getStations()
    suspend fun saveStations(stations: List<StationEntity>) = stationDao.insert(stations)
}
