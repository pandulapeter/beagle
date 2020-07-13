package com.pandulapeter.beagle.appDemo.feature

import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.pandulapeter.beagle.Beagle
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.databinding.ActivityBeagleDemoBinding
import com.pandulapeter.beagle.appDemo.feature.main.MainFragment
import com.pandulapeter.beagle.appDemo.feature.shared.BaseFragment
import com.pandulapeter.beagle.appDemo.utils.handleReplace
import com.pandulapeter.beagle.appDemo.utils.updateSystemBars
import com.pandulapeter.beagle.appDemo.utils.visible

class BeagleDemoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBeagleDemoBinding
    private val currentFragment get() = supportFragmentManager.findFragmentById(R.id.fragment_container) as? BaseFragment<*>?

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_beagle_demo)
        binding.beagleButton.setOnClickListener {
            Beagle.show()
            binding.debugMenu.visible = !binding.debugMenu.visible
        }
        if (savedInstanceState == null) {
            supportFragmentManager.handleReplace(transitionType = null, newInstance = MainFragment.Companion::newInstance)
        }
    }

    override fun onResume() {
        super.onResume()
        updateSystemBars()
    }

    override fun onBackPressed() {
        if (binding.debugMenu is FrameLayout && binding.debugMenu.visible) {
            binding.debugMenu.visible = false
        } else if (!Beagle.hide() && currentFragment?.onBackPressed() != true) {
            super.onBackPressed()
        }
    }
}