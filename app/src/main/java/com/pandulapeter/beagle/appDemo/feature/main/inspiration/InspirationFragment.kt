package com.pandulapeter.beagle.appDemo.feature.main.inspiration

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.pandulapeter.beagle.Beagle
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.databinding.FragmentInspirationBinding
import com.pandulapeter.beagle.appDemo.feature.main.inspiration.list.InspirationAdapter
import com.pandulapeter.beagle.appDemo.feature.shared.BaseViewModelFragment
import com.pandulapeter.beagle.common.contracts.BeagleListItemContract
import com.pandulapeter.beagle.modules.ButtonModule
import com.pandulapeter.beagle.modules.CheckBoxModule
import com.pandulapeter.beagle.modules.SingleSelectionListModule
import com.pandulapeter.beagle.modules.SwitchModule
import com.pandulapeter.beagle.modules.TextModule
import org.koin.androidx.viewmodel.ext.android.viewModel

class InspirationFragment : BaseViewModelFragment<FragmentInspirationBinding, InspirationViewModel>(R.layout.fragment_inspiration, true, R.string.inspiration_title) {

    override val viewModel by viewModel<InspirationViewModel>()
    private val radioGroupOptions by lazy {
        listOf(
            RadioGroupOption(getString(R.string.inspiration_beagle_radio_group_option_1)),
            RadioGroupOption(getString(R.string.inspiration_beagle_radio_group_option_2)),
            RadioGroupOption(getString(R.string.inspiration_beagle_radio_group_option_3))
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val inspirationAdapter = InspirationAdapter()
        binding.recyclerView.run {
            adapter = inspirationAdapter
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
        }
        viewModel.items.observeForever(inspirationAdapter::submitList)
        Beagle.setModules(
            TextModule(text = getText(R.string.inspiration_beagle_text_1)),
            SwitchModule(text = getString(R.string.inspiration_beagle_switch), onValueChanged = {}),
            ButtonModule(text = getString(R.string.inspiration_beagle_button), onButtonPressed = {}),
            CheckBoxModule(text = getString(R.string.inspiration_beagle_check_box), onValueChanged = {}),
            SingleSelectionListModule(
                title = getString(R.string.inspiration_beagle_radio_group),
                items = radioGroupOptions,
                initiallySelectedItemId = radioGroupOptions.first().id,
                onSelectionChanged = {}
            ),
            TextModule(text = getText(R.string.inspiration_beagle_text_2))
        )
    }

    private data class RadioGroupOption(
        override val name: String,
        override val id: String = name
    ) : BeagleListItemContract

    companion object {
        fun newInstance() = InspirationFragment()
    }
}