package com.pandulapeter.beagle.appDemo.feature.shared

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.StringRes
import androidx.fragment.app.FragmentActivity
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.utils.visible

class AppBarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppBarLayout(context, attrs, defStyleAttr) {

    private val toolbarContainer by lazy { findViewById<View>(R.id.toolbar_container) }

    init {
        LayoutInflater.from(context).inflate(R.layout.view_app_bar, this, true)
        isLiftOnScroll = true
        val typedValue = TypedValue()
        context.theme.resolveAttribute(android.R.attr.windowBackground, typedValue, true)
        if (typedValue.type >= TypedValue.TYPE_FIRST_COLOR_INT && typedValue.type <= TypedValue.TYPE_LAST_COLOR_INT) {
            setBackgroundColor(typedValue.data)
        }
    }

    fun setup(@StringRes titleResourceId: Int, isRoot: Boolean, activity: FragmentActivity) {
        findViewById<MaterialToolbar>(R.id.toolbar).title = context.getString(titleResourceId)
        findViewById<View>(R.id.back_button).run {
            visible = !isRoot
            setOnClickListener { activity.onBackPressed() }
        }
    }

    fun updateTopInset(topInset: Int) = toolbarContainer.run {
        post { setPadding(paddingLeft, topInset, paddingRight, paddingBottom) }
    }
}