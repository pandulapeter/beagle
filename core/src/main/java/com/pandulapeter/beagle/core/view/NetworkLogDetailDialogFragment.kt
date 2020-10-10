package com.pandulapeter.beagle.core.view

import android.app.Dialog
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StyleSpan
import android.view.MenuItem
import android.view.ViewTreeObserver
import android.widget.ProgressBar
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
import com.pandulapeter.beagle.core.util.extension.text
import com.pandulapeter.beagle.core.util.extension.visible
import com.pandulapeter.beagle.core.util.extension.withArguments
import com.pandulapeter.beagle.utils.BundleArgumentDelegate
import com.pandulapeter.beagle.utils.consume
import com.pandulapeter.beagle.utils.extensions.colorResource
import com.pandulapeter.beagle.utils.extensions.tintedDrawable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import org.json.JSONTokener
import kotlin.math.max

//TODO: Add UI controls for showing / hiding event metadata
internal class NetworkLogDetailDialogFragment : DialogFragment() {

    private lateinit var appBar: AppBarLayout
    private lateinit var toolbar: Toolbar
    private lateinit var textView: TextView
    private lateinit var scrollView: ScrollView
    private lateinit var progressBar: ProgressBar
    private lateinit var shareButton: MenuItem
    private var isJsonReady = false
    private var job: Job? = null
    private val scrollListener = ViewTreeObserver.OnScrollChangedListener { appBar.setLifted(scrollView.scrollY != 0) }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = AlertDialog.Builder(requireContext().applyTheme())
        .setView(R.layout.beagle_dialog_fragment_network_log_detail)
        .create()

    override fun onResume() {
        super.onResume()
        dialog?.let { dialog ->
            appBar = dialog.findViewById(R.id.beagle_app_bar)
            toolbar = dialog.findViewById(R.id.beagle_toolbar)
            textView = dialog.findViewById(R.id.beagle_text_view)
            scrollView = dialog.findViewById(R.id.beagle_scroll_view)
            progressBar = dialog.findViewById(R.id.beagle_progress_bar)
            appBar.run {
                setPadding(0, 0, 0, 0)
                setBackgroundColor(context.colorResource(R.attr.colorBackgroundFloating))
            }
            textView.setTextIsSelectable(BeagleCore.implementation.behavior.shouldAllowSelectionInDialogs)
            scrollView.viewTreeObserver.addOnScrollChangedListener(scrollListener)
            toolbar.run {
                val textColor = context.colorResource(android.R.attr.textColorPrimary)
                setNavigationOnClickListener { dismiss() }
                navigationIcon = context.tintedDrawable(R.drawable.beagle_ic_close, textColor)
                shareButton = menu.findItem(R.id.beagle_share).also {
                    it.title = context.text(BeagleCore.implementation.appearance.shareHint)
                    it.icon = context.tintedDrawable(R.drawable.beagle_ic_share, textColor)
                }
                setOnMenuItemClickListener(::onMenuItemClicked)
            }
            formatJson()
        }
    }

    //TODO: Use ViewModelScope, remove isJsonReady flag
    private fun formatJson() {
        if (isJsonReady) {
            progressBar.visible = false
        } else {
            job?.cancel()
            job = GlobalScope.launch {
                val title = "${if (arguments?.isOutgoing == true) "↑" else "↓"} ${arguments?.url.orEmpty()}"
                val text = SpannableString(
                    title
                        .append("\n\n• Headers:${arguments?.headers?.formatHeaders()}")
                        .let { text ->
                            arguments?.timestamp?.let { text.append("\n• Timestamp: ${BeagleCore.implementation.appearance.networkEventTimestampFormatter(it)}") } ?: text
                        }
                        .let { text -> arguments?.duration?.let { text.append("\n• Duration: ${max(0, it)} ms") } ?: text }
                        .append("\n\n${arguments?.payload?.formatToJson()}")
                ).apply { setSpan(StyleSpan(Typeface.BOLD), 0, title.length, Spanned.SPAN_INCLUSIVE_INCLUSIVE) }
                launch(Dispatchers.Main) {
                    progressBar.visible = false
                    textView.text = text
                    isJsonReady = true
                    job = null
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        job?.cancel()
        scrollView.viewTreeObserver.removeOnScrollChangedListener(scrollListener)
    }

    private fun onMenuItemClicked(menuItem: MenuItem) = when (menuItem.itemId) {
        R.id.beagle_share -> consume { shareText() }
        else -> false
    }

    private fun List<String>?.formatHeaders() = if (isNullOrEmpty()) " [none]" else joinToString("") { header -> "\n    • $header" }

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
        private var Bundle.headers by BundleArgumentDelegate.StringList("headers")
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
            it.headers = headers.orEmpty()
            it.duration = duration ?: -1L
            it.timestamp = timestamp
        }.run { show(fragmentManager, tag) }
    }
}