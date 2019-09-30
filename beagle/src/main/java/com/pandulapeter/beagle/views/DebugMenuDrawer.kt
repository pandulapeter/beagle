package com.pandulapeter.beagle.views

import android.content.Context
import android.util.AttributeSet
import android.view.WindowInsets
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.R
import com.pandulapeter.beagle.utils.dimension
import com.pandulapeter.beagle.utils.setBackgroundFromWindowBackground
import com.pandulapeter.beagle.views.items.DrawerItemViewModel

//TODO: Scroll position is not restored.
internal class DebugMenuDrawer @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {

    private val debugMenuAdapter = DebugMenuAdapter()

    init {
        setBackgroundFromWindowBackground()
        clipToPadding = false
        layoutManager = LinearLayoutManager(context)
        adapter = debugMenuAdapter
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        requestApplyInsets()
    }

    //TODO: This doesn't seem to be working in all cases.
    override fun dispatchApplyWindowInsets(insets: WindowInsets?): WindowInsets = super.dispatchApplyWindowInsets(insets?.also { windowsInsets ->
        context.dimension(R.dimen.large_content_padding).also { padding ->
            setPadding(paddingLeft, windowsInsets.systemWindowInsetTop, paddingRight, padding + windowsInsets.systemWindowInsetBottom)
        }
    })

    fun updateItems(items: List<DrawerItemViewModel>) = debugMenuAdapter.submitList(items)
}