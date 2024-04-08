package com.koleo.core

import androidx.work.PeriodicWorkRequest
import java.util.concurrent.TimeUnit

data class PeriodicInterval(
    val repeatInterval: Long = PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS,
    val repeatIntervalUnit: TimeUnit = TimeUnit.MILLISECONDS,
    val flexTimeInterval: Long = PeriodicWorkRequest.MIN_PERIODIC_FLEX_MILLIS,
    val flexTimeIntervalUnit: TimeUnit = TimeUnit.MILLISECONDS
)