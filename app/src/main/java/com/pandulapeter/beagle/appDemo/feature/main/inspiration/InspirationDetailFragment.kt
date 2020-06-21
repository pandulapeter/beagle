package com.pandulapeter.beagle.appDemo.feature.main.inspiration

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.annotation.StringRes
import androidx.core.view.ViewCompat
import com.google.android.material.transition.MaterialContainerTransform
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.feature.shared.ListFragment
import com.pandulapeter.beagle.appDemo.feature.shared.list.ListItem
import com.pandulapeter.beagle.appDemo.feature.shared.ListViewModel
import com.pandulapeter.beagle.appDemo.utils.shouldUseContainerTransform

abstract class InspirationDetailFragment<VM : ListViewModel<LI>, LI : ListItem>(
    @StringRes titleResourceId: Int
) : ListFragment<VM, LI>(titleResourceId, R.color.window_background) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (shouldUseContainerTransform) {
            sharedElementEnterTransition = MaterialContainerTransform().apply {
                scrimColor = Color.TRANSPARENT
                fadeProgressThresholds = MaterialContainerTransform.ProgressThresholds(0f, 0.5f)
                scaleProgressThresholds = MaterialContainerTransform.ProgressThresholds(0f, 1f)
                scaleMaskProgressThresholds = MaterialContainerTransform.ProgressThresholds(0f, 1f)
                shapeMaskProgressThresholds = MaterialContainerTransform.ProgressThresholds(0f, 1f)
                fadeMode = MaterialContainerTransform.FADE_MODE_IN
                drawingViewId = R.id.fragment_container
            }
        }
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        postponeEnterTransition()
        super.onViewCreated(view, savedInstanceState)
        ViewCompat.setTransitionName(binding.root, getString(titleResourceId))
    }
}