package dev.yashh.devhub

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

/**
 * Main application class for DevHub.
 * Sets up Hilt dependency injection and Timber logging.
 */
@HiltAndroidApp
class DevHubApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            // Setup logging for debug builds
            Timber.plant(Timber.DebugTree())
        }
    }
}
