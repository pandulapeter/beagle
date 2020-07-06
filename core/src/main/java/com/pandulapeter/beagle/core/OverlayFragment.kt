package com.pandulapeter.beagle.core

import android.content.Context
import android.content.Intent
import android.media.projection.MediaProjectionManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.core.util.ScreenCaptureService


internal class OverlayFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) = BeagleCore.implementation.createOverlayLayout(requireActivity())

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun startCapture(isForVideo: Boolean) {
        (context?.getSystemService(Context.MEDIA_PROJECTION_SERVICE) as? MediaProjectionManager?).let { mediaProjectionManager ->
            if (mediaProjectionManager == null) {
                BeagleCore.implementation.onScreenshotReady?.invoke(null)
            } else {
                startActivityForResult(mediaProjectionManager.createScreenCaptureIntent(), if (isForVideo) SCREEN_RECORDING_REQUEST else SCREENSHOT_REQUEST)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            SCREENSHOT_REQUEST,
            SCREEN_RECORDING_REQUEST -> {
                if (data == null || Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    BeagleCore.implementation.onScreenshotReady?.invoke(null)
                } else {
                    requireContext().run { startService(ScreenCaptureService.getStartIntent(this, resultCode, data, requestCode == SCREEN_RECORDING_REQUEST)) }
                }
            }
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    companion object {
        const val TAG = "beagleOverlayFragment"
        private const val SCREENSHOT_REQUEST = 4246
        private const val SCREEN_RECORDING_REQUEST = 4247

        fun newInstance() = OverlayFragment()
    }
}