package com.koleo.koleoapplication.utils

import com.koleo.core.PeriodicWorkerScheduler
import com.koleo.koleoapplication.workers.DailyWorker
import javax.inject.Inject


class WorkersUtil @Inject constructor(
    private val periodicWorkerScheduler: PeriodicWorkerScheduler,
    private val dailyWorker: DailyWorker.Scheduler,
) {

    fun scheduleWorkers() {
        dailyWorker.schedulePeriodically(periodicWorkerScheduler)
    }

}
