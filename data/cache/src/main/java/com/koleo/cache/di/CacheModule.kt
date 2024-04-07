package com.koleo.cache.di

import android.content.Context
import com.koleo.cache.room.KoleoAppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class CacheModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context) = KoleoAppDatabase.getDatabase(context)

    @Provides
    @Singleton
    fun provideStationDao(database: KoleoAppDatabase) = database.stationDao

    @Provides
    @Singleton
    fun provideStationKeywordDao(database: KoleoAppDatabase) = database.stationKeywordDao

}