package com.pandulapeter.beagle.appDemo.feature

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.pandulapeter.beagle.Beagle
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.databinding.ActivityBeagleDemoBinding
import com.pandulapeter.beagle.appDemo.feature.main.MainFragment
import com.pandulapeter.beagle.appDemo.utils.handleReplace
import com.pandulapeter.beagle.appDemo.utils.updateSystemBars

class BeagleDemoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBeagleDemoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_beagle_demo)
        binding.beagleButton.setOnClickListener { Beagle.show() }
        if (savedInstanceState == null) {
            supportFragmentManager.handleReplace(containerId = R.id.main_fragment_container, newInstance = MainFragment.Companion::newInstance)
        }
    }

    override fun onResume() {
        super.onResume()
        updateSystemBars()
    }

    override fun onBackPressed() {
        if (!Beagle.hide()) {
            super.onBackPressed()
        }
    }
}