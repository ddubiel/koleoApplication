package com.koleo.koleoapplication.workers

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.koleo.cache.room.dao.StationDao
import com.koleo.cache.room.dao.StationKeywordDao
import com.koleo.cache.room.entities.StationEntity
import com.koleo.cache.room.entities.StationKeywordEntity
import com.koleo.core.PeriodicInterval
import com.koleo.core.PeriodicScheduler
import com.koleo.core.PeriodicWorkerScheduler
import com.koleo.network.StationsRepository
import com.koleo.network.model.Station
import com.koleo.network.model.StationKeyword
import com.koleo.network.onError
import com.koleo.network.onSuccess
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.async
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit


@HiltWorker
class DailyWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val stationsRepository: StationsRepository,
    private val stationsDao: StationDao,
    private val stationsKeywordDao: StationKeywordDao,

) : CoroutineWorker(context, workerParams) {

    companion object Scheduler : PeriodicScheduler {
        private const val WORKER_NAME = "DailyWorker"
        private const val SYNC_INTERVAL = 1L
        override fun schedulePeriodically(
            scheduler: PeriodicWorkerScheduler,
            interval: Long,
            unit: TimeUnit
        ) {
            scheduler.run<DailyWorker>(
                WORKER_NAME,
                PeriodicInterval(
                    SYNC_INTERVAL,
                    TimeUnit.MINUTES
                )
            )
        }
    }

    override suspend fun doWork(): Result {
        Log.e("dawid", "doingWork")
        withContext(currentCoroutineContext()) {

            async {
                stationsRepository.getStations().onSuccess {
                    it?.let {
                        stationsDao.insert(it.map {
                            it.toStationEntity()
                        })
                    }

                }.onError {
                    Log.e("dawid", it.error?.message.orEmpty())
                }
            }
            async {
                stationsRepository.getStationsKeywords().onSuccess {
                    it?.let {
                        stationsKeywordDao.insert(it.map {
                            it.toStationEntity()
                        })
                    }

                }.onError {
                    Log.e("dawid", it.error?.message.orEmpty())
                }
            }

        }

        return Result.success()
    }
}

private fun Station.toStationEntity(): StationEntity {
    return StationEntity(
        id = this.id ?: 0L,
        name = this.name.orEmpty(),
        longitude = longitude ?: 0.0,
        latitude = latitude ?: 0.0
    )
}


private fun StationKeyword.toStationEntity(): StationKeywordEntity {
    return StationKeywordEntity (
        id = this.id ?: 0L,
        keyword = this.keyword.orEmpty(),
        stationId = this.stationId?: 0L
    )
}
