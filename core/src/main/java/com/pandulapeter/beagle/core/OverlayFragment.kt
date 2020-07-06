package com.pandulapeter.beagle.core

import android.R.attr
import android.content.Intent
import android.graphics.Bitmap
import android.media.projection.MediaProjectionManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.core.util.ScreenshotService


internal class OverlayFragment : Fragment() {

    private val mediaProjectionManager by lazy { if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) context?.getSystemService(MediaProjectionManager::class.java) else null }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) = BeagleCore.implementation.createOverlayLayout(requireActivity())

    @RequiresApi(Build.VERSION_CODES.M)
    fun takeScreenshot(callback: (Bitmap) -> Unit) {
        BeagleCore.implementation.onScreenshotReady = callback
        startActivityForResult(mediaProjectionManager?.createScreenCaptureIntent(), SCREENSHOT_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == SCREENSHOT_REQUEST && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            data?.let {
                context?.startService(
                    Intent(requireContext(), ScreenshotService::class.java)
                        .putExtra(ScreenshotService.EXTRA_RESULT_CODE, resultCode)
                        .putExtra(ScreenshotService.EXTRA_RESULT_INTENT, data)
                )
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    companion object {
        const val TAG = "beagleOverlayFragment"
        private const val SCREENSHOT_REQUEST = 4246

        fun newInstance() = OverlayFragment()
    }
}