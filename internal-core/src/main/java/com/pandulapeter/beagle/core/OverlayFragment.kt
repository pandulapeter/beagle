package com.pandulapeter.beagle.core

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.media.projection.MediaProjectionManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.core.util.ScreenCaptureService
import com.pandulapeter.beagle.core.util.extension.text
import com.pandulapeter.beagle.utils.BundleArgumentDelegate


internal class OverlayFragment : Fragment() {

    private var fileName: String = "file"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) = BeagleCore.implementation.createOverlayLayout(requireActivity(), this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        fileName = savedInstanceState?.fileName ?: fileName
    }

    fun startCapture(isForVideo: Boolean, fileName: String) {
        requestNotificationPermission {
            (getSystemService(Context.MEDIA_PROJECTION_SERVICE) as? MediaProjectionManager?).let { mediaProjectionManager ->
                if (mediaProjectionManager == null) {
                    BeagleCore.implementation.onScreenCaptureReady?.invoke(null)
                } else {
                    this@OverlayFragment.fileName = fileName
                    startActivityForResult(mediaProjectionManager.createScreenCaptureIntent(), if (isForVideo) SCREEN_RECORDING_REQUEST else SCREENSHOT_REQUEST)
                }
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            SCREENSHOT_REQUEST,
            SCREEN_RECORDING_REQUEST -> {
                if (data == null) {
                    BeagleCore.implementation.onScreenCaptureReady?.invoke(null)
                } else {
                    requestNotificationPermission {
                        startService(
                            ScreenCaptureService.getStartIntent(
                                context = this,
                                resultCode = resultCode,
                                data = data,
                                isForVideo = requestCode == SCREEN_RECORDING_REQUEST,
                                fileName = fileName
                            )
                        )
                    }
                }
            }

            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.fileName = fileName
    }

    private fun requestNotificationPermission(doWhenGranted: Context.() -> Unit) {
        context?.run {
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            if (notificationManager.areNotificationsEnabled()) {
                doWhenGranted()
            } else {
                Toast.makeText(this, text(BeagleCore.implementation.appearance.screenCaptureTexts.permissionToast), Toast.LENGTH_SHORT).show()
                startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).setData(Uri.fromParts("package", packageName, null)))
            }
        }
    }

    companion object {
        private var Bundle.fileName by BundleArgumentDelegate.String("fileName")
        private const val SCREENSHOT_REQUEST = 4246
        private const val SCREEN_RECORDING_REQUEST = 4247

        fun newInstance() = OverlayFragment()
    }
}