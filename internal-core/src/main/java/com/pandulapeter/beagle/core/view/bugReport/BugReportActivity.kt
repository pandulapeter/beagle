package com.pandulapeter.beagle.core.view.bugReport

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.ViewTreeObserver
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.common.configuration.toText
import com.pandulapeter.beagle.core.R
import com.pandulapeter.beagle.core.databinding.BeagleActivityBugReportBinding
import com.pandulapeter.beagle.core.list.delegates.LifecycleLogListDelegate
import com.pandulapeter.beagle.core.util.crashLogEntryAdapter
import com.pandulapeter.beagle.core.util.extension.text
import com.pandulapeter.beagle.core.util.extension.visible
import com.pandulapeter.beagle.core.util.generateBuildInformation
import com.pandulapeter.beagle.core.util.generateDeviceInformation
import com.pandulapeter.beagle.core.util.model.SerializableCrashLogEntry
import com.pandulapeter.beagle.core.util.model.SerializableLifecycleLogEntry
import com.pandulapeter.beagle.core.util.model.SerializableLogEntry
import com.pandulapeter.beagle.core.util.model.SerializableNetworkLogEntry
import com.pandulapeter.beagle.core.util.restoreModelAdapter
import com.pandulapeter.beagle.core.view.bugReport.list.BugReportAdapter
import com.pandulapeter.beagle.core.view.gallery.MediaPreviewDialogFragment
import com.pandulapeter.beagle.utils.consume
import com.pandulapeter.beagle.utils.extensions.colorResource
import com.pandulapeter.beagle.utils.extensions.dimension
import com.pandulapeter.beagle.utils.extensions.hideKeyboard
import com.pandulapeter.beagle.utils.extensions.tintedDrawable
import com.squareup.moshi.JsonDataException

class BugReportActivity : AppCompatActivity() {

    private lateinit var binding: BeagleActivityBugReportBinding
    private val crashLogEntryToShow by lazy {
        intent.getStringExtra(CRASH_LOG_ENTRY_TO_SHOW).let { inputString ->
            if (inputString.isNullOrBlank()) null else try {
                crashLogEntryAdapter.fromJson(inputString)
            } catch (_: JsonDataException) {
                null
            }
        }
    }
    private val restoreModel by lazy {
        intent.getStringExtra(RESTORE_MODEL).let { inputString ->
            if (inputString.isNullOrBlank()) null else try {
                restoreModelAdapter.fromJson(inputString)
            } catch (_: JsonDataException) {
                null
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    private val viewModel by lazy {
        ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T = BeagleCore.implementation.behavior.bugReportingBehavior.run {
                BugReportViewModel(
                    application = application,
                    restoreModel = restoreModel,
                    crashLogEntryToShow = crashLogEntryToShow,
                    buildInformation = buildInformation(application).generateBuildInformation(this@BugReportActivity),
                    deviceInformation = generateDeviceInformation(this@BugReportActivity),
                    textInputTitles = textInputFields.map { it.first },
                    textInputDescriptions = textInputFields.map { it.second }
                ) as T
            }
        }).get(BugReportViewModel::class.java)
    }
    private lateinit var sendButton: MenuItem
    private val onGlobalLayoutListener = ViewTreeObserver.OnGlobalLayoutListener {
        binding.beagleRecyclerView.run { post { binding.beagleAppBar.isLifted = computeVerticalScrollOffset() != 0 } }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        BeagleCore.implementation.appearance.themeResourceId?.let { setTheme(it) }
        super.onCreate(savedInstanceState)
        binding = BeagleActivityBugReportBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        binding.beagleToolbar.run {
            val textColor = colorResource(android.R.attr.textColorPrimary)
            setNavigationOnClickListener { supportFinishAfterTransition() }
            navigationIcon = tintedDrawable(R.drawable.beagle_ic_close, textColor)
            title = text(BeagleCore.implementation.appearance.bugReportTexts.title)
            sendButton = menu.findItem(R.id.beagle_send).also {
                it.title = text(BeagleCore.implementation.appearance.bugReportTexts.sendButtonHint)
                it.icon = tintedDrawable(R.drawable.beagle_ic_send, textColor)
            }
            setOnMenuItemClickListener(::onMenuItemClicked)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            val contentPadding by lazy { dimension(R.dimen.beagle_content_padding) }
            binding.beagleBottomNavigationOverlay.setBackgroundColor(if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) window.navigationBarColor else Color.BLACK)
            window.decorView.run {
                setOnApplyWindowInsetsListener { _, insets ->
                    onApplyWindowInsets(insets).also {
                        binding.beagleToolbar.setPadding(it.systemWindowInsetLeft, 0, it.systemWindowInsetRight, 0)
                        binding.beagleRecyclerView.setPadding(0, 0, 0, it.systemWindowInsetBottom + contentPadding)
                        binding.beagleBottomNavigationOverlay.run { layoutParams = layoutParams.apply { height = it.systemWindowInsetBottom } }
                    }
                }
                requestApplyInsets()
            }
        }
        val bugReportAdapter = BugReportAdapter(
            onMediaFileSelected = ::showMediaPreviewDialog,
            onMediaFileLongTapped = viewModel::onMediaFileLongTapped,
            onCrashLogSelected = { id -> viewModel.allCrashLogEntries?.firstOrNull { it.id == id }?.let(::showCrashLogDetailDialog) },
            onCrashLogLongTapped = viewModel::onCrashLogLongTapped,
            onNetworkLogSelected = { id -> viewModel.allNetworkLogEntries.firstOrNull { it.id == id }?.let(::showNetworkLogDetailDialog) },
            onNetworkLogLongTapped = viewModel::onNetworkLogLongTapped,
            onLogSelected = { id, label -> BeagleCore.implementation.getLogEntriesInternal(label).firstOrNull { it.id == id }?.let(::showLogDetailDialog) },
            onLogLongTapped = viewModel::onLogLongTapped,
            onLifecycleLogSelected = { id -> viewModel.allLifecycleLogEntries.firstOrNull { it.id == id }?.let(::showLifecycleLogDetailDialog) },
            onLifecycleLogLongTapped = viewModel::onLifecycleLogLongTapped,
            onShowMoreTapped = viewModel::onShowMoreTapped,
            onDescriptionChanged = viewModel::onDescriptionChanged,
            onMetadataItemClicked = ::showMetadataDetailDialog,
            onMetadataItemSelectionChanged = viewModel::onMetadataItemSelectionChanged,
            onAttachAllButtonClicked = viewModel::onAttachAllButtonClicked
        )
        binding.beagleRecyclerView.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = bugReportAdapter
        }
        viewModel.items.observe(this, bugReportAdapter::submitList)
        viewModel.shouldShowLoadingIndicator.observe(this) { shouldShowLoadingIndicator ->
            binding.beagleProgressBar.visible = shouldShowLoadingIndicator
            if (shouldShowLoadingIndicator) {
                currentFocus?.hideKeyboard()
            } else {
                crashLogEntryToShow?.let { crashLogEntryToShow ->
                    if (!intent.getStringExtra(CRASH_LOG_ENTRY_TO_SHOW).isNullOrBlank()) {
                        intent.removeExtra(CRASH_LOG_ENTRY_TO_SHOW)
                        viewModel.onCrashLogLongTapped(crashLogEntryToShow.id)
                        viewModel.allCrashLogEntries?.firstOrNull { it.id == crashLogEntryToShow.id }?.let(::showCrashLogDetailDialog)
                    }
                }
            }
        }
        viewModel.isSendButtonEnabled.observe(this) {
            sendButton.isEnabled = it
            sendButton.icon.alpha = if (it) 255 else 63
        }
    }

    override fun onResume() {
        super.onResume()
        binding.beagleRecyclerView.viewTreeObserver.addOnGlobalLayoutListener(onGlobalLayoutListener)
    }

    override fun onPause() {
        super.onPause()
        binding.beagleRecyclerView.viewTreeObserver.removeOnGlobalLayoutListener(onGlobalLayoutListener)
    }

    fun refresh() = viewModel.refresh()

    private fun onMenuItemClicked(menuItem: MenuItem) = when (menuItem.itemId) {
        R.id.beagle_send -> consume(viewModel::onSendButtonPressed)
        else -> false
    }

    private fun showMediaPreviewDialog(fileName: String) = MediaPreviewDialogFragment.show(supportFragmentManager, fileName)

    private fun showCrashLogDetailDialog(entry: SerializableCrashLogEntry) = BeagleCore.implementation.showDialog(
        content = entry.getFormattedContents(BeagleCore.implementation.appearance.logLongTimestampFormatter).toText(),
        isHorizontalScrollEnabled = true,
        shouldShowShareButton = true,
        timestamp = entry.timestamp,
        id = entry.id,
        fileName = BeagleCore.implementation.behavior.bugReportingBehavior.getCrashLogFileName(entry.timestamp, entry.id)
    )

    private fun showNetworkLogDetailDialog(entry: SerializableNetworkLogEntry) = BeagleCore.implementation.showNetworkEventDialog(
        isOutgoing = entry.isOutgoing,
        url = entry.url,
        payload = entry.payload,
        headers = entry.headers,
        duration = entry.duration,
        timestamp = entry.timestamp,
        id = entry.id
    )

    private fun showLogDetailDialog(entry: SerializableLogEntry) = BeagleCore.implementation.showDialog(
        content = entry.getFormattedContents(BeagleCore.implementation.appearance.logLongTimestampFormatter).toText(),
        timestamp = entry.timestamp,
        id = entry.id
    )

    private fun showLifecycleLogDetailDialog(entry: SerializableLifecycleLogEntry) = BeagleCore.implementation.showDialog(
        content = LifecycleLogListDelegate.format(
            entry = entry,
            formatter = BeagleCore.implementation.appearance.logLongTimestampFormatter,
            shouldDisplayFullNames = BeagleCore.implementation.behavior.lifecycleLogBehavior.shouldDisplayFullNames
        ),
        isHorizontalScrollEnabled = false,
        shouldShowShareButton = true,
        timestamp = entry.timestamp,
        id = entry.id,
        fileName = BeagleCore.implementation.behavior.lifecycleLogBehavior.getFileName(entry.timestamp, entry.id)
    )

    private fun showMetadataDetailDialog(type: BugReportViewModel.MetadataType) = BeagleCore.implementation.showDialog(
        content = when (type) {
            BugReportViewModel.MetadataType.BUILD_INFORMATION -> viewModel.buildInformation
            BugReportViewModel.MetadataType.DEVICE_INFORMATION -> viewModel.deviceInformation
        }.toText(),
        shouldShowShareButton = false
    )

    companion object {
        private const val CRASH_LOG_ENTRY_TO_SHOW = "crashLogEntry"
        private const val RESTORE_MODEL = "restoreModel"

        fun newIntent(
            context: Context,
            crashLogEntryToShowJson: String = "",
            restoreModelJson: String = ""
        ) = Intent(context, BugReportActivity::class.java)
            .putExtra(CRASH_LOG_ENTRY_TO_SHOW, crashLogEntryToShowJson)
            .putExtra(RESTORE_MODEL, restoreModelJson)
    }
}