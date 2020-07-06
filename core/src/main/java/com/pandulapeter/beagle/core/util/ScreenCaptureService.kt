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

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
internal class ScreenCaptureService : Service() {
    private var projection: MediaProjection? = null
    private var virtualDisplay: VirtualDisplay? = null
    private val handlerThread = HandlerThread(javaClass.simpleName, Process.THREAD_PRIORITY_BACKGROUND)
    private lateinit var handler: Handler

    override fun onCreate() {
        super.onCreate()
        handlerThread.start()
        handler = Handler(handlerThread.looper)
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        moveToForeground()
        handler.postDelayed({ startCapture(intent.getIntExtra(EXTRA_RESULT_CODE, 0), intent.getParcelableExtra(EXTRA_RESULT_INTENT) as Intent) }, SCREENSHOT_DELAY)
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        stopCapture()
        super.onDestroy()
    }

    override fun onBind(intent: Intent): IBinder? = throw IllegalStateException("Binding not supported.")

    private fun stopCapture() {
        if (projection != null) {
            projection?.stop()
            virtualDisplay?.release()
            projection = null
        }
    }

    private fun startCapture(resultCode: Int, resultData: Intent) {
        projection = (getSystemService(Context.MEDIA_PROJECTION_SERVICE) as? MediaProjectionManager?)?.getMediaProjection(resultCode, resultData)
        val screenshotWriter = ScreenshotWriter(getSystemService(Context.WINDOW_SERVICE) as WindowManager, handler) { bitmap ->
            BeagleCore.implementation.onScreenshotReady?.let { callback ->
                callback(bitmap)
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && notificationManager.getNotificationChannel(NOTIFICATION_CHANNEL_ID) == null) {
            notificationManager.createNotificationChannel(NotificationChannel(NOTIFICATION_CHANNEL_ID, "Beagle screenshot", NotificationManager.IMPORTANCE_DEFAULT))
        }
        startForeground(
            NOTIFICATION_ID, NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentTitle("Taking screenshot...").build()
        )
    }

    companion object {
        private const val NOTIFICATION_CHANNEL_ID = "channel_beagle_screenshot"
        private const val NOTIFICATION_ID = 2657
        private const val SCREENSHOT_DELAY = 300L
        private const val EXTRA_RESULT_CODE = "resultCode"
        private const val EXTRA_RESULT_INTENT = "resultIntent"
        private const val VIRTUAL_DISPLAY_FLAGS = DisplayManager.VIRTUAL_DISPLAY_FLAG_OWN_CONTENT_ONLY or DisplayManager.VIRTUAL_DISPLAY_FLAG_PUBLIC

        fun getStartIntent(context: Context, resultCode: Int, data: Intent) = Intent(context, ScreenCaptureService::class.java)
            .putExtra(EXTRA_RESULT_CODE, resultCode)
            .putExtra(EXTRA_RESULT_INTENT, data)
    }
}