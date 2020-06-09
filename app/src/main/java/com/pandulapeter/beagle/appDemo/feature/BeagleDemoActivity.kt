package com.pandulapeter.beagle.appDemo.feature

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.pandulapeter.beagle.Beagle
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.databinding.ActivityBeagleDemoBinding
import com.pandulapeter.beagle.appDemo.feature.main.MainFragment
import com.pandulapeter.beagle.appDemo.utils.handleReplace
import com.pandulapeter.beagle.appDemo.utils.updateSystemBars
import com.pandulapeter.beagle.appDemo.utils.visible

class BeagleDemoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBeagleDemoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_beagle_demo)
        binding.beagleButton.setOnClickListener { Beagle.show() }
        binding.backButton.setOnClickListener { onBackPressed() }
        if (savedInstanceState == null) {
            supportFragmentManager.handleReplace(newInstance = MainFragment.Companion::newInstance)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            binding.root.setOnApplyWindowInsetsListener { _, insets ->
                insets.also { binding.appBarLayout.run { setPadding(paddingLeft, insets.systemWindowInsetTop, paddingRight, paddingBottom) } }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        updateSystemBars()
    }

    fun updateToolbar(shouldShowBackButton: Boolean, title: String) {
        binding.backButton.visible = shouldShowBackButton
        binding.toolbar.title = title
    }

    override fun onBackPressed() {
        if (!Beagle.hide()) {
            super.onBackPressed()
        }
    }
}