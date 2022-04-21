package com.pandulapeter.beagle.appDemo

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.pandulapeter.beagle.Beagle
import com.pandulapeter.beagle.logCrash.BeagleCrashLogger
import com.pandulapeter.beagle.appDemo.data.networking.Constants
import com.pandulapeter.beagle.common.configuration.Appearance
import com.pandulapeter.beagle.common.configuration.Behavior
import com.pandulapeter.beagle.common.configuration.toText
import com.pandulapeter.beagle.log.BeagleLogger
import com.pandulapeter.beagle.logOkHttp.BeagleOkHttpLogger
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

@Suppress("unused")
class BeagleDemoApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@BeagleDemoApplication)
            modules(modules)
        }
        Beagle.initialize(
            application = this,
            appearance = Appearance(
                themeResourceId = R.style.DebugMenuTheme
            ),
            behavior = Behavior(
                logBehavior = Behavior.LogBehavior(
                    loggers = listOf(BeagleLogger)
                ),
                networkLogBehavior = Behavior.NetworkLogBehavior(
                    baseUrl = Constants.BASE_URL,
                    networkLoggers = listOf(BeagleOkHttpLogger)
                ),
                bugReportingBehavior = Behavior.BugReportingBehavior(
                    crashLoggers = listOf(BeagleCrashLogger),
                    logRestoreLimit = 5,
                    buildInformation = {
                        listOf(
                            "Version name".toText() to BuildConfig.VERSION_NAME,
                            "Version code".toText() to BuildConfig.VERSION_CODE.toString(),
                            "Application ID".toText() to BuildConfig.APPLICATION_ID
                        )
                    }
                )
            )
        )
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        @Suppress("ConstantConditionIf")
        if (BuildConfig.BUILD_TYPE == "debug") {
            MultiDex.install(this)
        }
    }
}