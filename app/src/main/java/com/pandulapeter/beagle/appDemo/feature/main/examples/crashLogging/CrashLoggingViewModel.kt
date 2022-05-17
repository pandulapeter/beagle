package com.pandulapeter.beagle.appDemo.feature.main.examples.crashLogging

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.feature.shared.ListViewModel
import com.pandulapeter.beagle.appDemo.feature.shared.list.CodeSnippetViewHolder
import com.pandulapeter.beagle.appDemo.feature.shared.list.ListItem
import com.pandulapeter.beagle.appDemo.feature.shared.list.TextViewHolder

class CrashLoggingViewModel : ListViewModel<ListItem>() {

    override val items: LiveData<List<ListItem>> = MutableLiveData(
        listOf(
            TextViewHolder.UiModel(R.string.case_study_crash_logging_text_1),
            CodeSnippetViewHolder.UiModel(
                "dependencies {\n" +
                        "    …\n" +
                        "    debugImplementation \"io.github.pandulapeter.beagle:log-crash:\$beagleVersion\"\n" +
                        "    releaseImplementation \"io.github.pandulapeter.beagle:log-crash-noop:\$beagleVersion\"\n" +
                        "}"
            ),
            TextViewHolder.UiModel(R.string.case_study_crash_logging_text_2),
            CodeSnippetViewHolder.UiModel(
                "Beagle.initialize(\n" +
                        "    …\n" +
                        "    behavior = Behavior(\n" +
                        "        …\n" +
                        "        bugReportingBehavior = Behavior.BugReportingBehavior(\n" +
                        "            crashLoggers = listOf(BeagleCrashLogger),\n" +
                        "            …\n" +
                        "        )\n" +
                        "    )\n" +
                        ")"
            ),
            TextViewHolder.UiModel(R.string.case_study_crash_logging_text_3),
        )
    )
}