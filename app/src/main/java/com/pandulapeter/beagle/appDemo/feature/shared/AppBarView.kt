package com.pandulapeter.beagle.appDemo.feature.shared

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import androidx.annotation.StringRes
import androidx.fragment.app.FragmentActivity
import com.google.android.material.appbar.AppBarLayout
import com.pandulapeter.beagle.appDemo.databinding.ViewAppBarBinding
import com.pandulapeter.beagle.appDemo.utils.visible
import com.pandulapeter.beagle.utils.extensions.inflater

class AppBarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppBarLayout(context, attrs, defStyleAttr) {

    private val binding = ViewAppBarBinding.inflate(inflater, this, true)

    init {
        isLiftOnScroll = true
        val typedValue = TypedValue()
        context.theme.resolveAttribute(android.R.attr.windowBackground, typedValue, true)
        if (typedValue.type >= TypedValue.TYPE_FIRST_COLOR_INT && typedValue.type <= TypedValue.TYPE_LAST_COLOR_INT) {
            setBackgroundColor(typedValue.data)
        }
    }

    fun setup(@StringRes titleResourceId: Int, isRoot: Boolean, activity: FragmentActivity) {
        binding.toolbar.title = context.getString(titleResourceId)
        binding.backButton.run {
            visible = !isRoot
            setOnClickListener { activity.onBackPressed() }
        }
    }

    fun updateTopInset(topInset: Int) = binding.toolbarContainer.run {
        post { setPadding(paddingLeft, topInset, paddingRight, paddingBottom) }
    }
}