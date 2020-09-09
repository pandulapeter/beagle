package com.pandulapeter.beagle.appDemo.feature.main.examples.overlay

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.feature.shared.ListViewModel
import com.pandulapeter.beagle.appDemo.feature.shared.list.CodeSnippetViewHolder
import com.pandulapeter.beagle.appDemo.feature.shared.list.ListItem
import com.pandulapeter.beagle.appDemo.feature.shared.list.TextViewHolder

class OverlayViewModel : ListViewModel<ListItem>() {

    override val items: LiveData<List<ListItem>> = MutableLiveData(
        listOf(
            TextViewHolder.UiModel(R.string.case_study_overlay_text_1),
            CodeSnippetViewHolder.UiModel(
                "Beagle.addOverlayListener(\n" +
                        "    listener = object : OverlayListener {\n" +
                        "        override fun onDrawOver(canvas: Canvas, insets: Insets) {\n" +
                        "            if (isOverlayEnabled) {\n" +
                        "                canvas.drawCircle(…)\n" +
                        "            }\n" +
                        "        }\n" +
                        "    },\n" +
                        "    lifecycleOwner = viewLifecycleOwner\n" +
                        ")"
            ),
            TextViewHolder.UiModel(R.string.case_study_overlay_text_2),
            CodeSnippetViewHolder.UiModel("Beagle.invalidateOverlay()"),
            TextViewHolder.UiModel(R.string.case_study_overlay_text_3)
        )
    )
}