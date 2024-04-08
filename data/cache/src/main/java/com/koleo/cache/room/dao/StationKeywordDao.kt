package com.koleo.cache.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.koleo.cache.room.entities.StationKeywordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StationKeywordDao {

    @Query("SELECT * from station_keyword_entity")
    suspend fun getStationKeywords(): List<StationKeywordEntity>

    @Query("SELECT * from station_keyword_entity")
    fun getStationKeywordsFlow(): Flow<List<StationKeywordEntity>?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(stations: List<StationKeywordEntity>)

    @Update
    suspend fun update(stationEntity: StationKeywordEntity)

    @Delete
    suspend fun delete(stationEntity: StationKeywordEntity)
}