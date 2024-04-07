package com.koleo.cache.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.koleo.cache.room.entities.StationEntity

@Dao
interface StationDao {

    @Query("SELECT * from station_entity")
    suspend fun getStations(): List<StationEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(stations: List<StationEntity>)

    @Update
    suspend fun update(stationEntity: StationEntity)

    @Delete
    suspend fun delete(stationEntity: StationEntity)
}