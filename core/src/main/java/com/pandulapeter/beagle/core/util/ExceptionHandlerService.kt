package com.pandulapeter.beagle.core.util

import android.app.Application
import android.app.Service
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.Message
import android.os.Messenger
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.common.configuration.Appearance
import com.pandulapeter.beagle.common.configuration.Behavior
import com.pandulapeter.beagle.core.view.bugReport.BugReportActivity
import com.pandulapeter.beagle.utils.BundleArgumentDelegate

internal class ExceptionHandlerService : Service() {

    private val handler by lazy { ExceptionHandler(applicationContext as Application) }

    override fun onBind(intent: Intent?): IBinder? = Messenger(handler).binder

    internal class ExceptionHandler(
        private val application: Application
    ) : Handler(Looper.getMainLooper()) {

        override fun handleMessage(msg: Message) {
            //TODO: Beagle needs to be restored.
            BeagleCore.implementation.initialize(
                application = application,
//                appearance = msg.data.appearance ?: Appearance(),
//                behavior = msg.data.behavior ?: Behavior()
            )
            crashLogEntryAdapter.fromJson(msg.data.crashLogEntry)?.let { crashLogEntry ->
                BeagleCore.implementation.logCrash(crashLogEntry)
                application.startActivity(
                    BugReportActivity.newIntent(
                        context = application,
                        crashLogIdToShow = crashLogEntry.id
                    ).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                )
            }
        }
    }

    companion object {
        //        private var Bundle.appearance by BundleArgumentDelegate.Parcelable<Appearance>("appearance")
//        private var Bundle.behavior by BundleArgumentDelegate.Parcelable<Behavior>("behavior")
        private var Bundle.crashLogEntry by BundleArgumentDelegate.String("crashLogEntry")

        fun createBundle(
            appearance: Appearance,
            behavior: Behavior,
            crashLogEntry: CrashLogEntry
        ) = Bundle().apply {
//            this.appearance = appearance
//            this.behavior = behavior
            this.crashLogEntry = crashLogEntryAdapter.toJson(crashLogEntry)
        }
    }
}
