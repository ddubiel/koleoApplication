package com.koleo.core

import java.util.concurrent.TimeUnit

interface PeriodicScheduler {

    fun schedulePeriodically(
        scheduler: PeriodicWorkerScheduler,
        interval: Long = 0L,
        unit: TimeUnit = TimeUnit.HOURS
    )

}