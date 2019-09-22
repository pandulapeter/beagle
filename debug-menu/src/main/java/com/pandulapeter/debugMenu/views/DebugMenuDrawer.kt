package com.pandulapeter.debugMenu.views

import android.content.Context
import android.util.AttributeSet
import android.view.WindowInsets
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.debugMenu.R
import com.pandulapeter.debugMenu.utils.dimension
import com.pandulapeter.debugMenu.views.items.DrawerItemViewModel

//TODO: Scroll position is not restored.
internal class DebugMenuDrawer @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {

    private val debugMenuAdapter = DebugMenuAdapter()

    init {
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