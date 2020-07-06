package com.pandulapeter.beagle.core.util

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.display.DisplayManager
import android.hardware.display.VirtualDisplay
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.os.Build
import android.os.Handler
import android.os.HandlerThread
import android.os.IBinder
import android.os.Process
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.pandulapeter.beagle.BeagleCore

@RequiresApi(Build.VERSION_CODES.M)
class ScreenshotService : Service() {
    private var projection: MediaProjection? = null
    private var virtualDisplay: VirtualDisplay? = null
    private val handlerThread = HandlerThread(javaClass.simpleName, Process.THREAD_PRIORITY_BACKGROUND)
    private lateinit var handler: Handler
    private lateinit var mgr: MediaProjectionManager
    private lateinit var windowManager: WindowManager

    override fun onCreate() {
        super.onCreate()
        mgr = getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
        windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        handlerThread.start()
        handler = Handler(handlerThread.looper)
    }

    override fun onStartCommand(i: Intent, flags: Int, startId: Int): Int {
        moveToForeground()
        handler.postDelayed({
            startCapture(i.getIntExtra(EXTRA_RESULT_CODE, 0), i.getParcelableExtra(EXTRA_RESULT_INTENT) as Intent)
        }, SCREENSHOT_DELAY)
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        stopCapture()
        super.onDestroy()
    }

    override fun onBind(intent: Intent): IBinder? = throw IllegalStateException("Binding not supported.")

    private fun stopCapture() {
        if (projection != null) {
            projection!!.stop()
            virtualDisplay!!.release()
            projection = null
        }
    }

    private fun startCapture(resultCode: Int, resultData: Intent) {
        projection = mgr.getMediaProjection(resultCode, resultData)
        val screenshotWriter = ScreenshotWriter(windowManager, handler) { bitmap ->
            BeagleCore.implementation.onScreenshotReady?.let {
                it(bitmap)
                BeagleCore.implementation.onScreenshotReady = null
            }
            stopCapture()
            stopForeground(true)
            stopSelf()
        }
        val cb: MediaProjection.Callback = object : MediaProjection.Callback() {
            override fun onStop() {
                virtualDisplay!!.release()
            }
        }
        virtualDisplay = projection?.createVirtualDisplay(
            "beagleScreenshot",
            screenshotWriter.width,
            screenshotWriter.height,
            resources.displayMetrics.densityDpi,
            VIRTUAL_DISPLAY_FLAGS,
            screenshotWriter.surface,
            null,
            handler
        )
        projection?.registerCallback(cb, handler)
    }

    private fun moveToForeground() {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && notificationManager.getNotificationChannel(CHANNEL_WHATEVER) == null) {
            notificationManager.createNotificationChannel(NotificationChannel(CHANNEL_WHATEVER, "Beagle screenshot", NotificationManager.IMPORTANCE_DEFAULT))
        }
        startForeground(
            NOTIFICATION_ID, NotificationCompat.Builder(this, CHANNEL_WHATEVER)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentTitle("Taking screenshot...").build()
        )
    }

    companion object {
        private const val CHANNEL_WHATEVER = "channel_beagle_screenshot"
        private const val NOTIFICATION_ID = 2657
        private const val SCREENSHOT_DELAY = 300L
        const val EXTRA_RESULT_CODE = "resultCode"
        const val EXTRA_RESULT_INTENT = "resultIntent"
        const val VIRTUAL_DISPLAY_FLAGS = DisplayManager.VIRTUAL_DISPLAY_FLAG_OWN_CONTENT_ONLY or DisplayManager.VIRTUAL_DISPLAY_FLAG_PUBLIC
    }
}