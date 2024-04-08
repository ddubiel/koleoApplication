package com.koleo.koleoapplication

import android.app.Application
import androidx.work.Configuration
import com.koleo.koleoapplication.di.modules.HiltWorkerFactoryEntryPoint
import com.koleo.koleoapplication.utils.WorkersUtil
import dagger.hilt.EntryPoints
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class MainApp : Application(), Configuration.Provider {

    @Inject
    lateinit var workersUtil: WorkersUtil

    override fun onCreate() {
        super.onCreate()
        workersUtil.scheduleWorkers()
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(
                EntryPoints.get(this, HiltWorkerFactoryEntryPoint::class.java).workerFactory()
            )
            .build()
}
