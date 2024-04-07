package com.koleo.core

import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ListenableWorker
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import javax.inject.Inject

class PeriodicWorkerScheduler @Inject constructor(
    val workManager: WorkManager
) {

    inline fun <reified W : ListenableWorker> run(
        workerName: String,
        periodicInterval: PeriodicInterval,
        periodicWorkPolicy: ExistingPeriodicWorkPolicy = ExistingPeriodicWorkPolicy.REPLACE,
        constraints: Constraints = Constraints.NONE

    ) {
        val workRequest = buildRequest<W>(constraints, periodicInterval)
        workManager.enqueueUniquePeriodicWork(workerName, periodicWorkPolicy, workRequest)
    }

    inline fun <reified W : ListenableWorker> buildRequest(
        constraints: Constraints,
        periodicInterval: PeriodicInterval
    ) =
        PeriodicWorkRequestBuilder<W>(
            repeatInterval = periodicInterval.repeatInterval,
            repeatIntervalTimeUnit = periodicInterval.repeatIntervalUnit
        ).setConstraints(constraints).build()

}