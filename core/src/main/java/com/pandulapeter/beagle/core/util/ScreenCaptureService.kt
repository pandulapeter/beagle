package com.pandulapeter.beagle.core.util

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.display.DisplayManager
import android.hardware.display.VirtualDisplay
import android.media.CamcorderProfile
import android.media.MediaRecorder
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.os.Build
import android.os.Handler
import android.os.HandlerThread
import android.os.IBinder
import android.os.Process
import android.util.DisplayMetrics
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.FileProvider
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.core.util.extension.shareFile
import java.io.File

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
internal class ScreenCaptureService : Service() {

    private var projection: MediaProjection? = null
    private var virtualDisplay: VirtualDisplay? = null
    private var mediaRecorder: MediaRecorder? = null
    private val handlerThread = HandlerThread(javaClass.simpleName, Process.THREAD_PRIORITY_BACKGROUND)
    private lateinit var handler: Handler
    private lateinit var file: File

    override fun onCreate() {
        super.onCreate()
        handlerThread.start()
        handler = Handler(handlerThread.looper)
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        moveToForeground()
        handler.postDelayed({
            startCapture(
                resultCode = intent.getIntExtra(EXTRA_RESULT_CODE, 0),
                resultData = intent.getParcelableExtra(EXTRA_RESULT_INTENT) as Intent,
                isForVideo = intent.getBooleanExtra(EXTRA_IS_FOR_VIDEO, false)
            )
        }, SCREENSHOT_DELAY)
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        stopCapture()
        super.onDestroy()
    }

    override fun onBind(intent: Intent): IBinder? = throw IllegalStateException("Binding not supported.")

    private fun stopCapture() {
        projection?.stop()
        mediaRecorder?.stop()
        mediaRecorder?.release()
        mediaRecorder = null
        virtualDisplay?.release()
        projection = null
    }

    private fun startCapture(resultCode: Int, resultData: Intent, isForVideo: Boolean) {
        projection = (getSystemService(Context.MEDIA_PROJECTION_SERVICE) as? MediaProjectionManager?)?.getMediaProjection(resultCode, resultData)
        val displayMetrics = DisplayMetrics()
        (getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.getMetrics(displayMetrics)
        var width = displayMetrics.widthPixels
        var height = displayMetrics.heightPixels
        while (width * height > 2 shl 19) {
            width = width shr 1
            height = height shr 1
        }
        if (isForVideo) {
            mediaRecorder = MediaRecorder().apply {
                setVideoSource(MediaRecorder.VideoSource.SURFACE)
                CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH).apply {
                    videoFrameHeight = width  //TODO
                    videoFrameWidth = height
                }.let { profile ->
                    setOutputFormat(profile.fileFormat)
                    setVideoFrameRate(profile.videoFrameRate)
                    setVideoSize(profile.videoFrameWidth, profile.videoFrameHeight)
                    setVideoEncodingBitRate(profile.videoBitRate)
                    setVideoEncoder(profile.videoCodec)
                }
                file = createFile("test_${System.currentTimeMillis()}.mp4")
                setOutputFile(file.absolutePath) //TODO
                prepare()
            }
            virtualDisplay = projection?.createVirtualDisplay(
                "captureDisplay",
                displayMetrics.widthPixels,
                displayMetrics.heightPixels,
                displayMetrics.densityDpi,
                DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                mediaRecorder?.surface,
                null,
                handler
            )
            mediaRecorder?.start()
            handler.postDelayed({
                stopCapture()
                //TODO
                val uri = FileProvider.getUriForFile(applicationContext, applicationContext.packageName + ".beagle.fileProvider", file)
                BeagleCore.implementation.currentActivity?.shareFile(uri, "video/mp4", "Share video")
                stopForeground(true)
                stopSelf()
            }, 2000)
        } else {
            val screenshotWriter = ScreenshotWriter(displayMetrics.widthPixels, displayMetrics.heightPixels, handler) { bitmap ->
                BeagleCore.implementation.onScreenshotReady?.let { callback ->
                    callback(bitmap)
                    BeagleCore.implementation.onScreenshotReady = null
                }
                stopCapture()
                stopForeground(true)
                stopSelf()
            }
            virtualDisplay = projection?.createVirtualDisplay(
                "beagleScreenshot",
                displayMetrics.widthPixels,
                displayMetrics.heightPixels,
                displayMetrics.densityDpi,
                DisplayManager.VIRTUAL_DISPLAY_FLAG_OWN_CONTENT_ONLY or DisplayManager.VIRTUAL_DISPLAY_FLAG_PUBLIC,
                screenshotWriter.surface,
                null,
                handler
            )
            projection?.registerCallback(object : MediaProjection.Callback() {
                override fun onStop() {
                    virtualDisplay?.release()
                }
            }, handler)
        }
    }

    private fun moveToForeground() {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && notificationManager.getNotificationChannel(NOTIFICATION_CHANNEL_ID) == null) {
            notificationManager.createNotificationChannel(NotificationChannel(NOTIFICATION_CHANNEL_ID, "Beagle screen captures", NotificationManager.IMPORTANCE_DEFAULT))
        }
        startForeground(
            NOTIFICATION_ID, NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentTitle("Recording…").build()
        )
    }

    private fun createFile(fileName: String): File {
        val folder = File(cacheDir, "beagleScreenCaptures")
        folder.mkdirs()
        return File(folder, fileName)
    }

    companion object {
        private const val NOTIFICATION_CHANNEL_ID = "channel_beagle_screen_capture"
        private const val NOTIFICATION_ID = 2657
        private const val SCREENSHOT_DELAY = 300L
        private const val EXTRA_RESULT_CODE = "resultCode"
        private const val EXTRA_RESULT_INTENT = "resultIntent"
        private const val EXTRA_IS_FOR_VIDEO = "isForVideo"

        fun getStartIntent(context: Context, resultCode: Int, data: Intent, isForVideo: Boolean) = Intent(context, ScreenCaptureService::class.java)
            .putExtra(EXTRA_RESULT_CODE, resultCode)
            .putExtra(EXTRA_RESULT_INTENT, data)
            .putExtra(EXTRA_IS_FOR_VIDEO, isForVideo)
    }
}