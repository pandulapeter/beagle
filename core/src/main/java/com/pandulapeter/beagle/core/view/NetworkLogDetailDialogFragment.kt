package com.pandulapeter.beagle.core.view

import android.app.Dialog
import android.os.Bundle
import android.view.MenuItem
import android.view.ViewTreeObserver
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.appbar.AppBarLayout
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.core.R
import com.pandulapeter.beagle.core.util.extension.append
import com.pandulapeter.beagle.core.util.extension.applyTheme
import com.pandulapeter.beagle.core.util.extension.shareText
import com.pandulapeter.beagle.core.util.extension.withArguments
import com.pandulapeter.beagle.utils.BundleArgumentDelegate
import com.pandulapeter.beagle.utils.consume
import com.pandulapeter.beagle.utils.extensions.colorResource
import com.pandulapeter.beagle.utils.extensions.tintedDrawable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import org.json.JSONTokener
import kotlin.math.max

//TODO: Implement async parsing, add UI controls
internal class NetworkLogDetailDialogFragment : DialogFragment() {

    private lateinit var appBar: AppBarLayout
    private lateinit var toolbar: Toolbar
    private lateinit var textView: TextView
    private lateinit var scrollView: ScrollView
    private lateinit var shareButton: MenuItem
    private var isJsonReady = false
    private val scrollListener = ViewTreeObserver.OnScrollChangedListener { appBar.setLifted(scrollView.scrollY != 0) }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = AlertDialog.Builder(requireContext().applyTheme())
        .setView(R.layout.beagle_dialog_fragment_log_detail_scrolling)
        .create()

    override fun onResume() {
        super.onResume()
        dialog?.let { dialog ->
            appBar = dialog.findViewById(R.id.beagle_app_bar)
            toolbar = dialog.findViewById(R.id.beagle_toolbar)
            textView = dialog.findViewById(R.id.beagle_text_view)
            scrollView = dialog.findViewById(R.id.beagle_scroll_view)
            textView.text = "Loading…"
            appBar.run {
                setPadding(0, 0, 0, 0)
                setBackgroundColor(context.colorResource(R.attr.colorBackgroundFloating))
            }
            scrollView.viewTreeObserver.addOnScrollChangedListener(scrollListener)
            toolbar.run {
                val textColor = context.colorResource(android.R.attr.textColorPrimary)
                setNavigationOnClickListener { dismiss() }
                navigationIcon = context.tintedDrawable(R.drawable.beagle_ic_close, textColor)
                shareButton = menu.findItem(R.id.beagle_share).also {
                    it.title = BeagleCore.implementation.appearance.galleryShareHint
                    it.icon = context.tintedDrawable(R.drawable.beagle_ic_share, textColor)
                }
                setOnMenuItemClickListener(::onMenuItemClicked)
            }
            formatJson()
        }
    }

    //TODO: Use ViewModelScope, remove isJsonReady flag
    private fun formatJson() {
        if (!isJsonReady) {
            GlobalScope.launch {
                val prefix = if (arguments?.isOutgoing == true) "↑ " else "↓ "
                val url = arguments?.url.orEmpty()
                val text = "${prefix}${url}".append("\n• Headers:${arguments?.headers.orEmpty()}")
                    //TODO: .let { text -> formattedTimestamp?.let { text.append("\n• Timestamp: $it") } ?: text }
                    .let { text -> arguments?.duration?.let { text.append("\n• Duration: ${max(0, it)} ms") } ?: text }
                    .append("\n\n${arguments?.payload?.formatToJson()}")
                launch(Dispatchers.Main) {
                    textView.text = text
                    isJsonReady = true
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        scrollView.viewTreeObserver.removeOnScrollChangedListener(scrollListener)
    }

    private fun onMenuItemClicked(menuItem: MenuItem) = when (menuItem.itemId) {
        R.id.beagle_share -> consume { shareText() }
        else -> false
    }

    private fun shareText() {
        textView.text?.let { text ->
            activity?.shareText(text.toString())
        }
    }

    private fun String.formatToJson() = try {
        if (isJson()) {
            val obj = JSONTokener(this).nextValue()
            if (obj is JSONObject) obj.toString(4) else (obj as? JSONArray)?.toString(4) ?: (obj as String)
        } else this
    } catch (e: JSONException) {
        this
    }

    private fun String.isJson(): Boolean {
        try {
            JSONObject(this)
        } catch (_: JSONException) {
            try {
                JSONArray(this)
            } catch (_: JSONException) {
                return false
            }
        }
        return true
    }

    companion object {
        private var Bundle.isOutgoing by BundleArgumentDelegate.Boolean("isOutgoing")
        private var Bundle.url by BundleArgumentDelegate.String("url")
        private var Bundle.payload by BundleArgumentDelegate.String("payload")
        private var Bundle.headers by BundleArgumentDelegate.String("headers")
        private var Bundle.duration by BundleArgumentDelegate.Long("duration")
        private var Bundle.timestamp by BundleArgumentDelegate.Long("timestamp")

        fun show(
            fragmentManager: FragmentManager,
            isOutgoing: Boolean,
            url: String,
            payload: String,
            headers: List<String>?,
            duration: Long?,
            timestamp: Long
        ) = NetworkLogDetailDialogFragment().withArguments {
            it.isOutgoing = isOutgoing
            it.url = url
            it.payload = payload
            it.headers = if (headers.isNullOrEmpty()) " [none]" else headers.joinToString("") { header -> "\n    • $header" }
            it.duration = duration ?: -1L
            it.timestamp = timestamp
        }.run { show(fragmentManager, tag) }
    }
}