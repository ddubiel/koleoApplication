package com.koleo.cache.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.koleo.cache.room.dao.StationDao
import com.koleo.cache.room.dao.StationKeywordDao
import com.koleo.cache.room.entities.StationEntity
import com.koleo.cache.room.entities.StationKeywordEntity


@Database(
    entities = [
        StationKeywordEntity::class,
        StationEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class KoleoAppDatabase : RoomDatabase() {

    abstract val stationKeywordDao: StationKeywordDao

    abstract val stationDao: StationDao

    companion object {

        private const val DATABASE_NAME = "KoleoAppDatabase"

        @Synchronized
        fun getDatabase(context: Context): KoleoAppDatabase {
            return Room.databaseBuilder(context, KoleoAppDatabase::class.java, DATABASE_NAME)
                .fallbackToDestructiveMigration().build()
        }
    }
}